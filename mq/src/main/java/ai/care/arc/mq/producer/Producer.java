package ai.care.arc.mq.producer;

import ai.care.arc.mq.Message;

/**
 * 消息发送者
 *
 * @see Message
 */
@FunctionalInterface
public interface Producer<T> {

    void send(Message<T> message);

}
