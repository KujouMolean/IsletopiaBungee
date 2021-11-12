package com.molean.isletopia.bungee.handler;

import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.shared.MessageHandler;
import com.molean.isletopia.shared.pojo.resp.CommonResponseObject;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.message.RedisMessageListener;

public class CommonResponseHandler implements MessageHandler<CommonResponseObject> {
    public CommonResponseHandler() {
        RedisMessageListener.setHandler("CommonResponse", this, CommonResponseObject.class);
    }

    @Override
    public void handle(WrappedMessageObject wrappedMessageObject,CommonResponseObject message) {
        CirnoUtils.broadcastMessage(message.getMessage());
    }
}
