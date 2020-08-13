package ai.care.arc.mq.consumer;

import java.util.Objects;

/**
 * 消息分区
 *
 * @see ConsumerCluster
 * @see ai.care.arc.mq.store.Store
 */
public class Partition {

    public final String consumerId;
    public final String topic;

    public Partition(String consumerId, String topic) {
        this.consumerId = consumerId;
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partition partition = (Partition) o;
        return Objects.equals(consumerId, partition.consumerId) &&
                Objects.equals(topic, partition.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumerId, topic);
    }
}
