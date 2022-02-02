package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.parameter.CustomParameter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WBan implements BotCommandExecutor {

    public static final Set<String> stringSet = new HashSet<>();

    public WBan() {
        CommandHandler.setExecutor("wban", this);
        stringSet.addAll(CustomParameter.keys("wban"));

    }

    @Override
    public String execute(long id, List<String> args) throws Exception {
        if (args.size() < 1) {
            return "用法: /wban word";
        }
        CustomParameter.set("wban", args.get(0), "true");
        stringSet.add(args.get(0));
        return args.get(0) + " 已被添加到自动封禁词列表";
    }
}
