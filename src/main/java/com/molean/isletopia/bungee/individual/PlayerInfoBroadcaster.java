package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.IsletopiaBungee;
import com.molean.isletopia.shared.pojo.obj.PlayerInfoObject;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerInfoBroadcaster {
    public PlayerInfoBroadcaster() {
        ProxyServer.getInstance().getScheduler().schedule(IsletopiaBungee.getPlugin(), () -> {
            ArrayList<ProxiedPlayer> proxiedPlayers = new ArrayList<>(ProxyServer.getInstance().getPlayers());
            ArrayList<String> playersName = new ArrayList<>();
            Map<String, List<String>> serverPlayersMap = new HashMap<>();

            for (String s : ProxyServer.getInstance().getConfig().getServersCopy().keySet()) {
                serverPlayersMap.put(s, new ArrayList<>());
            }

            for (ProxiedPlayer proxiedPlayer : proxiedPlayers) {
                if(proxiedPlayer.getServer()==null){
                    continue;
                }
                playersName.add(proxiedPlayer.getName());
                String server = proxiedPlayer.getServer().getInfo().getName();
                List<String> list = serverPlayersMap.get(server);
                list.add(proxiedPlayer.getName());
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
