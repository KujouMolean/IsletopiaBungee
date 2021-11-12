package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.ArrayList;
import java.util.List;

public class GRevoke implements BotCommandExecutor {
    public GRevoke() {
        CommandHandler.setExecutor("gRevoke", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "用法: /gRevoke [group] [permission..]";
        } else {
            String group = args.get(0);
            args.remove(0);

            ArrayList<String> removed = new ArrayList<>();
            ArrayList<String> ignored = new ArrayList<>();

            for (String arg : args) {
                if (PermissionHandler.hasPermission(arg, group)) {
                    PermissionHandler.removePermission(arg, group);
                    removed.add(arg);
                } else {
                    ignored.add(arg);
                }
            }

            if (removed.isEmpty()) {
                return "这个小组没有这样的权限。";
            } else {
                if (ignored.isEmpty()) {
                    return "已全部完成。";
                } else {
                    return "移除了：" + String.join(",", removed) + "。" +
                            "但该组并没有这些权限：" + String.join(",", ignored)+"。";
                }

            }
        }
    }
}
