package ai.care.arc.mq.store;

import ai.care.arc.mq.Message;
import ai.care.arc.mq.consumer.Partition;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存储层
 */
public interface Store {

    void append(Message message, Partition partition);

    LinkedBlockingQueue<Message> findByPartition(Partition partition);

}
