package com.mdsoft.mdscore.callback;

import com.mdsoft.mdscore.callback.commands.HelpCommand;
import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Anton Tsivarev on 19.11.16.
 */
public class MessagesHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MessagesHandler.class);

    private static Integer parseInt(String[] args, int index, int defaultValue) {
        if (args.length <= index) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(args[index]);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void parseMessage(Integer groupId, Message message) {
        Integer vkId = message.getFromId();
        String[] args = message.getText().split(" ");
        String command = args[0];

        try {
            switch (command.toLowerCase()) {
                case "help":
                    new HelpCommand(vkId).run();
                    break;
                default:
                    new HelpCommand(vkId).run();
            }
        } catch (Exception e) {
            LOG.error("Can't execute command", e);
        }
    }
}
