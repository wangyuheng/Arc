package io.github.wangyuheng.arc.mq.consumer;

import io.github.wangyuheng.arc.mq.Message;
import io.github.wangyuheng.arc.mq.store.Store;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消费cluster
 */
public class ConsumerCluster implements Serializable {

    private String id;
    private String topic;
    private transient MessageHandler messageHandler;
    private AtomicBoolean liveToggle = new AtomicBoolean(true);
    private AtomicBoolean runToggle = new AtomicBoolean(true);
    private AtomicBoolean initialized = new AtomicBoolean(false);

    public ConsumerCluster() {
    }

    public ConsumerCluster(String id, String topic) {
        this.id = id;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public Partition generatePartition() {
        return new Partition(id, topic);
    }

    /**
     * 开启消费线程
     * 只能开启一次
     */
    public synchronized void start(Store store) {
        if (!initialized.get()) {
            synchronized (this) {
                SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
                taskExecutor.setDaemon(true);
                taskExecutor.execute(new ConsumerListener(this.getMessageHandler(), store.findByPartition(this.generatePartition())));
                initialized.set(true);
            }
        }
    }

    /**
     * 关闭消费线程
     */
    public void shutdown() {
        liveToggle.set(false);
    }

    /**
     * 暂停消费
     */
    public void pause() {
        runToggle.set(false);
    }

    /**
     * 重启暂停的消费线程
     */
    public void restart() {
        runToggle.set(true);
    }

    class ConsumerListener implements Runnable {

        private MessageHandler handler;
        private LinkedBlockingQueue<Message> queue;

        ConsumerListener(MessageHandler handler, LinkedBlockingQueue<Message> queue) {
            this.handler = handler;
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (!liveToggle.get()) {
                        break;
                    }
                    if (runToggle.get()) {
                        Message message = queue.poll();
                        if (null == message) {
                            Thread.sleep(100);
                        } else {
                            try {
                                handler.handle(message);
                            } catch (Exception e) {
                                //TODO retry | save?
                            }
                        }
                    } else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
