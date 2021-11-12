package com.molean.isletopia.bungee.cirno.command.ban;

import com.molean.isletopia.bungee.parameter.HostNameParameter;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class BanIpCommand implements BotCommandExecutor {
    public BanIpCommand() {
        CommandHandler.setExecutor("ban-ip", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if(args.size()< 1){
            return "用法: /ban-ip 玩家ID";
        }else{
            String hostName = PlayerParameter.get(args.get(0),"hostAddress");
            if(hostName==null){
                return "这个人的老巢没见过,emm.";
            }
            HostNameParameter.set(hostName,"isBanned","true");
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if(player.getName().equalsIgnoreCase(args.get(0))){
                    player.disconnect(TextComponent.fromLegacyText("你已被封禁!"));
                }
            }

            return "琪露诺机器人已端了敌人的老巢: " + hostName+" 。";
        }
    }
}
