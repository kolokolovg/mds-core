package com.mdsoft.mdscore.callback.commands;

import com.mdsoft.mdscore.MdsCoreApplication;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.Random;

/**
 * Created by tsivarev on 07.09.16.
 */
public abstract class VkCommand {

    private Integer vkId;

    public VkCommand(Integer vkId) {
        this.vkId = vkId;
    }

    public abstract void run() throws Exception;

    public Integer getVkId() {
        return vkId;
    }

    public void sendMessage(String msg) throws ClientException, ApiException {
        MdsCoreApplication.vk().messages().send(MdsCoreApplication.actor())
                .randomId(new Random().nextInt(10000))
                .message(msg)
                .peerId(getVkId()).execute();
    }
}
