package com.molean.isletopia.bungee;

import com.molean.crinobot.CrinoBot;
import com.molean.isletopia.bungee.cirno.CirnoHandlerImpl;
import com.molean.isletopia.bungee.cirno.CommandsRegister;
import com.molean.isletopia.bungee.handler.HandlerRegister;
import com.molean.isletopia.bungee.individual.*;
import com.molean.isletopia.shared.message.RedisMessageListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.score.Scoreboard;

public final class IsletopiaBungee extends Plugin {
    private static IsletopiaBungee isletopiaBungee;

    public static IsletopiaBungee getPlugin() {
        return isletopiaBungee;
    }

    public static void test() {
        if (ProxyServer.getInstance().getConfig().getPlayerLimit() == 5) {
        }
    }

    @Override
    public void onEnable() {
        isletopiaBungee = this;
        new CommandsRegister();
        CrinoBot.setCrinoHandler(new CirnoHandlerImpl());
        new UniversalTell();
        new WelcomeMessage();
        new UniversalChat();
        new ConnectionDetect();
        new KickUnsupportedUser();
        new OnlineModeSwitcher();
        RedisMessageListener.init();
        new HandlerRegister();
        new PlayerInfoBroadcaster();
        new IslandCommand();
        new GlobalTabList();
        new PlayerLogin();
        new PlayerRegister();
        new PlayerPassword();
        new ChatChannel();
        test();

    }

    @Override
    public void onDisable() {
        CrinoBot.setCrinoHandler(null);
        RedisMessageListener.destroy();
    }
}
