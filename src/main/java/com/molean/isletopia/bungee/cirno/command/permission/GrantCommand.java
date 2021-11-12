package com.molean.isletopia.bungee.cirno.command.permission;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.List;

public class GrantCommand implements BotCommandExecutor {
    public GrantCommand() {
        CommandHandler.setExecutor("grant", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "用法: /grant id permission1 permission2 ...";
        } else {
            long qq;
            try {
                qq = Long.parseLong(args.get(0));
            } catch (NumberFormatException e) {
                return args.get(0)+ "是谁啊, 琪露诺表示没见过!";
            }
            args.remove(0);
            for (String arg : args) {
                PermissionHandler.grantPermission(arg, qq);
            }
            return CirnoUtils.getNameCardByQQ(qq) + " 已获得权限 " + String.join(",", args) + " 。";
        }
    }

}
