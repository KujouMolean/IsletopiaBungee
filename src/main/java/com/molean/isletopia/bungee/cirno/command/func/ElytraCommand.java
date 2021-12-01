package com.molean.isletopia.bungee.cirno.command.func;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.shared.service.UniversalParameter;
import com.molean.isletopia.shared.utils.UUIDUtils;

import java.util.List;
import java.util.UUID;

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
        UUID uuid = UUIDUtils.get(player);
        String resp;
        if (uuid == null) {
            resp = "该玩家不存在";
        } else {
            UniversalParameter.addParameter(uuid, "elytra", player);
            UniversalParameter.setParameter(uuid, "elytraReason", reason);
            resp = player + " 获得了鞘翅权限, 原因是: " + reason;
        }
        return resp;
    }

}
