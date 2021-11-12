package com.molean.isletopia.bungee.cirno.command.ban;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class BanCommand implements BotCommandExecutor {
    public BanCommand() {
        CommandHandler.setExecutor("ban", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /ban 玩家 原因";
        } else {
            PlayerParameter.set(args.get(0), "isBanned", "true");
            String reason;
            if (args.size() >= 2) {
                ArrayList<String> strings = new ArrayList<>(args);
                strings.remove(0);
                reason = String.join(" ", strings);
                PlayerParameter.set(args.get(0), "bannedReason", reason);
                PlayerParameter.unset(args.get(0), "pardonTime");
            } else {
                reason = "";
            }
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().equalsIgnoreCase(args.get(0))) {
                    player.disconnect(TextComponent.fromLegacyText("你已被封禁!" + reason));
                }
            }
            return "琪露诺机器人已干掉坏人: " + args.get(0) + " 。";
        }
    }

}
