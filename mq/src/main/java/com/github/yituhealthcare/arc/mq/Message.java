package com.github.yituhealthcare.arc.mq;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 消息实体
 */
public class Message<T> implements Serializable {

    /**
     * 消息唯一标识
     */
    private String key = UUID.randomUUID().toString();
    /**
     * 订阅主题
     */
    private String topic;
    /**
     * 消息元信息
     */
    private Map<String, Object> meta = new HashMap<>();
    /**
     * 消息内容
     */
    private T data;
    /**
     * 时间戳
     */
    private Long ts = System.currentTimeMillis();

    public Message() {
    }

    public Message(String topic, T data) {
        this.topic = topic;
        this.data = data;
    }

    public Message(String key, String topic, T data) {
        this.key = key;
        this.topic = topic;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "Message{" +
                "key='" + key + '\'' +
                ", topic='" + topic + '\'' +
                ", meta=" + meta +
                ", data=" + data +
                ", ts=" + ts +
                '}';
    }
}
