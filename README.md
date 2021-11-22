#  一、前言 
本工具暂仅解决rabbitMq的常见场景，如可靠投递、消费重试、消费幂，视你的业务场景选择，基本无侵入的实现上述功能，让你只需关注业务的逻辑。  
本文不对rabbitmq架构设计以及消息的可靠投递、幂等等概念做过多解析，具体可参考别的文章。
> - [x] 注：本工具与网上大部分文章提出的方案大概一致，只是基于spring-rabbitMq做了封装，让你尽可能的少关注这一层面的代码实现。
# 二、设计与实现
### 1、消息可靠性实现
 消息的可靠性保证基本从两方入手，一方面是发送者、一方面是消费者。  
###### **发送侧**   
 与大多数mq设计一致，rabbitMq提供了broker confirm机制，在发送消息后broker将会'回调'触发confirm，如下图：  
 ![mq  confirm](https://hyl-blog.oss-cn-beijing.aliyuncs.com/blog-content/20190401230750555.png)
 rabbit-mq提供了两处confirm，一是在消息到达交换机时进行的confirm，另外是由交换机到达queue时进行的confirm，考虑到交换机一般我们都会选择消息持久化，所以这里仅监听到达交换机一层的confirm回调。
 使用较为简单，仅需让你的sender继承AbstractConfirmedMqSender类即可:
 
```
// 此处手动指定bean名字是为了后面方便于补偿逻辑的执行
@Component("testProducer")
public class TestProducer extends AbstractConfirmedMqSender {

}
```

值得注意的是，AbstractConfirmedMqSender默认注入RabbitTemplate作为发送的RabbitTemplate，如果你需要是你自定义的RabbitTemplate，则可重写rabbitTemplate()方法。  
另外：假如消息发送失败，会异步重试直到达到最大次数，次数由mq.helper.retryTimes 配置，默认为3。    假设超过最大次数还是失败，则你可选择入库，或者丢弃（由重写isRecordWhenFail方法决定）。

> 注：  请确定开启配置：publisher-confirms: true；否则broker并不会回调confirm。  

---

###### **消费侧**
在消费侧，mq依然提供了类似于sender的机制，默认情况下当消息投递到消费者将会将该消息剔除，所以我们需要将消息确认模式改为MANUAL，即手动确认：

```
acknowledge-mode: manual
```
手动确认的好处是当消费发生错误，我们可以做相应的处理。
下面是当消费出现异常，我们采集重试的策略的方式：

```
    @HpRabbitListener(queues = "testDirectQueue", retryTimes = 3, retrySleepTimeOut = 5000)
    public void consumer(@Payload String msg, Channel channel, Message message) {
        System.out.println("消费" + msg);
        throw new UnsupportedOperationException();
    }
```
@HpRabbitListener可以指定与@RabbitListener所有相同的属性，并且可以指定消费逻辑在处理异常时如何应对。  
上述例子中设定消费失败时最大重试3次，并且为了防止快速重试，每次消费失败后休眠5秒，具体参数视你的场景而定。  
> 注：这里的休眠时间并不等于重试的时间间隔，而是休眠一段时候后nack，消息重新回到队列中。  
> 开启重试时请尽量保证关闭spring-rabbitmq自带的重试机制：spring.rabbitmq.listener.simple.retry.enabled = false。

### 2、消费幂等
幂等在很多业务场景中都很常见，下述是mq消费幂等的设计，幂等的概念这里不再赘述。  
一般的，幂等一般由消费者去保证（生产者由于可能需要可靠投递等因素变的不太可控，较难实现有且仅投递一次）。      
所以本工具幂等的设计是通过生产者提供消息的唯一code，在业务侧消费前进行判断该消息是否消费过，假设消费过则不再消费，若未消费过则正常消费即可。   而我们一般的架构都是分布式架构，所以如何判断该消息唯一code是否消费过是实现幂等的关键所在，本工具提供两种方式实现：  

---
**1）通过数据库的唯一索引约束实现**

```
    @HpRabbitListener(queues = "testDirectQueue", openIdempotent = true,idempotentType = HpRabbitListener.IDEMPOTENT.DB)
    public void consumer2(@Payload String msg, Channel channel, Message message) {
        System.out.println("消费" + msg);
    }
```
需要将openIdempotent设置为true，并指定幂等实现类型：HpRabbitListener.IDEMPOTENT.D。  
值得注意的是，此种方式在消息多时，性能不大好，因为依赖于数据库的唯一索引机制，不大推荐使用。
> 注： 幂等与重试机制是相违背的，所以在你选择开启幂等后，重试机制自动失效。

---

**2）通过redis的set原子命令实现**

```
    @HpRabbitListener(queues = "testDirectQueue", openIdempotent = true,idempotentType = HpRabbitListener.IDEMPOTENT.REDIS)
    public void consumer2(@Payload String msg, Channel channel, Message message) {
        System.out.println("消费" + msg);
    }

```
此种方式依赖于redis的set原子命令，默认key的过期时间为1天，你可通过mq.helper.consumer.redisIdempotentLockTtl 设置（单位为ms）。

---

### 3、包裹事务强一致性
一般的，在消息消费中我们会有rpc调用修改某个业务，一旦后面的接口异常会导致出现分布式事务问题，并不能完全保证强一致性，当然这属于分布式事务问题的范畴；在我们hp工具中，你的业务逻辑执行与Ack其实是存在不一致问题的，一旦ack操作报错，并不会回滚你的事务。但是考虑到我们很多场景是需要rpc的，所以全局包裹事务意义不大。  
但是本工具还是预留了该功能，因为某些需强一致性场景下(消费仅存在本地事务)时，全局包裹一个事务是有必要的；

```
    @HpRabbitListener(queues = "testDirectQueue", openIdempotent = true, idempotentType = HpRabbitListener.IDEMPOTENT.REDIS, wrapTx = true, txTimeOut = 5)
    public void consumer3(@Payload String msg, Channel channel, Message message) {
        System.out.println("消费" + msg);
        // db 
        // 。。。
    }
```
> 注：txTimeOut可指定事务超时时间，默认5s，并且包裹事务可以与上述的重试和幂等共用。开启包裹事务后可能会影响你的消费者消费能力。


> 
# 三 、消费补偿
针对上述的功能，都会有一些场景下会导致消息出现发送失败或者消费失败的场景，这是无法避免的。  
如消费逻辑存在异常或者服务宕机等等情况，这里提供最终一致性的方案即定时重试来对异常情况进行补偿。
### 1、消息发送者补偿
针对发送失败（重试后还是失败）的消息，最终会落库到表：**mq_send_fail_log**，本工具提供了本地补偿的功能，仅需做如下配置即可开启发送者的本地缓存：
```
mq:
  helper:
    compensator:
      localSenderEnable: true #开启自带的发送者本地补偿机制，默认5秒执行一次，每次处理50条失败记录。
      localSenderPeriod：10000 # sender补偿定时任务间隔
```
一般的，我们根据该条消息的sender的bean进行重新发送，不论是否成功到达交换机，该条记录都被认为是补偿成功（如若最终失败发送，将会产生新的失败记录）。  
开启上述功能后，能够保证你的消息最终都会正确的投递出去，当然这会增加消息被重复投递的可能性。

### 2、消息消费补偿
消费者的补偿较为复杂，因为这与你的业务紧密相关；  
本工具提供本地定时任务补偿机制（不采用分布式的原因是不想耦合别的第三方框架），用于补偿你的消费失败的消息，以达到最终一致性。  

```
mq:
  helper:
    compensator:
      localConsumerEnable: true #开启消费者者本地补偿机制，默认5秒执行一次，每次处理50条失败记录。
      localSenderPeriod：10000 # consumer补偿定时任务间隔      
      localConsumerQueueNames，: # 需要补偿的消费者队列，未指定的将不会定时补偿
        - "testDirectQueue"
```
假设你配置了localConsumerQueueNames，则本地定时触发后，默认将会找到你标注了注解@HpRabbitListener的消费方法进行反射调用补偿，如需要自定义不同队列的补偿逻辑，则可以实现接口CustomerCompensator，如下：

```
@Component
public class TestCustomerCompensator implements CustomerCompensator<String> {
    @Override
    public Set<String> queueNames() {
        return Collections.singleton("testDirectQueue");
    }

    @Override
    public void doCompensate(String msg) {
        System.out.println("补偿消费成功,msg:" + msg);
    }
}
```
本工具会优先使用你自定义的补偿逻辑进行补偿调用，请注意如果你的补偿逻辑发生异常，则该条记录会一致重试直到成功。



---
github源码地址：https://github.com/nidjbs/rabbit-mq-helper
