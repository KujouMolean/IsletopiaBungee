package com.molean.isletopia.bungee.parameter;

import com.molean.isletopia.bungee.dao.ParameterDao;

public class PlayerParameter {

    public static void set(String player, String key, String value) {
        ParameterDao.set("player", player, key, value);
    }

    public static String get(String player, String key) {
        return ParameterDao.get("player", player, key);
    }

    public static void unset(String player, String key) {
        ParameterDao.delete("player", player, key);
    }

}
