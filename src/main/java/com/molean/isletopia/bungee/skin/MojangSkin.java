package com.molean.isletopia.bungee.skin;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

public class MojangSkin {
    public static MojangSkin fromId(String id){
        MojangSkin mojangSkin = null;
        try {
            String urlString = "https://sessionserver.mojang.com/session/minecraft/profile/{uuid}?unsigned=false";
            urlString = urlString.replaceAll("\\{uuid}", id);
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStream.readAllBytes();
            String s = new String(bytes);
            mojangSkin = new Gson().fromJson(s, MojangSkin.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mojangSkin;
    }

    public static class Property {
        private String name;
        private String value;
        private String signature;

        public Property() {
        }

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
    private String id;
    private String name;
    private Set<Property> properties;

    public MojangSkin() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public MojangSkin(String id, String name, Set<Property> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }
}
