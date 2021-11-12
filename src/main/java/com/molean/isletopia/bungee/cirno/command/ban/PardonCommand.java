package com.molean.isletopia.bungee.cirno.command.ban;

import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.List;

public class PardonCommand implements BotCommandExecutor {
    public PardonCommand() {
        CommandHandler.setExecutor("pardon", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /pardon 玩家ID";
        } else {
            PlayerParameter.set(args.get(0), "isBanned", "false");
            return "琪露诺机器人已原谅坏人: " + args.get(0) + " 。";
        }
    }

}
