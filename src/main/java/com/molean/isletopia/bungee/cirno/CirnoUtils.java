package com.molean.isletopia.bungee.cirno;

import com.molean.cirnobot.Robot;
import com.molean.isletopia.bungee.individual.UniversalChat;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CirnoUtils {


    public static int realLength(String string) {
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < string.length(); i++) {
            String single = string.substring(i, i + 1);
            if (single.matches(chinese)) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }

    public static String leftString(String string, int size) {
        if (size == 0)
            return "";
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < string.length(); i++) {
            String single = string.substring(i, i + 1);
            if (single.matches(chinese)) {
                length += 2;
            } else {
                length += 1;
            }
            if (length >= size) {
                return string.substring(0, i + 1);
            }
        }
        return string;
    }


    public static String getNameCardByQQ(long id) {

        NormalMember normalMember = getGameGroup().get(id);
        if (normalMember == null) {
            return id + "";
        }
        String nameCard = normalMember.getNameCard();
        if (nameCard.isEmpty()) {
            return normalMember.getNick();
        } else {
            return normalMember.getNameCard();
        }
    }

    public static net.mamoe.mirai.contact.Group getGameGroup() {
        return Robot.getBot().getGroup(483653595L);
    }


    public static void broadcastMessage(String message) {
        UniversalChat.chatMessage("白", "§bCirnoBot", message);

        Objects.requireNonNull(Robot.getBot().getGroup(483653595L)).sendMessage(message.replaceAll("§.", ""));

    }

    public static void broadcastChat(String sender, String message) {
        String p = sender.replace('§', '&');
        String m = message.replace('§', '&');
        m = m.replace("\n", "\\n");

        if (realLength(p) > 16) {
            p = leftString(p, 16) + "..";
        }

        int addition = 0;

        Pattern pattern = Pattern.compile("\\{url#(.*)#(.*)}");
        Matcher matcher = pattern.matcher(m);

        if (matcher.find()) {
            do {
                String group = matcher.group();
                addition += group.length();
            } while (matcher.find(matcher.start() + 1));
        }


        if (realLength(m) > 256 + addition) {
            m = leftString(m, 256 + addition) + "..";
        }
        if (m.trim().length() == 0)
            return;
        UniversalChat.chatMessage("白", "§o" + p, m);
    }

    private static final HashSet<SimpleListenerHost> hashSet = new HashSet<>();

    public static void registerListener(SimpleListenerHost simpleListenerHost) {
        EventChannel<BotEvent> eventChannel = Robot.getBot().getEventChannel();
        hashSet.add(simpleListenerHost);
        eventChannel.registerListenerHost(simpleListenerHost);
    }

    public static void clearListener() {
        for (SimpleListenerHost simpleListenerHost : hashSet) {
            simpleListenerHost.cancelAll();
        }
    }
}
