package com.molean.isletopia.bungee.cirno.command.func;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.shared.service.UniversalParameter;
import com.molean.isletopia.shared.utils.UUIDUtils;

import java.util.List;
import java.util.UUID;

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
        String resp;
        UUID uuid = UUIDUtils.get(player);
        if (uuid == null) {
            resp = player + "该玩家不存在";
        } else {
            UniversalParameter.addParameter(uuid, "beacon", "true");
            UniversalParameter.setParameter(uuid, "beaconReason", reason);
            resp = player + " 获得了信标权限, 原因是: " + reason;
        }
        return resp;
    }

}
