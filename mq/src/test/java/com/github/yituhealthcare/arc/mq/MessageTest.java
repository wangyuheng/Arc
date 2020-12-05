package com.github.yituhealthcare.arc.mq;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageTest {

    @Test
    public void should_generate_default_key_and_ts() throws InterruptedException {
        long now = System.currentTimeMillis();
        Thread.sleep(1);
        Message<String> producerRecord = new Message<>("test_topic", "test_data");
        assertEquals(36, producerRecord.getKey().length());
        assertTrue(producerRecord.getTs() > now);
    }

    @Test
    public void should_generate_current_ts() throws InterruptedException {
        long now = System.currentTimeMillis();
        Thread.sleep(1);
        Message<String> producerRecord = new Message<>("test", "test_topic", "test_data");
        assertTrue(producerRecord.getTs() > now);
    }

    @Test
    public void should_custom_key(){
        String key = "123";
        Message<String> message = new Message<>();
        message.setKey(key);
        Assert.assertEquals(key, message.getKey());
    }

    @Test
    public void should_custom_ts(){
        long ts = System.currentTimeMillis();
        Message<String> message = new Message<>();
        message.setTs(ts);
        Assert.assertEquals(ts, message.getTs().longValue());
    }

    @Test
    public void should_add_meta_without_init(){
        String metaKey = "a";
        String metaValue = "b";
        Message<String> message = new Message<>();
        message.getMeta().put(metaKey, metaValue);
        Assert.assertEquals(message.getMeta().get(metaKey), metaValue);
        message.setMeta(new HashMap<>());
        Assert.assertNull(message.getMeta().get(metaKey));
    }
}