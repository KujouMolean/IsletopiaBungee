package com.molean.isletopia.bungee.skin;

import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MojangUUID {
    private String name;
    private String id;

    @Nullable
    public static MojangUUID fromName(String name) {
        MojangUUID mojangUUID = null;
        try {
            String urlString = "https://api.mojang.com/users/profiles/minecraft/{name}";
            urlString = urlString.replaceAll("\\{name}", name);
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStream.readAllBytes();
            String s = new String(bytes);
            mojangUUID = new Gson().fromJson(s, MojangUUID.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mojangUUID;

    }

    public MojangUUID() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MojangUUID(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
