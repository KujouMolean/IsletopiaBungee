package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.parameter.CustomParameter;

import java.util.List;

public class WPardon implements BotCommandExecutor {
    public WPardon() {
        CommandHandler.setExecutor("wpardon", this);
    }

    @Override
    public String execute(long id, List<String> args) throws Exception {
        if (args.size() < 1) {
            return "用法: /wban words";
        }
        CustomParameter.unset("wban", args.get(0));
        WBan.stringSet.remove(args.get(0));
        return args.get(0) + " 已从自动封禁词列表移除";
    }
}
