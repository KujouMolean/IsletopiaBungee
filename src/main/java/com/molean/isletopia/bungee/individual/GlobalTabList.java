package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.ReflectionUtil;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import com.molean.isletopia.shared.utils.RedisUtils;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class GlobalTabList implements Listener {

    private static final Map<ProxiedPlayer, IsletopiaTabList> normalTabLists = new ConcurrentHashMap<>();
    private static final Map<String, Map<ProxiedPlayer, IsletopiaTabList>> clubTabList = new ConcurrentHashMap<>();

    public static void onPlayerJoinProxy(ProxiedPlayer player) {
        IsletopiaTabList tabListHandler = new IsletopiaTabList(player);
        normalTabLists.put(player, tabListHandler);
        try {
            ReflectionUtil.setTablistHandler(player, tabListHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
            tabListHandler.addPlayer(proxiedPlayer);
        }
        for (IsletopiaTabList value : normalTabLists.values()) {
            value.addPlayer(player);
        }
    }

    public static void onPlayerConnectClub(ProxiedPlayer player, ServerInfo serverInfo) {
        IsletopiaTabList isletopiaTabList = normalTabLists.get(player);
        normalTabLists.remove(player);

        if (!clubTabList.containsKey(serverInfo.getName())) {
            clubTabList.put(serverInfo.getName(), new ConcurrentHashMap<>());
        }

        Map<ProxiedPlayer, IsletopiaTabList> tabListMap = clubTabList.get(serverInfo.getName());

        tabListMap.put(player, isletopiaTabList);
        for (ProxiedPlayer innerPlayer : ProxyServer.getInstance().getPlayers()) {
            isletopiaTabList.removePlayer(innerPlayer);
        }
        for (ProxiedPlayer innerPlayer : serverInfo.getPlayers()) {
            isletopiaTabList.addPlayer(innerPlayer);
        }
        for (IsletopiaTabList value : tabListMap.values()) {
            value.addPlayer(player);
        }
        isletopiaTabList.addPlayer(player);
    }

    public static void onPlayerDisconnectClub(ProxiedPlayer player, ServerInfo serverInfo) {
        IsletopiaTabList isletopiaTabList = clubTabList.get(serverInfo.getName()).get(player);
        clubTabList.get(serverInfo.getName()).remove(player);
        normalTabLists.put(player, isletopiaTabList);
        for (ProxiedPlayer innerPlayer : serverInfo.getPlayers()) {
            isletopiaTabList.removePlayer(innerPlayer);
        }
        for (ProxiedPlayer innerPlayer : ProxyServer.getInstance().getPlayers()) {
            isletopiaTabList.addPlayer(innerPlayer);
        }
        for (IsletopiaTabList value : clubTabList.get(serverInfo.getName()).values()) {
            value.removePlayer(player);
        }
        isletopiaTabList.addPlayer(player);
    }

    public static void onPlayerQuitProxy(ProxiedPlayer player) {
        normalTabLists.remove(player);
        for (Map<ProxiedPlayer, IsletopiaTabList> value : clubTabList.values()) {
            for (IsletopiaTabList isletopiaTabList : value.values()) {
                isletopiaTabList.removePlayer(player);
            }
            value.remove(player);
        }
        for (IsletopiaTabList value : normalTabLists.values()) {
            value.removePlayer(player);
        }
    }

    public GlobalTabList() {
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeRelatedUtils.getPlugin(), this);
        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
            onPlayerJoinProxy(proxiedPlayer);
            Server server = proxiedPlayer.getServer();
            if (server != null && server.getInfo().getName().startsWith("club_")) {
                onPlayerConnectClub(proxiedPlayer, server.getInfo());
            }
        }
        ProxyServer.getInstance().getScheduler().schedule(BungeeRelatedUtils.getPlugin(), () -> {
            for (ProxiedPlayer proxiedPlayer : normalTabLists.keySet()) {
                for (ProxiedPlayer innerPlayer : ProxyServer.getInstance().getPlayers()) {
                    normalTabLists.get(proxiedPlayer).setPing(innerPlayer, innerPlayer.getPing());
                    String s = RedisUtils.getCommand().get(innerPlayer.getName() + ":GameMode");
                    if (s == null || s.isEmpty()) {
                        s = "0";
                    }
                    int i = Integer.parseInt(s);
                    normalTabLists.get(proxiedPlayer).setGameMode(innerPlayer, i);
                }
            }
            for (String s : clubTabList.keySet()) {
                for (ProxiedPlayer proxiedPlayer : clubTabList.get(s).keySet()) {
                    for (ProxiedPlayer innerPlayer : clubTabList.get(s).keySet()) {
                        clubTabList.get(s).get(proxiedPlayer).setPing(innerPlayer, innerPlayer.getPing());
                        if (innerPlayer instanceof UserConnection userConnection) {
                            clubTabList.get(s).get(proxiedPlayer).setGameMode(innerPlayer, userConnection.getGamemode());
                        }
                    }
                }
            }
        }, 1L, 1L, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        onPlayerJoinProxy(event.getPlayer());
    }

    @EventHandler
    public void on(ServerConnectedEvent event) {
        if (event.getServer().getInfo().getName().startsWith("club_")) {
            onPlayerConnectClub(event.getPlayer(), event.getServer().getInfo());
        }
    }

    @EventHandler
    public void on(ServerDisconnectEvent event) {
        if (event.getTarget().getName().startsWith("club_")) {
            onPlayerDisconnectClub(event.getPlayer(), event.getTarget());
        }
    }

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        onPlayerQuitProxy(event.getPlayer());
    }
}
