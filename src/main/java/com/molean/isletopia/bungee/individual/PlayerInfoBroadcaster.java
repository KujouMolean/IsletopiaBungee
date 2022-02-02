package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.IsletopiaBungee;
import com.molean.isletopia.shared.pojo.obj.PlayerInfoObject;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerInfoBroadcaster {
    public PlayerInfoBroadcaster() {
        ProxyServer.getInstance().getScheduler().schedule(IsletopiaBungee.getPlugin(), () -> {
            ArrayList<ProxiedPlayer> proxiedPlayers = new ArrayList<>(ProxyServer.getInstance().getPlayers());
            Map<UUID,String> playersName = new HashMap<>();
            Map<String, Map<UUID,String>> serverPlayersMap = new HashMap<>();

            for (String s : ProxyServer.getInstance().getConfig().getServers().keySet()) {
                serverPlayersMap.put(s, new HashMap<>());
            }

            for (ProxiedPlayer proxiedPlayer : proxiedPlayers) {
                if(proxiedPlayer.getServer()==null){
                    continue;
                }
                playersName.put(proxiedPlayer.getUniqueId(), proxiedPlayer.getName());
                String server = proxiedPlayer.getServer().getInfo().getName();
                Map<UUID, String> map = serverPlayersMap.get(server);
                map.put(proxiedPlayer.getUniqueId(), proxiedPlayer.getName());
            }

            PlayerInfoObject playerInfoObject = new PlayerInfoObject();
            playerInfoObject.setPlayers(playersName);
            playerInfoObject.setPlayersPerServer(serverPlayersMap);
            if (proxiedPlayers.size() > 0) {
                ServerMessageUtils.broadcastBungeeMessage( "PlayerInfo", playerInfoObject);
            }

        }, 1, 1, TimeUnit.SECONDS);
    }
}
