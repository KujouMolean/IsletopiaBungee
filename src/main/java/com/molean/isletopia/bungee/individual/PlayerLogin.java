package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.shared.database.AccountDao;
import com.molean.isletopia.shared.database.UUIDDao;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import com.molean.isletopia.shared.model.Account;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import com.molean.isletopia.shared.service.AccountService;
import com.molean.isletopia.shared.service.UniversalParameter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerLogin extends Command implements Listener {

    public static final Map<ProxiedPlayer, Boolean> isLoggedInMap = new HashMap<>();
    private static final Set<ProxiedPlayer> unregisteredSet = new HashSet<>();
    private static final Map<ProxiedPlayer, Long> lastDispatchMap = new HashMap<>();
    public static final Set<ProxiedPlayer> failedPlayers = new HashSet<>();


    public PlayerLogin() {
        super("login", "login", "l", "登录");
        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeRelatedUtils.getPlugin(), this);
        ProxyServer.getInstance().getPluginManager().registerListener(BungeeRelatedUtils.getPlugin(), this);
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            Server server = player.getServer();
            if (server == null) {
                continue;
            }
            String name = server.getInfo().getName();
            if (name.startsWith("server")) {
                isLoggedInMap.put(player, Boolean.TRUE);
            } else {
                joinCheck(player);
            }
        }
        ProxyServer.getInstance().getScheduler().schedule(BungeeRelatedUtils.getPlugin(), () -> {
            ServerInfo dispatcher = ProxyServer.getInstance().getServerInfo("dispatcher");
            if (dispatcher == null) {
                return;
            }
            for (ProxiedPlayer player : dispatcher.getPlayers()) {
                checkRegistration(player);
                if (unregisteredSet.contains(player)) {
                    if (failedPlayers.contains(player)) {
                        failedPlayers.remove(player);
                        continue;
                    }
                    player.sendMessage(ChatMessageType.ACTION_BAR
                            , TextComponent.fromLegacyText("请注册: /register 密码 确认密码"));
                } else if (!isLoggedInMap.getOrDefault(player, Boolean.FALSE)) {
                    if (failedPlayers.contains(player)) {
                        failedPlayers.remove(player);
                        continue;
                    }
                    player.sendMessage(ChatMessageType.ACTION_BAR
                            , TextComponent.fromLegacyText("请登录: /login 密码"));
                } else {
                    dispatch(player);
                }
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    public void dispatch(ProxiedPlayer player) {
        long l = System.currentTimeMillis();
        if (l - lastDispatchMap.getOrDefault(player, 0L) < 30 * 1000) {
            return;
        }
        lastDispatchMap.put(player, l);
        if (UUIDDao.query(player.getUniqueId()) != null) {
            UUIDDao.update(player.getUniqueId(), player.getName());
        } else {
            UUIDDao.insert(player.getUniqueId(), player.getName());
        }
        UUID uuid = player.getUniqueId();
        String server = UniversalParameter.getParameter(uuid, "server");
        if (server == null || server.isEmpty()) {
            ArrayList<String> strings = new ArrayList<>();
            for (String s : ProxyServer.getInstance().getServers().keySet()) {
                if (s.startsWith("server")) {
                    strings.add(s);
                }
            }
            String defaultServer = strings.get(new Random().nextInt(strings.size()));
            UniversalParameter.setParameter(uuid, "server", defaultServer);
            server = defaultServer;
        }

        String lastServer = UniversalParameter.getParameter(uuid, "lastServer");
        if (lastServer != null) {
            server = lastServer;
        }
        ServerMessageUtils.switchServer(player.getName(), server);
    }

    public void checkRegistration(ProxiedPlayer player) {
        if (!player.getName().startsWith("#")) {
            return;
        }

        Account account = null;
        try {
            account = AccountDao.getAccount(player.getName().toLowerCase(Locale.ROOT));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (account == null) {
            unregisteredSet.add(player);
        } else {
            unregisteredSet.remove(player);
            String realname = account.getRealname();
            if (!realname.equals(player.getName().replaceAll("#", ""))) {
                player.disconnect(TextComponent.fromLegacyText("#你的账号已被注册，你应该使用 #" + realname + " 进入游戏"));
            }
        }

    }

    public void joinCheck(ProxiedPlayer player) {

        Server server = player.getServer();
        if (server != null && !server.getInfo().getName().equalsIgnoreCase("dispatcher")) {
            isLoggedInMap.put(player, Boolean.TRUE);
        } else if (!player.getName().startsWith("#")) {
            isLoggedInMap.put(player, Boolean.TRUE);
            player.setPermission("login", false);
            player.setPermission("register", false);
            player.setPermission("password", false);
        } else {
            player.setPermission("login", true);
            player.setPermission("register", true);
            player.setPermission("password", false);
        }

        if (isLoggedInMap.getOrDefault(player, Boolean.FALSE)) {
            dispatch(player);
        } else {
            checkRegistration(player);
        }
    }

    @EventHandler
    public void on(ServerConnectedEvent event) throws SQLException {
        ProxiedPlayer player = event.getPlayer();
        String serverName = event.getServer().getInfo().getName();
        if (serverName.equalsIgnoreCase("dispatcher")) {
            joinCheck(player);
        } else if (player.getName().startsWith("#")) {
            player.setPermission("login", false);
            player.setPermission("register", false);
            player.setPermission("password", true);
        }
    }

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        unregisteredSet.remove(event.getPlayer());
        isLoggedInMap.remove(event.getPlayer());
    }

    @EventHandler
    public void on(ServerDisconnectEvent event) {
        lastDispatchMap.remove(event.getPlayer());
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length < 1) {
            return;
        }
        if (AccountService.login(sender.getName(), args[0])) {
            isLoggedInMap.put(player, Boolean.TRUE);
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("§aSUCCESS"));
        } else {
            player.sendMessage(ChatMessageType.ACTION_BAR
                    , TextComponent.fromLegacyText("§cFAILED"));
            failedPlayers.add(player);
        }
    }
}
