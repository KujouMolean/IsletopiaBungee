package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.IsletopiaBungee;
import com.molean.isletopia.bungee.parameter.HostNameParameter;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.shared.platform.BukkitRelatedUtils;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class KickUnsupportedUser implements Listener {

    Map<String, Integer> map = new ConcurrentHashMap<>();

    public KickUnsupportedUser() {
        ProxyServer.getInstance().getPluginManager().registerListener(IsletopiaBungee.getPlugin(), this);

        ProxyServer.getInstance().getScheduler().schedule(BungeeRelatedUtils.getPlugin(), () -> {
            map.clear();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
                String hostName = socketAddress.getAddress().getHostAddress();
                Integer orDefault = map.getOrDefault(hostName, 0);
                if (orDefault > 5) {
                    player.disconnect(new TextComponent("你所在IP的在线玩家过多!"));
                } else {
                    map.put(hostName, orDefault + 1);
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onPlayerJoin(PreLoginEvent event) {

        String name = event.getConnection().getName();
        InetSocketAddress socketAddress = (InetSocketAddress) event.getConnection().getSocketAddress();
        String hostName = socketAddress.getAddress().getHostAddress();
        PlayerParameter.set(name, "hostAddress", hostName);
        String isBannedHostName = HostNameParameter.get(hostName, "isBanned");
        if ("true".equalsIgnoreCase(isBannedHostName)) {

            String reason = HostNameParameter.get(hostName, "bannedReason");
            if (reason == null) {
                reason = "";
            }

            event.getConnection().disconnect(new TextComponent("你已被封禁!" + reason));
            return;
        }
        String isBanned = PlayerParameter.get(name, "isBanned");
        if ("true".equalsIgnoreCase(isBanned)) {

            String pardonTime = PlayerParameter.get(name, "pardonTime");
            if (pardonTime != null && !pardonTime.isEmpty()) {

                long l = Long.parseLong(pardonTime);
                if (System.currentTimeMillis() > l) {
                    PlayerParameter.unset(name, "isBanned");
                    PlayerParameter.unset(name, "pardonTime");
                }else{
                    String reason = PlayerParameter.get(name, "bannedReason");
                    if (reason == null) {
                        reason = "";
                    }
                    LocalDateTime localDateTime = new Timestamp(l).toLocalDateTime();
                    localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
                    String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    event.getConnection().disconnect(new TextComponent("你已被封禁至" + format + "!" + reason));
                    return;
                }
            } else {
                String reason = PlayerParameter.get(name, "bannedReason");
                if (reason == null) {
                    reason = "";
                }
                event.getConnection().disconnect(new TextComponent("你已被封禁!" + reason));
                return;
            }
        }
    }

}
