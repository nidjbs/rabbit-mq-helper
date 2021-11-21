package com.hyl.mq.helper.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author huayuanlin
 * @date 2021/11/19 10:34
 * @desc the class desc
 */
public class TreadPoolStrategy {

    private TreadPoolStrategy() {
        throw new UnsupportedOperationException();
    }


    /**
     * when rejected happened ,add the new task and run the oldest task
     *
     * @return rejected execution handler
     */
    public static RejectedExecutionHandler runsOldestTaskPolicy() {
        return (r, executor) -> {
            if (executor.isShutdown()) {
                return;
            }
            BlockingQueue<Runnable> workQueue = executor.getQueue();
            Runnable firstWork = workQueue.poll();
            boolean newTaskAdd = workQueue.offer(r);
            if (firstWork != null) {
                firstWork.run();
            }
            if (!newTaskAdd) {
                executor.execute(r);
            }
        };
    }

}
