package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.IsletopiaBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnectionDetect implements Listener {
    public ConnectionDetect() {
        ProxyServer.getInstance().getPluginManager().registerListener(IsletopiaBungee.getPlugin(), this);
    }

    @EventHandler
    public void on(ServerKickEvent event) {

        //如果玩家被踢出服务器, 不会直接退出群组, 而如果踢出理由带有#, 则例外.

        StringBuilder stringBuilder = new StringBuilder();
        BaseComponent[] kickReasonComponent = event.getKickReasonComponent();
        for (BaseComponent baseComponent : kickReasonComponent) {
            stringBuilder.append(baseComponent.toLegacyText());
        }

        String reason = stringBuilder.toString();
        if (!reason.contains("#")) {
            event.getPlayer().sendMessage(event.getKickReasonComponent());
            ProxyServer.getInstance().getLogger().info("Kick for " + event.getPlayer().getName() +
                    " have been cancelled: " + reason);
            event.setCancelled(true);
        }
    }
}
