package ai.care.arc.mq.consumer;

import ai.care.arc.mq.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestMockConsumer {

    public static final String CONSUMER_TOPIC = "consumer.topic";
    public static final String CUSTOM_CONSUMER_ID = "consumer2";

    protected static final Map<String, List<Message>> consumerRecordMap = new HashMap<>(16);

    @PostConstruct
    public void initMap() {
        consumerRecordMap.put("consumer1", new ArrayList<>());
        consumerRecordMap.put(CUSTOM_CONSUMER_ID, new ArrayList<>());
    }

    public void reset() {
        initMap();
    }

    @Consumer(topic = CONSUMER_TOPIC)
    public void consumer1(Message message) {
        consumerRecordMap.get("consumer1").add(message);
    }

    @Consumer(topic = CONSUMER_TOPIC, id = CUSTOM_CONSUMER_ID)
    public void consumer2(Message message) {
        consumerRecordMap.get(CUSTOM_CONSUMER_ID).add(message);
    }


}
