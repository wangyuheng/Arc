package com.github.yituhealthcare.arc.mq.consumer;


import com.github.yituhealthcare.arc.mq.Message;

/**
 * 消息处理者
 *
 * @see ConsumerBeanDefinitionRegistryPostProcessor
 */
@FunctionalInterface
public interface MessageHandler {

    /**
     * 消息处理方法
     *
     * @see com.github.yituhealthcare.arc.mq.consumer.ConsumerCluster.ConsumerListener
     */
    void handle(Message message);

}
