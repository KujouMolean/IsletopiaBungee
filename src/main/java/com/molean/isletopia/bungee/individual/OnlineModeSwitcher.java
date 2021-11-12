package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.IsletopiaBungee;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.shared.utils.RedisUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.lang.reflect.Field;

public class OnlineModeSwitcher implements Listener {
    public OnlineModeSwitcher() {
        ProxyServer.getInstance().getPluginManager().registerListener(IsletopiaBungee.getPlugin(), this);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerPreLogin(PreLoginEvent event) {
        PendingConnection connection = event.getConnection();
        String name = event.getConnection().getName();
        if (name.startsWith("#")) {
            connection.setOnlineMode(false);
            return;
        }
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
                                        
                    §7离线用户请在用户名前加上符号"#"
                                        
                    §7在启动器填写"#@"后重进服务器
                                        
                     
                    §5✧ §4正版玩家 §5✧
                                        
                    §7首先确保网络通畅后再次尝试进入服务器
                                        
                    §7或在启动器重新登录正版账号以刷新登录信息
                    """.replaceAll("@", name));
            connection.disconnect(textComponent);
        } else {
            PlayerParameter.set(name, "lastLogin", System.currentTimeMillis() + "");
        }
    }

    @EventHandler
    public void on(PostLoginEvent event) {
        String name = event.getPlayer().getName();
        PlayerParameter.set(name, "lastLogin", 0 + "");
    }

    @EventHandler
    public void onPreLogin(LoginEvent event) {
        String name = event.getConnection().getName();
        String uuid = event.getConnection().getUniqueId().toString();
        if (event.getLoginResult() == null) {
            try {
                PendingConnection pendingConnection = event.getConnection();
                Class<?> initialHandlerClass = pendingConnection.getClass();
                Field loginProfile = initialHandlerClass.getDeclaredField("loginProfile");
                String value = RedisUtils.getCommand().get(name + ":SkinValue");
                String signature = RedisUtils.getCommand().get(name+ ":SkinSignature");
                if (value != null && signature != null) {
                    LoginResult.Property property = new LoginResult.Property("textures", value, signature);
                    LoginResult loginResult = new LoginResult(uuid, name, new LoginResult.Property[]{property});
                    loginProfile.setAccessible(true);
                    loginProfile.set(pendingConnection, loginResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LoginResult.Property property = event.getLoginResult().getProperties()[0];
            String value = property.getValue();
            String signature = property.getSignature();
            RedisUtils.getCommand().set(name + ":SkinValue", value);
            RedisUtils.getCommand().set(name + ":SkinSignature", signature);
        }
    }


}
