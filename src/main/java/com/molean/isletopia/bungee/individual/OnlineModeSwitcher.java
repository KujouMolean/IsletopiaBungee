package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.IsletopiaBungee;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.shared.utils.RedisUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class OnlineModeSwitcher implements Listener {
    public OnlineModeSwitcher() {
        ProxyServer.getInstance().getPluginManager().registerListener(IsletopiaBungee.getPlugin(), this);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerPreLogin(PreLoginEvent event) {
        PendingConnection connection = event.getConnection();
        String name = event.getConnection().getName();
        connection.setOnlineMode(true);
        String lastLoginString = PlayerParameter.get(name, "lastLogin");
        if (lastLoginString == null || lastLoginString.isEmpty()) {
            lastLoginString = "0";
        }
        long lastLogin = Long.parseLong(lastLoginString);
        if (System.currentTimeMillis() - lastLogin < 60 * 1000) {
            PlayerParameter.set(name, "lastLogin", 0 + "");
            TextComponent textComponent = new TextComponent("""
                    §7【§d§l梦幻之屿§7】

                    §c✘§6检测到正版验证登录失败§c✘


                    §b✦ §3非正版玩家 §b✦

                    §7请购买正版后再加入此服务器

                    §7或自行更换其他服务器进行游戏


                    §5✦ §4正版玩家 §5✦

                    §7首先确保网络通畅后再次尝试进入服务器

                    §7或在启动器重新登录正版账号以刷新登录信息
                    """.replaceAll("@", name));
            connection.disconnect(textComponent);
        } else {
            PlayerParameter.set(name, "lastLogin", System.currentTimeMillis() + "");
        }
    }

    @EventHandler
    public void onPreLogin(LoginEvent event) {
        String name = event.getConnection().getName();
        LoginResult.Property property = event.getLoginResult().getProperties()[0];
        String value = property.getValue();
        String signature = property.getSignature();
        RedisUtils.getCommand().set(name + ":SkinValue", value);
        RedisUtils.getCommand().set(name + ":SkinSignature", signature);
    }
}
