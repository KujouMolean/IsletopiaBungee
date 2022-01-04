package com.molean.isletopia.bungee.cirno.command.info;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class ListCommand implements BotCommandExecutor {
    public ListCommand() {
        CommandHandler.setExecutor("list", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        List<ProxiedPlayer> players = new ArrayList<>(ProxyServer.getInstance().getPlayers());
        players.removeIf(player -> !player.getPendingConnection().isOnlineMode());

        if (players.size() > 0) {
            String message = "梦幻之屿共有 " + players.size() + " 正版玩家在线。";

            Random random = new Random();
            int starNumber = random.nextInt(Math.min(players.size(), 5)) + 1;
            Set<String> set = new HashSet<>();
            for (int i = 0; i < starNumber; i++) {
                set.add(players.get(random.nextInt(players.size())).getName());
            }
            message += "其中 " + String.join(",", set) + " 是大佬!";
            return message;
        }
        return "无人在线";
    }


}
