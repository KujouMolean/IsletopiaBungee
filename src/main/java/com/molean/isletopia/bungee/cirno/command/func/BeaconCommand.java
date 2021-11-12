package com.molean.isletopia.bungee.cirno.command.func;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.shared.pojo.req.BeaconRequestObject;
import com.molean.isletopia.shared.message.ServerMessageUtils;

import java.util.List;

public class BeaconCommand implements BotCommandExecutor {
    public BeaconCommand() {
        CommandHandler.setExecutor("beacon", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "/beacon [player] [reason]";
        }
        String player = args.get(0);
        String reason = args.get(1);
        BeaconRequestObject beaconRequestObject = new BeaconRequestObject(player, reason);
        ServerMessageUtils.sendServerBungeeMessage("server1", "BeaconRequest", beaconRequestObject);
        return null;
    }

}
