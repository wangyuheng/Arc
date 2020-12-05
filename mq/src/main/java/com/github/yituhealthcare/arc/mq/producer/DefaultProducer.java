package com.github.yituhealthcare.arc.mq.producer;

import com.github.yituhealthcare.arc.mq.Message;
import com.github.yituhealthcare.arc.mq.transport.Transport;

/**
 * 默认消息发送者
 *
 * @see Transport
 * @see Message
 */
public class DefaultProducer<T> implements Producer<T> {

    private final Transport transport;

    public DefaultProducer(Transport transport) {
        this.transport = transport;
    }

    /**
     * 发送消息
     */
    @Override
    public void send(Message<T> message) {
        transport.transfer(message);
    }
}
