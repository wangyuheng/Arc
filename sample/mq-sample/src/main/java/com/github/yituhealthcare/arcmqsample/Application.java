package com.github.yituhealthcare.arcmqsample;

import com.github.yituhealthcare.arc.mq.Message;
import com.github.yituhealthcare.arc.mq.consumer.Consumer;
import com.github.yituhealthcare.arc.mq.producer.Producer;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class Application implements InitializingBean {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static final String TOPIC = "test";

    @Autowired
    private Producer<Integer> producer;

    private static final AtomicInteger msg = new AtomicInteger();

    @Override
    public void afterPropertiesSet() throws Exception {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message<Integer> record = new Message<>();
                record.setTopic(TOPIC);
                record.setData(msg.getAndAdd(1));
                producer.send(record);
            }
        }, 2000, 1000);
    }

    @Component
    class ConsumerListener {

        @Consumer(topic = TOPIC)
        public void consumerA(Object message) {
            log.info("consumerA handle:{}", message);
        }

        @Consumer(topic = TOPIC)
        public void consumerB(Object message) {
            log.info("consumerB handle:{}", message);
        }
    }


}

