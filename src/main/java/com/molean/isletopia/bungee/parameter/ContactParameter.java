package com.molean.isletopia.bungee.parameter;

import com.molean.isletopia.bungee.dao.ParameterDao;

import java.util.ArrayList;
import java.util.List;

public class ContactParameter {
    public static void set(long qq, String key, String value) {
        ParameterDao.set("contact", qq + "", key, value);
    }

    public static String get(long qq, String key) {
        return ParameterDao.get("contact", qq + "", key);
    }

    public static void unset(long qq, String key) {
        ParameterDao.delete("contact", qq + "", key);
    }

    public static List<Long> targets() {
        List<String> contact = ParameterDao.targets("contact");
        ArrayList<Long> longs = new ArrayList<>();
        for (String s : contact) {
            longs.add(Long.parseLong(s));
        }
        return longs;
    }
}
