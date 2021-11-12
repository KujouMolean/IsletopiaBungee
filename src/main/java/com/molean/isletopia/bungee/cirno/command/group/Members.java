package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.bungee.parameter.ContactParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Members implements BotCommandExecutor {
    public Members() {
        CommandHandler.setExecutor("members",this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /members group";
        } else {
            String group = args.get(0);
            ArrayList<String> strings = new ArrayList<>();

            for (Long target : ContactParameter.targets()) {
                String groups = ContactParameter.get(target, "groups");
                if (groups == null) {
                    groups = "";
                }
                if (Arrays.asList(groups.split(",")).contains(group)) {
                    strings.add(CirnoUtils.getNameCardByQQ(target));
                }
            }
            if (!strings.isEmpty()) {

                return "成员：" + String.join(",", strings);
            }else{
                return "无成员";
            }
        }
    }
}
