package com.molean.isletopia.bungee.cirno.command.ban;

import com.molean.isletopia.bungee.parameter.HostNameParameter;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;

import java.util.List;

public class PardonIpCommand implements BotCommandExecutor {
    public PardonIpCommand() {
        CommandHandler.setExecutor("pardon-ip", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if(args.size()< 1){
            return "用法: /pardon-ip 玩家ID";
        }else{
            String hostName = PlayerParameter.get(args.get(0),"hostAddress");
            if(hostName==null){
                return "这个人的老巢没见过,emm.";
            }
            HostNameParameter.set(hostName,"isBanned","false");
            return "琪露诺机器人修复了敌人的老巢: " + hostName+" 。";
        }
    }

}
