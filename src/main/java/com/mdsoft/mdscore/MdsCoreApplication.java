package com.mdsoft.mdscore;


import com.mdsoft.mdscore.server.CallbackRequestHandler;
import com.mdsoft.mdscore.server.ConfirmationCodeRequestHandler;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.CallbackServer;
import com.vk.api.sdk.objects.groups.responses.AddCallbackServerResponse;
import com.vk.api.sdk.objects.groups.responses.GetCallbackConfirmationCodeResponse;
import com.vk.api.sdk.objects.groups.responses.GetCallbackServersResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class MdsCoreApplication {
    private static final Logger LOG = LoggerFactory.getLogger(MdsCoreApplication.class);
    private static GroupActor actor = null;
    private static String version = "1.0";
    private static String mode = "standalone";
    private static Integer groupId = null;
    private static VkApiClient vk;
    public static void main(String[] args) throws Exception {
        init();
    }

    private static void init() throws Exception {
        Properties properties = loadConfiguration();

        version = properties.getProperty("version");
        mode = properties.getProperty("mode");
        groupId = Integer.valueOf(properties.getProperty("vk.group.id"));
        initClients(properties);
        initServer(properties);
    }
    private static Properties loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream is = MdsCoreApplication.class.getResourceAsStream("/config.properties")) {
            properties.load(is);
        } catch (IOException e) {
            LOG.error("Can't load properties file", e);
            throw new IllegalStateException(e);
        }

        return properties;
    }
    private static void initClients(Properties properties) throws IOException {
        TransportClient client = HttpTransportClient.getInstance();
        vk = new VkApiClient(client);
        actor = new GroupActor(Integer.parseInt(properties.getProperty("vk.group.id")), properties.getProperty("vk.group.token"));
    }

    private static void initServer(Properties properties) throws Exception {
        String host = properties.getProperty("server.host");
        int port = Integer.parseInt(properties.getProperty("server.port"));
        HandlerCollection handlers = new HandlerCollection();

        ConfirmationCodeRequestHandler confirmationCodeRequestHandler = null;

        /*GetCallbackServersResponse getCallbackServersResponse =
                vk.groups().getCallbackServers(actor, actor.getGroupId()).execute();
        CallbackServer callbackServer = isServerExist(getCallbackServersResponse.getItems(), host);

        if (callbackServer == null) {
            GetCallbackConfirmationCodeResponse getCallbackConfirmationCodeResponse =
                    vk.groups().getCallbackConfirmationCode(actor, actor.getGroupId()).execute();
            String confirmationCode = getCallbackConfirmationCodeResponse.getCode();
            confirmationCodeRequestHandler = new ConfirmationCodeRequestHandler(confirmationCode);
        }
*/
        CallbackRequestHandler callbackRequestHandler = new CallbackRequestHandler("ff9a2256");
        confirmationCodeRequestHandler = new ConfirmationCodeRequestHandler("ff9a2256");
        //if (callbackServer == null) {
            handlers.setHandlers(new Handler[]{confirmationCodeRequestHandler, callbackRequestHandler});
        //} else {
       //     handlers.setHandlers(new Handler[]{callbackRequestHandler}); //temp solution
       // }

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();

        /*if (callbackServer == null) {
            AddCallbackServerResponse addServerResponse =
                    vk.groups().addCallbackServer(actor, actor.getGroupId(), host, "bot").execute();
            Integer serverId = addServerResponse.getServerId();
            vk.groups().setCallbackSettings(actor, serverId).messageNew(true).execute();
        }*/

        server.join();
    }

    private static CallbackServer isServerExist(List<CallbackServer> items, String host) {
        for (CallbackServer callbackServer : items) {
            if (callbackServer.getUrl().equals(host)) {
                return callbackServer;
            }
        }
        return null;
    }
    public static VkApiClient vk() {
        return vk;
    }
    public static GroupActor actor() {
        return actor;
    }

    public static String getVersion() {
        return version;
    }

    public static boolean isServerMode() {
        return mode.equalsIgnoreCase("server");
    }

    public static Integer groupId() {
        return groupId;
    }
}
