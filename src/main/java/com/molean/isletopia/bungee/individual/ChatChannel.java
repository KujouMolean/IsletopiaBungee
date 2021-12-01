package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.MessageUtils;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import com.molean.isletopia.shared.service.UniversalParameter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChatChannel extends Command implements TabExecutor, Listener {

    private final static Map<UUID, String> channelMap = new HashMap<>();

    public ChatChannel() {
        super("channel", null);
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeRelatedUtils.getPlugin(), this);
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeRelatedUtils.getPlugin(), this);
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            String channel = UniversalParameter.getParameter(player.getUniqueId(), "Channel");
            if (channel == null || channel.isEmpty()) {
                channel = "白";
            }
            channelMap.put(player.getUniqueId(), channel);
        }
    }

    private static final List<String> channels = List.of("黑", "深蓝", "深绿", "湖蓝", "深红", "紫", "金",
            "灰", "深灰", "蓝", "绿", "天蓝", "红", "粉红", "黄", "白");

    public static String getChannelColor(String channel) {
        if (channels.contains(channel)) {
            return String.format("§%x", channels.indexOf(channel));
        }
        return "§f";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 0) {
            String channel = channelMap.get(player.getUniqueId());

            MessageUtils.notify(player, "你目前位于 " + channel + "频道");


            ArrayList<String> members = new ArrayList<>();
            for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
                if (channelMap.get(proxiedPlayer.getUniqueId()).equalsIgnoreCase(channel)) {
                    members.add(proxiedPlayer.getName());
                }
            }
            MessageUtils.notify(player, "该频道目前有以下玩家: " + String.join(", ", members));
            return;
        }
        if (args.length > 1) {
            MessageUtils.notify(player, "用法: /channel 频道");
            return;
        }
        String channel = args[0];
        if (!channels.contains(channel)) {
            MessageUtils.notify(player, "当前可用的频道只有: " + String.join(", ", channels));
            return;
        }
        if (channel.equalsIgnoreCase(channelMap.getOrDefault(player.getUniqueId(), "白"))) {
            MessageUtils.notify(player, "无变化，你已经在这个频道。");
            return;
        }

        UniversalParameter.setParameter(player.getUniqueId(), "Channel", channel);
        notifyQuit(channelMap.getOrDefault(player.getUniqueId(), "白"), player.getName());
        channelMap.put(player.getUniqueId(), channel);
        notifyJoin(channelMap.getOrDefault(player.getUniqueId(), "白"), player.getName());
        MessageUtils.notify(player, "你已经加入聊天频道: " + channel);
        ArrayList<String> members = new ArrayList<>();
        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
            if (channel.equalsIgnoreCase(channelMap.get(proxiedPlayer.getUniqueId()))) {
                members.add(proxiedPlayer.getName());
            }
        }
        MessageUtils.notify(player, "该频道目前有以下玩家: " + String.join(", ", members));


    }

    public static String getChannel(UUID uuid) {
        return channelMap.getOrDefault(uuid, "白");
    }

    public static Collection<UUID> getPlayersInChannel(String channel) {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (UUID uuid : channelMap.keySet()) {
            if (!channelMap.get(uuid).equalsIgnoreCase(channel)) {
                continue;
            }
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
            if (player == null) {
                continue;
            }
            Server server = player.getServer();
            if (server == null) {
                continue;
            }
            if (server.getInfo().getName().startsWith("club_")) {
                continue;
            }
            uuids.add(uuid);
        }
        return uuids;
    }

    public static void notifyJoin(@NotNull String channel, String player) {
        if (!channel.equalsIgnoreCase("白")) {
            for (UUID uuid : getPlayersInChannel(channel)) {
                ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(uuid);
                if (targetPlayer == null) {
                    continue;
                }
                MessageUtils.action(targetPlayer, "§6" + player + "加入了" + channel + "聊天频道");
            }
        }
    }

    public static void notifyQuit(@NotNull String channel, String player) {
        if (!channel.equalsIgnoreCase("白")) {
            for (UUID uuid : getPlayersInChannel(channel)) {
                ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(uuid);
                if (targetPlayer == null) {
                    continue;
                }
                MessageUtils.action(targetPlayer, "§0" + player + "离开了" + channel + "聊天频道");
            }
        }
    }

    @EventHandler
    public void on(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String channel = UniversalParameter.getParameter(player.getUniqueId(), "Channel");
        if (channel == null) {
            channel = "白";
        }
        if (!channel.equalsIgnoreCase("白")) {
            MessageUtils.notify(player, "你位于" + channel + "聊天频道, 只接受频道消息或私聊");
            MessageUtils.notify(player, "如需退出请输入 /channel 白");
        }
        channelMap.put(player.getUniqueId(), channel);
        notifyJoin(channel, player.getName());


    }

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        String channel = channelMap.getOrDefault(event.getPlayer().getUniqueId(), "白");
        ProxiedPlayer player = event.getPlayer();
        channelMap.remove(event.getPlayer().getUniqueId());
        notifyQuit(channel, player.getName());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> strings = new ArrayList<>();

        if (args.length == 1) {
            for (String channel : channels) {
                if (channel.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))) {
                    strings.add(channel);
                }
            }
        }
        return strings;
    }
}
