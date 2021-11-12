package com.molean.isletopia.bungee.cirno.command.info;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.bungee.parameter.PlayerParameter;

import java.util.List;

public class HostNameCommand implements BotCommandExecutor {
    public HostNameCommand() {
        CommandHandler.setExecutor("hostname", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /hostname 玩家ID";
        }
        String hostAddress = PlayerParameter.get(args.get(0), "hostAddress");
        if (hostAddress == null || hostAddress.isEmpty()) {
            return "找不到 " + args.get(0) + " 的地址";
        } else {
            return hostAddress;
        }
    }
}
