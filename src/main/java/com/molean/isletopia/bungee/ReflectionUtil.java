package com.molean.isletopia.bungee;

import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.tab.TabList;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static void setTablistHandler(ProxiedPlayer player, TabList tablistHandler) throws NoSuchFieldException, IllegalAccessException {
        setField(UserConnection.class, player, "tabListHandler", tablistHandler, 5);
    }

    public static TabList getTablistHandler(ProxiedPlayer player) throws NoSuchFieldException, IllegalAccessException {
        return getField(UserConnection.class, player, "tabListHandler", 5);
    }

    public static ChannelWrapper getChannelWrapper(ProxiedPlayer player) throws NoSuchFieldException, IllegalAccessException {
        return getField(UserConnection.class, player, "ch", 50);
    }

    public static void setField(Class<?> clazz, Object instance, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField(field);
        f.setAccessible(true);
        f.set(instance, value);
    }

    public static void setField(Class<?> clazz, Object instance, String field, Object value, int tries) throws NoSuchFieldException, IllegalAccessException {
        while(true) {
            --tries;
            if (tries <= 0) {
                setField(clazz, instance, field, value);
                return;
            }

            try {
                setField(clazz, instance, field, value);
                return;
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
            }
        }
    }

    public static <T> T getField(Class<?> clazz, Object instance, String field) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField(field);
        f.setAccessible(true);
        return (T) f.get(instance);
    }

    public static <T> T getField(Class<?> clazz, Object instance, String field, int tries) throws NoSuchFieldException, IllegalAccessException {
        while(true) {
            --tries;
            if (tries <= 0) {
                return getField(clazz, instance, field);
            }

            try {
                return getField(clazz, instance, field);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
            }
        }
    }
}
