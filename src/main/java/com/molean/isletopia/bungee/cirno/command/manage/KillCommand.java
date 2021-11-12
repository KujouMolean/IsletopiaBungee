package com.molean.isletopia.bungee.cirno.command.manage;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.io.IOException;
import java.util.List;

public class KillCommand implements BotCommandExecutor {
    public KillCommand() {
        CommandHandler.setExecutor("kill", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1 || !args.get(0).startsWith("server")) {
            return "/kill serverX";
        }
        ProcessHandle.allProcesses().forEach(processHandle -> {
            ProcessHandle.Info info = null;
            try {
                info = processHandle.info();
            } catch (Exception ignore) {
            }
            if (info == null) {
                return;
            }
            if (info.commandLine().isPresent()) {
                String s = info.commandLine().get();
                if (s.contains("-DServerName=" + args.get(0) + " ")) {
                    processHandle.destroyForcibly();
                }
            }
        });
        return "已尝试结束" + args.get(0) + ", 等待1分钟后重新查看状态.";
    }

}
