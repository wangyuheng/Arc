package com.github.yituhealthcare.arc.mq.transport;

import com.github.yituhealthcare.arc.mq.Message;
import com.github.yituhealthcare.arc.mq.consumer.ConsumerCluster;
import com.github.yituhealthcare.arc.mq.consumer.Partition;
import com.github.yituhealthcare.arc.mq.store.Store;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 运输层
 * 初始化各分区通道
 *
 * @see Store
 * @see ConsumerCluster
 * @see Partition
 */
public class VmTransport implements Transport, ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    private Store store;

    public VmTransport(Store store) {
        this.store = store;
    }

    private Map<String, Set<String>> topicClientIdMap = new ConcurrentHashMap<>();

    @Override
    public void transfer(Message message) {
        if (topicClientIdMap.isEmpty()) {
            this.init();
        }
        final String topic = message.getTopic();
        if (topicClientIdMap.containsKey(topic)) {
            topicClientIdMap.get(topic).forEach(clientId -> {
                Partition partition = new Partition(clientId, topic);
                store.append(message, partition);
            });
        }
    }

    private void init() {
        topicClientIdMap.putAll(
                applicationContext.getBeansOfType(ConsumerCluster.class)
                        .values()
                        .stream()
                        .collect(Collectors.groupingBy(ConsumerCluster::getTopic, Collectors.mapping(ConsumerCluster::getId, Collectors.toSet())))
        );
    }

}