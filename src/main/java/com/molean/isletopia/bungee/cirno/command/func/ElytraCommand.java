package com.molean.isletopia.bungee.cirno.command.func;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.shared.pojo.req.ElytraRequestObject;
import com.molean.isletopia.shared.message.ServerMessageUtils;

import java.util.List;

public class ElytraCommand implements BotCommandExecutor {
    public ElytraCommand() {
        CommandHandler.setExecutor("elytra", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "/elytra [player] [reason]";
        }
        String player = args.get(0);
        String reason = args.get(1);
        ElytraRequestObject elytraRequestObject = new ElytraRequestObject(player, reason);
        ServerMessageUtils.sendServerBungeeMessage("server1", "ElytraRequest", elytraRequestObject);
        return null;
    }

}
