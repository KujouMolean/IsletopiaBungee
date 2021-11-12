package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.parameter.ContactParameter;

import java.util.*;

public class RemoveMember implements BotCommandExecutor {
    public RemoveMember() {
        CommandHandler.setExecutor("removeMember", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "用法: /removeMember id group1 group2 ...";
        } else {
            long qq;
            try {
                qq = Long.parseLong(args.get(0));
            } catch (NumberFormatException e) {
                return args.get(0) + "是谁啊, 琪露诺表示没见过!";
            }
            args.remove(0);
            String groupsString = ContactParameter.get(qq, "groups");
            if (groupsString == null) {
                groupsString = "";
            }
            Set<String> groups = new HashSet<>(Arrays.asList(groupsString.split(",")));
            groups.remove("");
            for (String arg : args) {
                groups.remove(arg.toLowerCase(Locale.ROOT));
            }
            ContactParameter.set(qq, "groups", String.join(",", groups));

            return CirnoUtils.getNameCardByQQ(qq) + " 已从以下组中删除: " + String.join(",", args) + " 。";
        }
    }
}
