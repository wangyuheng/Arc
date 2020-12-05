package com.github.yituhealthcare.arc.mq.producer;

import com.github.yituhealthcare.arc.mq.Message;

/**
 * 消息发送者
 *
 * @see Message
 */
@FunctionalInterface
public interface Producer<T> {

    void send(Message<T> message);

}
