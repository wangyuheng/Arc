package com.github.yituhealthcare.arc.mq.transport;

import com.github.yituhealthcare.arc.mq.Message;

/**
 * 运输层
 */
public interface Transport {

    void transfer(Message message);

}
