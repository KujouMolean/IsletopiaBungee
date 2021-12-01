package com.molean.isletopia.bungee.cirno.command.manage;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.shared.utils.RedisUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusCommand implements BotCommandExecutor {
    public StatusCommand() {
        CommandHandler.setExecutor("status", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        Map<String, ServerInfo> serversCopy = ProxyServer.getInstance().getServers();
        Map<String, Long> lastUpdate = new HashMap<>();
        for (String s : serversCopy.keySet()) {
            if (s.startsWith("server")) {
                long l = Long.parseLong(RedisUtils.getCommand().get("ServerStatus:LastUpdate:" + s));
                lastUpdate.put(s, l);
            }
        }
        boolean bad = false;
        long l = System.currentTimeMillis();
        StringBuilder message = new StringBuilder("部分服务器异常:");
        for (String s : lastUpdate.keySet()) {
            long diff = l - lastUpdate.get(s);
            if (diff > 10 * 1000) {
                bad = true;
                message.append("\n").append(s).append("已经").append(diff / 1000).append("秒未响应");
            }
        }
        if (bad) {
            return message.toString();
        }else{
            return "服务器一切正常";
        }
    }

}
