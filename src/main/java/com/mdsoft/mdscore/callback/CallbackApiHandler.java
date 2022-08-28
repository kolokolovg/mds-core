package com.mdsoft.mdscore.callback;

import com.vk.api.sdk.events.callback.CallbackApi;
import com.vk.api.sdk.objects.messages.Message;


public class CallbackApiHandler extends CallbackApi {
    public CallbackApiHandler(String confirmationCode) {
        super(confirmationCode);
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
        MessagesHandler.parseMessage(groupId, message);
    }
}
