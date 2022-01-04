package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Locale;

public class IslandCommand implements Listener {

    public IslandCommand() {
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeRelatedUtils.getPlugin(), this);
    }

    @EventHandler
    public void on(ChatEvent event) {
        if (event.getMessage().startsWith("/")) {
            Connection sender = event.getSender();
            if (sender instanceof ProxiedPlayer player) {
                if (player.getServer() != null) {
                    if (player.getServer().getInfo().getName().toLowerCase(Locale.ROOT).startsWith("club_")) {
                        String message = event.getMessage();
                        String[] s = message.split(" ");
                        if (s.length > 0 && s[0].equalsIgnoreCase("/is")) {
                            ServerInfo dispatcher = ProxyServer.getInstance().getServerInfo("dispatcher");
                            player.connect(dispatcher);
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}