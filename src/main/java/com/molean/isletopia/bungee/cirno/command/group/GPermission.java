package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.List;

public class GPermission implements BotCommandExecutor {
    public GPermission() {
        CommandHandler.setExecutor("gPermission",this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /gPermission group";
        }
        String group = args.get(0);
        List<String> list = PermissionHandler.getPermissions(group);

        if (list.isEmpty()) {
            return group + " 没有任何权限.";
        } else {
            return group + " 的权限如下: " + String.join(", ", list);
        }
    }
}
