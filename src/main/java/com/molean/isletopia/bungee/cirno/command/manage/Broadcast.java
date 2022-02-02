package com.molean.isletopia.bungee.cirno.command.manage;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class Broadcast implements BotCommandExecutor {
    public Broadcast() {
        CommandHandler.setExecutor("broadcast", this);
    }

    @Override
    public String execute(long id, List<String> args) throws Exception {
        if (args.size() < 1) {
            return "/broadcast xxx";
        }
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage("§8[§b公告§8] §e" + String.join(" ", args));
        }
        return "OK";
    }
}
