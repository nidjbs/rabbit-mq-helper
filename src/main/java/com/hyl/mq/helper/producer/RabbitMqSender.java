package com.hyl.mq.helper.producer;

/**
 * @author huayuanlin
 * @date 2021/09/27 14:19
 * @desc the interface desc
 */
public interface RabbitMqSender {


     /**
      * send msg
      *
      * @param exchange exchange
      * @param routingKey routingKey
      * @param msg msg
      */
     void send(String exchange, String routingKey, String msg);

}
