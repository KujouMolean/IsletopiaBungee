package com.molean.isletopia.bungee.cirno.command.permission;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.List;

public class RevokeCommand implements BotCommandExecutor {
    public RevokeCommand() {
        CommandHandler.setExecutor("revoke", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "用法: /revoke [id] [permission]";
        } else {
            long qq;
            try {
                qq = Long.parseLong(args.get(0));
            } catch (NumberFormatException e) {
                return args.get(0)+ "是谁啊, 琪露诺表示没见过!";
            }

            if(PermissionHandler.hasPermission(args.get(1),qq)){
                PermissionHandler.removePermission(args.get(1), Long.parseLong(args.get(0)));
                return "琪露诺已经撤掉 "+ CirnoUtils.getNameCardByQQ(qq) + " 的权限 " + args.get(1) + " 。";
            }else{
                return "琪露诺表示 "+ CirnoUtils.getNameCardByQQ(qq) + " 根本没有权限 " + args.get(1) + " 。";
            }
        }
    }

}
