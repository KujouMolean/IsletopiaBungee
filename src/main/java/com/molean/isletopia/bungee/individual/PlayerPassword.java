package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.shared.database.AccountDao;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import com.molean.isletopia.shared.service.AccountService;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Objects;

public class PlayerPassword extends Command{
    public PlayerPassword() {
        super("password","password","ChangePassword","pw","更改密码");
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeRelatedUtils.getPlugin(),this);


    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length < 2 || !Objects.equals(args[0], args[1])) {
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("更改密码: /password 密码 确认密码"));
            return;
        }
        if (AccountService.changePassword(player.getName(), args[0])) {
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("§aSUCCESS"));
        }else{
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("§cFAILED"));
        }

    }
}
