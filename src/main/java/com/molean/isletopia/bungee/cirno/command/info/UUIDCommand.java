package com.molean.isletopia.bungee.cirno.command.info;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.shared.utils.UUIDUtils;

import java.util.List;
import java.util.UUID;

public class UUIDCommand implements BotCommandExecutor {
    public UUIDCommand() {
        CommandHandler.setExecutor("uuid", this);
    }

    @Override
    public String execute(long id, List<String> args) throws Exception {
        if (args.size() < 1) {
            return "/uuid [player])";
        }
        String name = args.get(0);
        if (!name.matches("[0-9a-zA-z_#]{3,16}")) {
            return "用户名不合法";
        }
        UUID uuid = UUIDUtils.get(name);
        if (uuid == null) {
            uuid = UUIDUtils.getOnlineSync(name);

        }
        if (uuid == null) {
            return "查不到" + name + "的uuid";
        }
        return uuid.toString();
    }
}
