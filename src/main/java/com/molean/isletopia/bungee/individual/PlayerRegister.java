package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import com.molean.isletopia.shared.service.AccountService;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.net.InetSocketAddress;

public class PlayerRegister extends Command {

    private static boolean allowRegister = true;

    public static boolean isAllowRegister() {
        return allowRegister;
    }

    public static void setAllowRegister(boolean allowRegister) {
        PlayerRegister.allowRegister = allowRegister;
    }

    public PlayerRegister() {
        super("register", "register", "reg", "r", "注册");
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeRelatedUtils.getPlugin(),this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (!allowRegister) {
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("§c暂时禁止注册，请联系管理员"));
            PlayerLogin.failedPlayers.add(player);
            return;
        }
        if (!args[0].equalsIgnoreCase(args[1])) {
            return;
        }

        String hostAddress = ((InetSocketAddress) player.getSocketAddress()).getAddress().getHostAddress();
        if (AccountService.register(sender.getName(), hostAddress, args[0])) {
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("§aSUCCESS"));
        }else{
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("§cFAILED"));
            PlayerLogin.failedPlayers.add(player);
        }
    }
}
