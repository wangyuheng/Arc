package io.github.wangyuheng.arc.mq.store;

import io.github.wangyuheng.arc.mq.Message;
import io.github.wangyuheng.arc.mq.consumer.Partition;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存储层
 */
public interface Store {

    void append(Message message, Partition partition);

    LinkedBlockingQueue<Message> findByPartition(Partition partition);

}
