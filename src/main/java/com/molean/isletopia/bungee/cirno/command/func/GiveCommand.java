package com.molean.isletopia.bungee.cirno.command.func;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.shared.pojo.req.GiveItemRequestObject;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand implements BotCommandExecutor {
    public GiveCommand() {
        CommandHandler.setExecutor("give", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if(args.size()<2){
            return "/give <player> <material> [amount] [name] [lore1] [lore2] ...";
        }

        String player = args.get(0);

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(player);
        if (!player.equals("*") && proxiedPlayer == null) {
            return "玩家不在线。";
        }

        String material = args.get(1);
        int amount;
        String name = null;
        List<String> lores = new ArrayList<>();
        if(args.size()==2){
            amount = 1;
        }else{
            try {
                amount = Integer.parseInt(args.get(2));
            } catch (NumberFormatException e) {
                return "你会不会数数! " + args.get(2) + " 根本不是数字。";
            }
        }

        if(args.size()>=4){
            name = args.get(3);
        }

        if(args.size()>=5){
            args.remove(0);
            args.remove(0);
            args.remove(0);
            args.remove(0);
            lores.addAll(args);
        }

        GiveItemRequestObject giveItemObject = new GiveItemRequestObject();
        giveItemObject.setPlayer(player);
        giveItemObject.setMaterial(material);
        giveItemObject.setAmount(amount);
        giveItemObject.setName(name);
        giveItemObject.setLores(lores);

        if (player.equals("*")) {
            for (ProxiedPlayer thePlayer : ProxyServer.getInstance().getPlayers()) {
                giveItemObject.setPlayer("#" + thePlayer.getName());
                ServerMessageUtils.broadcastBungeeMessage("GiveItemRequest", giveItemObject);
            }
        }else{
            ServerMessageUtils.broadcastBungeeMessage("GiveItemRequest", giveItemObject);
        }


        return null;
    }


}
