package com.github.yituhealthcare.arc.mq.store;

import com.github.yituhealthcare.arc.mq.Message;
import com.github.yituhealthcare.arc.mq.consumer.Partition;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存储层
 */
public interface Store {

    void append(Message message, Partition partition);

    LinkedBlockingQueue<Message> findByPartition(Partition partition);

}
