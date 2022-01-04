package com.molean.isletopia.bungee.cirno.command.manage;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.pojo.req.CommandExecuteRequest;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.util.List;

public class Sudo implements BotCommandExecutor {
    public Sudo() {
        CommandHandler.setExecutor("sudo", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "/sudo [server|servers] cmd";
        }

        String serverName = args.get(0);
        StringBuilder cmd = new StringBuilder();
        for (int i = 1; i < args.size(); i++) {
            cmd.append(args.get(i)).append(" ");
        }
        CommandExecuteRequest obj = new CommandExecuteRequest(cmd.toString());
        switch (serverName) {
            case "servers" -> {
                for (String server : PlatformRelatedUtils.getInstance().getIslandServers()) {
                    if (server.startsWith("server")) {
                        ServerMessageUtils.sendMessage(server, "CommandExecuteRequest", obj);
                    }
                }
            }
            case "proxy" -> {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(ConsoleCommandSender.getInstance(), cmd.toString());
            }
            default -> ServerMessageUtils.sendMessage(serverName, "CommandExecuteRequest", obj);
        }
        return "OK";
    }
}
