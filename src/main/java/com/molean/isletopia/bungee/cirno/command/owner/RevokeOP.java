package com.molean.isletopia.bungee.cirno.command.owner;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import net.mamoe.mirai.contact.NormalMember;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RevokeOP implements BotCommandExecutor {
    public RevokeOP() {
        CommandHandler.setExecutor("revokeOP", this);
    }

    @Override
    public String execute(long id, @NotNull List<String> args) {
        if (args.size() < 1) {
            return "用法: /revokeOP id";
        }
        long qq;
        try {
            qq = Long.parseLong(args.get(0));
        } catch (NumberFormatException e) {
            return args.get(0) + "是谁啊, 琪露诺表示没见过!";
        }
        if (!CirnoUtils.getGameGroup().contains(qq)) {
            return args.get(0) + "是谁啊, 琪露诺表示没见过!";
        }
        NormalMember normalMember = CirnoUtils.getGameGroup().get(qq);
        if (normalMember == null) {
            return args.get(0) + "是谁啊, 琪露诺表示没见过!";
        }
        normalMember.modifyAdmin(false);
        return "设置成功，请检查。";
    }
}
