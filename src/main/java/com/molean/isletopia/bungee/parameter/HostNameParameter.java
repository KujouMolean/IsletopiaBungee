package com.molean.isletopia.bungee.parameter;

import com.molean.isletopia.bungee.dao.ParameterDao;

public class HostNameParameter {
    public static void set(String hostname, String key, String value) {
        ParameterDao.set("hostname", hostname, key, value);
    }

    public static String get(String hostname, String key) {
        return ParameterDao.get("hostname",hostname, key);
    }

    public static void unset(String hostname, String key) {
        ParameterDao.delete("hostname", hostname, key);
    }
}
