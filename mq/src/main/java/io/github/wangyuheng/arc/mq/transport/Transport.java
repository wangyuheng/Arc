package io.github.wangyuheng.arc.mq.transport;

import io.github.wangyuheng.arc.mq.Message;

/**
 * 运输层
 */
public interface Transport {

    void transfer(Message message);

}
