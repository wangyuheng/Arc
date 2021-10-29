package io.github.wangyuheng.arc.mq.consumer;


import io.github.wangyuheng.arc.mq.Message;

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
     * @see io.github.wangyuheng.arc.mq.consumer.ConsumerCluster.ConsumerListener
     */
    void handle(Message message);

}
