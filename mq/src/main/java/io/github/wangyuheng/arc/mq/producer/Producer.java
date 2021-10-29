package io.github.wangyuheng.arc.mq.producer;

import io.github.wangyuheng.arc.mq.Message;

/**
 * 消息发送者
 *
 * @see Message
 */
@FunctionalInterface
public interface Producer<T> {

    void send(Message<T> message);

}
