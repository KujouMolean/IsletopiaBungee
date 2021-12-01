package com.molean.isletopia.bungee.cirno.command.info;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class IsOnlineCommand implements BotCommandExecutor {
    public IsOnlineCommand() {
        CommandHandler.setExecutor("isonline", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /isonline [player]";
        }
        String name = args.get(0);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
        if (player == null) {
            return name + " 不在线";
        }
        return name + " 当前在线";
    }
}
