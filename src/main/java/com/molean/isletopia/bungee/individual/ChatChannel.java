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

    private static final List<String> availableChannels = List.of("黑", "深蓝", "深绿", "湖蓝", "深红", "紫", "金", "灰", "深灰", "蓝", "绿", "天蓝", "红", "粉红", "黄", "白");

    public static String getChannelColor(String channel) {
        if (availableChannels.contains(channel)) {
            return String.format("§%x", availableChannels.indexOf(channel));
        }
        return "§f";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 0) {
            String channels = channelMap.get(player.getUniqueId());

            MessageUtils.notify(player, "你目前位于 " + channels + "频道");


            for (String channel : channels.split(",")) {
                ArrayList<String> members = new ArrayList<>();
                for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
                    if (Arrays.asList(channelMap.get(proxiedPlayer.getUniqueId()).split(",")).contains(channel)) {
                        members.add(proxiedPlayer.getName());
                    }
                }
                MessageUtils.notify(player, channel + "频道目前有以下玩家: " + String.join(", ", members));
            }
            return;
        }


        StringBuilder channels = new StringBuilder();
        for (String arg : Arrays.stream(args).distinct().toList()) {
            channels.append(arg).append(",");
            if (!availableChannels.toString().contains(arg)) {
                MessageUtils.notify(player, "当前可用的频道只有: " + String.join(", ", channels.toString()));
                return;
            }
        }
        channels.deleteCharAt(channels.length() - 1);

        UniversalParameter.setParameter(player.getUniqueId(), "Channel", channels.toString());
        notifyQuit(channelMap.getOrDefault(player.getUniqueId(), "白"), player.getName());
        channelMap.put(player.getUniqueId(), channels.toString());
        notifyJoin(channelMap.getOrDefault(player.getUniqueId(), "白"), player.getName());
        MessageUtils.notify(player, "你已经加入聊天频道: " + channels.toString());
        for (String channel : channels.toString().split(",")) {
            ArrayList<String> members = new ArrayList<>();
            for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
                if (Arrays.asList(channelMap.get(proxiedPlayer.getUniqueId()).split(",")).contains(channel)) {
                    members.add(proxiedPlayer.getName());
                }
            }
            MessageUtils.notify(player, channel + "频道目前有以下玩家: " + String.join(", ", members));
        }
    }

    public static String getChannels(UUID uuid) {
        return channelMap.getOrDefault(uuid, "白");
    }

    public static Collection<UUID> getPlayersInChannel(String channel) {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (UUID uuid : channelMap.keySet()) {
            if (!Arrays.asList(channelMap.get(uuid).split(",")).contains(channel)) {
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

    public static void notifyJoin(@NotNull String channels, String player) {
        for (String channel : channels.split(",")) {
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

    }

    public static void notifyQuit(@NotNull String channels, String player) {
        for (String channel : channels.split(",")) {
            if (!channel.equalsIgnoreCase("白")) {
                for (UUID uuid : getPlayersInChannel(channels)) {
                    ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(uuid);
                    if (targetPlayer == null) {
                        continue;
                    }
                    MessageUtils.action(targetPlayer, "§0" + player + "离开了" + channel + "聊天频道");
                }
            }
        }
    }

    @EventHandler
    public void on(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String channels = UniversalParameter.getParameter(player.getUniqueId(), "Channel");
        if (channels == null) {
            channels = "白";
        }
        if (!channels.equalsIgnoreCase("白")) {
            MessageUtils.notify(player, "你位于" + channels + "聊天频道, 只接受频道消息或私聊");
            MessageUtils.notify(player, "如需回到默认频道请输入 /channel 白");
        }
        channelMap.put(player.getUniqueId(), channels);
        notifyJoin(channels, player.getName());


    }

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        String channels = channelMap.getOrDefault(event.getPlayer().getUniqueId(), "白");
        ProxiedPlayer player = event.getPlayer();
        channelMap.remove(event.getPlayer().getUniqueId());
        notifyQuit(channels, player.getName());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        for (String channel : availableChannels) {
            if (channel.toLowerCase(Locale.ROOT).startsWith(args[args.length - 1].toLowerCase(Locale.ROOT))) {
                strings.add(channel);
            }
        }
        return strings;
    }
}
