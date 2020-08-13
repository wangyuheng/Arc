package ai.care.arc.mq.transport;

import ai.care.arc.mq.Message;

/**
 * 运输层
 */
public interface Transport {

    void transfer(Message message);

}
