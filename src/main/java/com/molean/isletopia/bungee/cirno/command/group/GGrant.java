package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.List;

public class GGrant implements BotCommandExecutor {
    public GGrant() {
        CommandHandler.setExecutor("gGrant",this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "用法: /gGrant id permission1 permission2 ...";
        } else {
            String group = args.get(0);
            args.remove(0);
            for (String arg : args) {
                PermissionHandler.grantPermission(arg, group);
            }
            return group + " 已获得权限 " + String.join(",", args) + " 。";
        }
    }
}
