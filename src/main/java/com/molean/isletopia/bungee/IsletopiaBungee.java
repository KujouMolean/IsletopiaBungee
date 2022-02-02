package com.molean.isletopia.bungee;

import com.molean.cirnobot.CirnoBot;
import com.molean.cirnobot.Robot;
import com.molean.isletopia.bungee.cirno.CirnoHandlerImpl;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.CommandsRegister;
import com.molean.isletopia.bungee.cirno.ListenerRegister;
import com.molean.isletopia.bungee.handler.HandlerRegister;
import com.molean.isletopia.bungee.individual.*;
import com.molean.isletopia.shared.message.RedisMessageListener;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import net.mamoe.mirai.message.data.MessageSource;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.protocol.PacketWrapper;

public final class IsletopiaBungee extends Plugin {
    private static IsletopiaBungee isletopiaBungee;

    public static IsletopiaBungee getPlugin() {
        return isletopiaBungee;
    }

    public void init() {
        EventChannel<BotEvent> eventChannel = Robot.getBot().getEventChannel();

    }


    @Override
    public void onEnable() {
        isletopiaBungee = this;
        new CommandsRegister();
        new ListenerRegister();

//        if (ProxyServer.getInstance().getConfig().getPlayerLimit() > 5)
            CirnoBot.setCirnoHandler(new CirnoHandlerImpl());


        new UniversalTell();
        new WelcomeMessage();
        new UniversalChat();
        new ConnectionDetect();
        new KickUnsupportedUser();
        new OnlineModeSwitcher();

        RedisMessageListener.init();

        new HandlerRegister();
        new PlayerInfoBroadcaster();
        new GlobalTabList();
        new ChatChannel();
        new PlayerLogin();
        new IslandCommand();
    }

    @Override
    public void onDisable() {
        CirnoBot.setCirnoHandler(null);
        CirnoUtils.clearListener();
        RedisMessageListener.destroy();;
    }


}
