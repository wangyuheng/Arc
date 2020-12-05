package com.github.yituhealthcare.arc.mq.producer;

import com.github.yituhealthcare.arc.mq.Message;
import com.github.yituhealthcare.arc.mq.consumer.ConsumerCluster;
import com.github.yituhealthcare.arc.mq.consumer.Partition;
import com.github.yituhealthcare.arc.mq.store.Store;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Consumer;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultProducerTest {

    public static final String MULTI_TOPIC = "multi_topic";
    public static final String SINGLE_TOPIC = "single_topic";
    public static final String CLIENT_ID_1 = "clientId1";
    public static final String CLIENT_ID_2 = "clientId2";

    @Autowired
    private DefaultProducer<String> producer;
    @Autowired
    private Store store;

    private Message<String> multiMessage;
    private Message<String> singleMessage;

    @Before
    public void setUp() {
        multiMessage = new Message<>(MULTI_TOPIC, "This is a message for multi consumers!");
        singleMessage = new Message<>(SINGLE_TOPIC, "This is a message for single consumer!");
    }

    @Test
    public void should_group_store_by_topic_and_client_after_send() {
        Consumer<Message<String>> c = message -> {};
//        Stream.of("")
//                .forEach(it->c);
        producer.send(multiMessage);
        producer.send(singleMessage);
        producer.send(singleMessage);
        Assert.assertEquals(1, store.findByPartition(new Partition(CLIENT_ID_1, multiMessage.getTopic())).size());
        Assert.assertEquals(1, store.findByPartition(new Partition(CLIENT_ID_2, multiMessage.getTopic())).size());
        Assert.assertEquals(2, store.findByPartition(new Partition(CLIENT_ID_1, singleMessage.getTopic())).size());
    }

    @TestConfiguration
    static class NoneConsumerCluster {
        @Bean
        public ConsumerCluster consumer1() {
            return new ConsumerCluster(CLIENT_ID_1, MULTI_TOPIC);
        }

        @Bean
        public ConsumerCluster consumer2() {
            return new ConsumerCluster(CLIENT_ID_2, MULTI_TOPIC);
        }

        @Bean
        public ConsumerCluster consumer3() {
            return new ConsumerCluster(CLIENT_ID_1, SINGLE_TOPIC);
        }
    }


}