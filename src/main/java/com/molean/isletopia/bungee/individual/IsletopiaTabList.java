package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.ReflectionUtil;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.tab.TabList;

import java.util.UUID;

public class IsletopiaTabList extends TabList {

    public IsletopiaTabList(ProxiedPlayer player) {
        super(player);
    }

    public ProxiedPlayer getPlayer() {
        return this.player;
    }

    public void executeInEventLoop(Runnable runnable) {
        ChannelWrapper ch;
        try {
            ch = ReflectionUtil.getChannelWrapper(this.getPlayer());
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        if (ch.getHandle().eventLoop().inEventLoop()) {
            runnable.run();
        } else {
            ch.getHandle().eventLoop().execute(runnable);
        }

    }

    @Override
    public void onPingChange(final int i) {

    }

    @Override
    public void onServerChange() {
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onUpdate(PlayerListItem playerListItem) {
    }


    void addFakePlayer(UUID uuid, String name) {
        executeInEventLoop(() -> {
            PlayerListItem pli = new PlayerListItem();
            pli.setAction(PlayerListItem.Action.ADD_PLAYER);
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setPing(0);
            item.setUsername(name);
//            item.setDisplayName(display);
            item.setUuid(uuid);
            item.setProperties(new String[0][0]);
            pli.setItems(new PlayerListItem.Item[]{item});
            this.player.unsafe().sendPacket(pli);
        });
    }

    void removeFakePlayer(UUID uuid) {
        executeInEventLoop(() -> {
            PlayerListItem pli = new PlayerListItem();
            pli.setAction(PlayerListItem.Action.REMOVE_PLAYER);
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(uuid);
            pli.setItems(new PlayerListItem.Item[]{item});
            this.player.unsafe().sendPacket(pli);
        });
    }

    void addPlayer(ProxiedPlayer proxiedPlayer) {
        executeInEventLoop(() -> {
            PlayerListItem pli = new PlayerListItem();
            pli.setAction(PlayerListItem.Action.ADD_PLAYER);
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setPing(proxiedPlayer.getPing());
            item.setUsername(proxiedPlayer.getName());
            proxiedPlayer.getUniqueId();
            item.setGamemode(((UserConnection) proxiedPlayer).getGamemode());
            item.setUuid(proxiedPlayer.getUniqueId());
            item.setProperties(new String[0][0]);
            LoginResult loginResult = ((UserConnection) proxiedPlayer).getPendingConnection().getLoginProfile();
            if (loginResult != null) {
                String[][] props = new String[loginResult.getProperties().length][];

                for (int i = 0; i < props.length; ++i) {
                    props[i] = new String[]{loginResult.getProperties()[i].getName(), loginResult.getProperties()[i].getValue(), loginResult.getProperties()[i].getSignature()};
                }

                item.setProperties(props);
            } else {
                item.setProperties(new String[0][0]);
            }
            pli.setItems(new PlayerListItem.Item[]{item});
            this.player.unsafe().sendPacket(pli);
        });


    }

    void removePlayer(ProxiedPlayer proxiedPlayer) {
        executeInEventLoop(() -> {
            PlayerListItem pli = new PlayerListItem();
            pli.setAction(PlayerListItem.Action.REMOVE_PLAYER);
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(proxiedPlayer.getUniqueId());
            pli.setItems(new PlayerListItem.Item[]{item});
            this.player.unsafe().sendPacket(pli);
        });
    }


    void setPing(ProxiedPlayer proxiedPlayer, int ping) {
        executeInEventLoop(() -> {
            PlayerListItem pli = new PlayerListItem();
            pli.setAction(PlayerListItem.Action.UPDATE_LATENCY);
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(proxiedPlayer.getUniqueId());
            item.setPing(ping);
            pli.setItems(new PlayerListItem.Item[]{item});
            this.player.unsafe().sendPacket(pli);
        });
    }

    void setGameMode(ProxiedPlayer player, int gamemode) {
        executeInEventLoop(() -> {
            PlayerListItem pli = new PlayerListItem();
            pli.setAction(PlayerListItem.Action.UPDATE_GAMEMODE);
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setGamemode(gamemode);
            pli.setItems(new PlayerListItem.Item[]{item});
            this.player.unsafe().sendPacket(pli);
        });
    }

    void setDisplayName(UUID uuid, String name) {
        executeInEventLoop(() -> {
            PlayerListItem pli = new PlayerListItem();
            pli.setAction(PlayerListItem.Action.UPDATE_DISPLAY_NAME);
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(uuid);
            item.setDisplayName(name);
            pli.setItems(new PlayerListItem.Item[]{item});
            this.player.unsafe().sendPacket(pli);
        });
    }

}
