package com.molean.isletopia.bungee.parameter;

import com.molean.isletopia.bungee.dao.ParameterDao;

public class GroupParameter {
    public static void set(String group, String key, String value) {
        ParameterDao.set("group", group, key, value);
    }

    public static String get(String group, String key) {
        return ParameterDao.get("group", group, key);
    }

    public static void unset(String group, String key) {
        ParameterDao.delete("group", group, key);
    }

}
