package com.molean.isletopia.bungee.individual;

import com.molean.isletopia.bungee.IsletopiaBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class WelcomeMessage implements Listener {

    private static Favicon favicon;


    public WelcomeMessage() {
        ProxyServer.getInstance().getPluginManager().registerListener(IsletopiaBungee.getPlugin(), this);
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("server-icon.png");
            assert inputStream != null;
            BufferedImage read = ImageIO.read(inputStream);
            favicon = Favicon.create(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @EventHandler
    public void on(ProxyPingEvent event) {
        String line1 = "       #§lM#§li#§ln#§le#§lc#§lr#§la#§lf#§lt #§l1#§l.#§l1#§l8#§l.#§l1 #§l- #§lI#§ls#§ll#§le#§lt#§lo#§lp#§li#§la#§lS#§le#§lr#§lv#§le#§lr";
        String line2 = "            #§l欢#§l迎#§l加#§l入#§l梦#§l幻#§l之#§l屿#§l原#§l版#§l空#§l岛#§l服#§l务#§l器";
        String text = generateRainbowText(line1) + "\n" + generateRainbowText(line2);
        event.getResponse().setFavicon(favicon);
        event.getResponse().setDescriptionComponent(new TextComponent(TextComponent.fromLegacyText(text)));
    }


    private static final Random random = new Random();

    public static String generateRainbowText(String text) {
        String[] split = text.split("#");
        Color begin = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Color end = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int incrementRed = (end.getRed() - begin.getRed()) / (split.length - 1);
        int incrementGreen = (end.getGreen() - begin.getGreen()) / (split.length - 1);
        int incrementBlu = (end.getBlue() - begin.getBlue()) / (split.length - 1);
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(split[0]);
        for (int i = 1; i < split.length; i++) {
            Color color = new Color(
                    begin.getRed() + incrementRed * i,
                    begin.getGreen() + incrementGreen * i,
                    begin.getBlue() + incrementBlu * i);
            String colorString = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
            ChatColor chatColor = ChatColor.of(colorString);
            textBuilder.append(ChatColor.COLOR_CHAR + "r").append(chatColor.toString()).append(split[i]);
        }
        return textBuilder.toString();
    }
}
