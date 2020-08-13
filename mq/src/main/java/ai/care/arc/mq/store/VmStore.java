package ai.care.arc.mq.store;

import ai.care.arc.mq.Message;
import ai.care.arc.mq.consumer.Partition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存储层
 *
 * @see Partition
 * @see Message
 */
public class VmStore implements Store {

    private Map<Partition, LinkedBlockingQueue<Message>> messageQueueMap = new ConcurrentHashMap<>();

    @Override
    public void append(Message message, Partition partition) {
        initQueue(partition);
        messageQueueMap.get(partition).add(message);
    }

    @Override
    public LinkedBlockingQueue<Message> findByPartition(Partition partition) {
        initQueue(partition);
        return messageQueueMap.get(partition);
    }

    private void initQueue(Partition partition) {
        if (!messageQueueMap.containsKey(partition)) {
            synchronized (this) {
                if (!messageQueueMap.containsKey(partition)) {
                    messageQueueMap.put(partition, new LinkedBlockingQueue<>());
                }
            }
        }
    }
}
