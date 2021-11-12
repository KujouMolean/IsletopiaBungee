package com.molean.isletopia.bungee.cirno;

import com.molean.crinobot.Robot;
import com.molean.isletopia.bungee.IsletopiaBungee;
import com.molean.isletopia.bungee.individual.UniversalChat;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tbp.v20190627.TbpClient;
import com.tencentcloudapi.tbp.v20190627.models.Group;
import com.tencentcloudapi.tbp.v20190627.models.TextProcessRequest;
import com.tencentcloudapi.tbp.v20190627.models.TextProcessResponse;
import net.mamoe.mirai.contact.NormalMember;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CirnoUtils {

    public static String getResponse(String terminalId, String inputText) {
        try {
            Credential cred = new Credential("AKID2tUorc7LIxMULuDFzSfdJiqiIbdPu89t", "VjgMmvnOjna6KIzPYQ1KM8dqmRqxnMef");
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tbp.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            TbpClient client = new TbpClient(cred, "", clientProfile);
            TextProcessRequest req = new TextProcessRequest();
            req.setBotId("7652b54d-9204-403a-a724-d896bf767d2d");
            req.setBotEnv("dev");
            req.setTerminalId(terminalId);
            req.setInputText(inputText);
            TextProcessResponse resp = client.TextProcess(req);
            Group[] groupList = resp.getResponseMessage().getGroupList();
            StringBuilder plainMessage = new StringBuilder();
            for (Group group : groupList) {
                plainMessage.append(group.getContent());

            }
            return plainMessage.toString();
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return "琪露诺再想想..";
    }


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


    private static final Map<Long, String> cachedNameCard = new HashMap<>();
    private static final Map<Long, Long> cachedNameCardTime = new HashMap<>();

    public static String getNameCardByQQ(long id) {
        if (System.currentTimeMillis() - cachedNameCardTime.getOrDefault(id, 0L) < 3 * 60 * 1000) {
            return cachedNameCard.get(id);
        }

        for (NormalMember member : Objects.requireNonNull(Robot.getBot().getGroup(483653595L)).getMembers()) {
            if (member.getId() == id) {
                cachedNameCard.put(id, member.getNameCard());
                cachedNameCardTime.put(id, System.currentTimeMillis());

                return member.getNameCard();
            }
        }
        return id + "";
    }

    public static void gameMessage(String message) {
        for (ProxiedPlayer player : IsletopiaBungee.getPlugin().getProxy().getPlayers()) {
            player.sendMessage(TextComponent.fromLegacyText("§f<§bCirnoBot§f> §r" + message));
        }
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
}
