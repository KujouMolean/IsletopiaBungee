package com.molean.isletopia.bungee.cirno.command.permission;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.List;

public class PermissionCommand implements BotCommandExecutor {
    public PermissionCommand() {
        CommandHandler.setExecutor("permission", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /permission [id]";
        }
        long qq;
        try {
            qq = Long.parseLong(args.get(0));
        } catch (NumberFormatException e) {
            return args.get(0)+ "是谁啊, 琪露诺表示没见过!";
        }

        List<String> list = PermissionHandler.getPermissions(qq);

        if (list.isEmpty()) {
            return CirnoUtils.getNameCardByQQ(qq) + " 没有任何权限.";
        } else {
            return CirnoUtils.getNameCardByQQ(qq) + " 的权限如下: " + String.join(", ", list);
        }
    }

}
