package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class UniversalTell extends Command {

    public UniversalTell() {
        super("tell", null, "w", "msg");
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeRelatedUtils.getPlugin(), this);

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //阻止登陆服玩家使用

        if (sender instanceof ProxiedPlayer player) {
            Server server = player.getServer();
            if (server == null || server.getInfo().getName().equals("login") || player.getName().startsWith("#")) {
                player.sendMessage(TextComponent.fromLegacyText("§c你当前不能使用此指令"));
                return;
            }
        }

        //


        if (args.length < 2) {
            sender.sendMessage(TextComponent.fromLegacyText("§c用法: /tell [玩家] [消息..]"));
            return;
        }

        //

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextComponent.fromLegacyText("§c玩家不在线"));
            return;
        }

        //

        ArrayList<String> strings = new ArrayList<>(Arrays.asList(args));
        strings.remove(0);
        String message = String.join(" ", strings);
        message = message.replaceAll("\\$", "￥");
        String name = sender.getName();
        String finalMessage = "§7" + name + " §7-> " + player.getName() + "§7: " + message;
        player.sendMessage(TextComponent.fromLegacyText(finalMessage));
        sender.sendMessage(TextComponent.fromLegacyText(finalMessage));
    }
}
