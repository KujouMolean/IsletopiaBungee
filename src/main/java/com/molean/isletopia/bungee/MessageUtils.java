package com.molean.isletopia.bungee;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageUtils {
    public static void warn(CommandSender player, String message) {
        player.sendMessage(TextComponent.fromLegacyText("§8[§c危险警告§8] §e" + message));
    }

    public static void info(CommandSender player, String message) {
        player.sendMessage(TextComponent.fromLegacyText("§8[§3岛屿助手§8] §7" + message));
    }

    public static void notify(CommandSender player, String message) {
        player.sendMessage(TextComponent.fromLegacyText("§8[§3温馨提示§8] §e" + message));
    }

    public static void strong(CommandSender player, String message) {
        player.sendMessage(TextComponent.fromLegacyText("§8[§3温馨提示§8] §e§l" + message));
    }

    public static void fail(CommandSender player, String message) {
        player.sendMessage(TextComponent.fromLegacyText("§8[§3岛屿助手§8] §c" + message));
    }

    public static void success(CommandSender player, String message) {
        player.sendMessage(TextComponent.fromLegacyText("§8[§3岛屿助手§8] §6" + message));
    }

    public static void action(ProxiedPlayer player,String message) {
        player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6"+message));
    }

    public static void custom(CommandSender player, String prefix, String message) {
        player.sendMessage(TextComponent.fromLegacyText("§8[§3" + prefix + "§8] §c" + message));
    }
}
