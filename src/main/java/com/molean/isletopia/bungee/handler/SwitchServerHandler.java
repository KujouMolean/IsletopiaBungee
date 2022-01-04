package com.molean.isletopia.bungee.handler;

import com.molean.isletopia.bungee.ThreadUtil;
import com.molean.isletopia.shared.MessageHandler;
import com.molean.isletopia.shared.message.RedisMessageListener;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.pojo.req.SwitchServerRequest;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SwitchServerHandler implements MessageHandler<SwitchServerRequest> {
    public SwitchServerHandler() {
        RedisMessageListener.setHandler("SwitchServer", this, SwitchServerRequest.class);
    }

    private static final Map<String, Long> map = new HashMap<>();

    @Override
    public void handle(WrappedMessageObject wrappedMessageObject, SwitchServerRequest message) {
        String player = message.getPlayer();
        String targetServer = message.getServer();
        PlatformRelatedUtils.getInstance().runAsync(() -> {
            for (int i = 0; i < 3; i++) {
                ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(player);
                if (proxiedPlayer == null) {
                    return;
                }
                Server server = proxiedPlayer.getServer();
                if (server == null) {
                    ThreadUtil.sleepForSecond();
                    continue;
                }
                String name = server.getInfo().getName();
                if (targetServer.equalsIgnoreCase(name)) {
                    return;
                }
                proxiedPlayer.connect(ProxyServer.getInstance().getServerInfo(targetServer));
                Logger.getGlobal().info("Switch " + player + " from " + name + " to " + targetServer);
                return;
            }
        });

    }
}
