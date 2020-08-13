package ai.care.arc.mq.consumer;


import ai.care.arc.mq.Message;

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
     * @see ai.care.arc.mq.consumer.ConsumerCluster.ConsumerListener
     */
    void handle(Message message);

}
