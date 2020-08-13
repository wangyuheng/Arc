package ai.care.arc.mq.consumer;

import ai.care.arc.mq.Message;
import ai.care.arc.mq.producer.Producer;
import ai.care.arc.mq.transport.Transport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestConsumer {

    @Autowired
    private Producer producer;
    @Autowired
    private TestMockConsumer consumer;
    @Autowired
    private List<ConsumerCluster> consumerClusters;
    @Autowired
    private Transport transport;

    private Message<String> message;

    @Before
    public void setUp() {
        consumer.reset();
        message = new Message<>(TestMockConsumer.CONSUMER_TOPIC, "This is a message for multi consumers!");
    }

    @Test
    public void two_consumer_should_receive_topic_message_after_producer_send() throws InterruptedException {
        producer.send(message);
        int i = 100;
        while (TestMockConsumer.consumerRecordMap.values().iterator().next().size() == 0) {
            Thread.sleep(100);
            if (i-- < 0) {
                throw new AssertionError("receive msg fail!");
            }
        }
        Assert.assertEquals(2, TestMockConsumer.consumerRecordMap.size());
        TestMockConsumer.consumerRecordMap.values().forEach(it -> Assert.assertEquals(Arrays.asList(message), it));
    }

    @Test
    public void should_continue_receive_message() throws InterruptedException {
        int i = 0;
        while (i++ < 10) {
            producer.send(new Message<>(TestMockConsumer.CONSUMER_TOPIC, "This is a message" + i));
            System.out.println(TestMockConsumer.consumerRecordMap);
            Thread.sleep(200);
            Assert.assertNotEquals(0, TestMockConsumer.consumerRecordMap.size());
            Assert.assertEquals(TestMockConsumer.consumerRecordMap.values().stream().mapToLong(Collection::size).sum(), i * TestMockConsumer.consumerRecordMap.keySet().size());
        }
    }

    @Test
    public void custom_consumer_id_by_annotation() {
        Assert.assertEquals(1,
                consumerClusters.stream()
                        .filter(it -> it.getId().equals(TestMockConsumer.CUSTOM_CONSUMER_ID))
                        .count()
        );
    }
}
