package com.molean.isletopia.bungee.individual;

import com.molean.crinobot.CrinoBot;
import com.molean.isletopia.bungee.IsletopiaBungee;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import com.molean.isletopia.shared.pojo.obj.PlaySoundObject;
import com.molean.isletopia.shared.service.UniversalParameter;
import com.molean.isletopia.shared.utils.Pair;
import com.molean.isletopia.shared.utils.RedisUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UniversalChat implements Listener {
    final public static Map<String, Set<String>> collections = new HashMap<>();
    final public static Map<String, String> commandMapping = new HashMap<>();


    @EventHandler
    public void on(ChatEvent event) {
        if (event.isCommand()) {
            return;
        }
        if (event.isProxyCommand()) {
            return;
        }
        if (!(event.getSender() instanceof ProxiedPlayer proxiedPlayer)) {
            return;
        }
        Server server = proxiedPlayer.getServer();
        if (server == null) {
            event.setCancelled(true);
            return;
        }

        if (server.getInfo().getName().startsWith("club_")) {
            return;
        }

        if (server.getInfo().getName().equalsIgnoreCase("dispatcher")) {
            if (!PlayerLogin.isLoggedInMap.getOrDefault(proxiedPlayer, false)) {
                event.setCancelled(true);
                return;
            }
        }


        String p = proxiedPlayer.getName();
        String m = event.getMessage();
        String channel = ChatChannel.getChannel(proxiedPlayer.getUniqueId());
        UniversalChat.chatMessage(channel, p, m);
        if (channel.equalsIgnoreCase("白")) {
            ProxyServer.getInstance().getScheduler().runAsync(IsletopiaBungee.getPlugin(), () -> {
                CrinoBot.sendMessage("<" + p + "> " + m);
            });
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        String name = event.getPlayer().getName();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (collections.containsKey(player.getName()) && collections.get(player.getName()).contains(name)) {
                player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7" + name + "下线了"));
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        String name = event.getPlayer().getName();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (collections.containsKey(player.getName()) && collections.get(player.getName()).contains(name)) {
                player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§a" + name + "上线了"));
            }
        }
        String channel = UniversalParameter.getParameter(event.getPlayer().getUniqueId(), "Channel");
        if (channel == null || channel.isEmpty()) {
            channel = "白";
        }

    }


    public UniversalChat() {
        ProxyServer.getInstance().getPluginManager().registerListener(IsletopiaBungee.getPlugin(), this);
        ProxyServer.getInstance().getScheduler().schedule(BungeeRelatedUtils.getPlugin(), () -> {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (RedisUtils.getCommand().exists("Collection-" + player.getName()) > 0) {
                    String s = RedisUtils.getCommand().get("Collection-" + player.getName());
                    if (s != null && !s.isEmpty()) {
                        String[] split = s.split(",");
                        HashSet<String> strings = new HashSet<>(Arrays.asList(split));
                        collections.put(player.getName(), strings);
                    }
                }
            }
        }, 5, 5, TimeUnit.SECONDS);

        commandMapping.put("wiki", "url#https://wiki.islet.world/");
        commandMapping.put("Wiki", "url#https://wiki.islet.world/");
        commandMapping.put("指南", "url#https://wiki.islet.world/");

        commandMapping.put("新合成表", "cmd#/menu recipe");

        commandMapping.put("FAQ", "url#https://wiki.islet.world/faq.html");
        commandMapping.put("faq", "url#https://wiki.islet.world/faq.html");
        commandMapping.put("常见问答", "url#https://wiki.islet.world/faq.html");

        commandMapping.put("游戏规则", "url#https://wiki.islet.world/guide/rules.html");

        commandMapping.put("魔改内容", "url#https://wiki.islet.world/guide/modification.html");

        commandMapping.put("特殊机制", "url#https://wiki.islet.world/guide/mechanism.html");
        commandMapping.put("特色系统", "url#https://wiki.islet.world/feature.html");
        commandMapping.put("社团制度", "url#https://wiki.islet.world/club.html");
        commandMapping.put("社团系统", "url#https://wiki.islet.world/club.html");

        commandMapping.put("活动", "url#https://wiki.islet.world/activities/");
        commandMapping.put("皮肤站", "url#http://skin.molean.com/");
        commandMapping.put("捐助", "url#https://afdian.net/@molean");
        commandMapping.put("MinecraftWiki", "url#https://minecraft.fandom.com/zh/wiki/Minecraft_Wiki");


        commandMapping.put("群号", "url#http://wiki.islet.world/introduction.html#%E4%B8%80%E8%B5%B7%E7%95%85%E8%81%8A");
        commandMapping.put("QQ群", "url#http://wiki.islet.world/introduction.html#%E4%B8%80%E8%B5%B7%E7%95%85%E8%81%8A");
        commandMapping.put("qq群", "url#http://wiki.islet.world/introduction.html#%E4%B8%80%E8%B5%B7%E7%95%85%E8%81%8A");
        commandMapping.put("企鹅群", "url#http://wiki.islet.world/introduction.html#%E4%B8%80%E8%B5%B7%E7%95%85%E8%81%8A");

        commandMapping.put("一号一岛制度", "url#http://wiki.islet.world/feature.html#%E4%B8%80%E5%8F%B7%E4%B8%80%E5%B2%9B%E5%88%B6%E5%BA%A6");
        commandMapping.put("多区负载均衡", "url#http://wiki.islet.world/feature.html#%E5%A4%9A%E5%8C%BA%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1");
        commandMapping.put("自由生物群系", "url#http://wiki.islet.world/feature.html#%E8%87%AA%E7%94%B1%E7%94%9F%E7%89%A9%E7%BE%A4%E7%B3%BB");
        commandMapping.put("岛屿独立备份", "url#http://wiki.islet.world/feature.html#%E5%B2%9B%E5%B1%BF%E7%8B%AC%E7%AB%8B%E5%A4%87%E4%BB%BD");
        commandMapping.put("原版内容轻改", "url#http://wiki.islet.world/feature.html#%E5%8E%9F%E7%89%88%E5%86%85%E5%AE%B9%E8%BD%BB%E6%94%B9");
        commandMapping.put("社团自治制度", "url#http://wiki.islet.world/feature.html#%E7%A4%BE%E5%9B%A2%E8%87%AA%E6%B2%BB%E5%88%B6%E5%BA%A6");
        commandMapping.put("化繁为简的设计理念", "url#http://wiki.islet.world/feature.html#%E5%8C%96%E7%B9%81%E4%B8%BA%E7%AE%80%E7%9A%84%E8%AE%BE%E8%AE%A1%E7%90%86%E5%BF%B5");
        commandMapping.put("TSI", "url#http://wiki.islet.world/club.html#_1-%E4%BC%8A%E7%A2%A7%E5%A1%94%E6%96%AF%E7%9A%84%E5%91%BC%E5%94%A4");
        commandMapping.put("伊碧塔斯的呼唤", "url#http://wiki.islet.world/club.html#_1-%E4%BC%8A%E7%A2%A7%E5%A1%94%E6%96%AF%E7%9A%84%E5%91%BC%E5%94%A4");
        commandMapping.put("幻想乡", "url#http://wiki.islet.world/club.html#_2-%E5%B9%BB%E6%83%B3%E4%B9%A1");
        commandMapping.put("Gensokyo", "url#http://wiki.islet.world/club.html#_2-%E5%B9%BB%E6%83%B3%E4%B9%A1");
        commandMapping.put("学习激励计划", "url#http://wiki.islet.world/activities/studybump.html");
        commandMapping.put("顶贴", "url#https://wiki.islet.world/activities/bump.html");
        commandMapping.put("顶帖", "url#https://wiki.islet.world/activities/bump.html");
        commandMapping.put("雕塑活动", "url#https://wiki.islet.world/activities/sculpture.html");
        commandMapping.put("小游戏设计大赛", "url#https://wiki.islet.world/activities/2021-07-20.html");
        commandMapping.put("鞘翅", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%8E%B7%E5%8F%96%E9%9E%98%E7%BF%85");
        commandMapping.put("信标", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%8E%B7%E5%8F%96%E4%BF%A1%E6%A0%87");
        commandMapping.put("泥土", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%8E%B7%E5%8F%96%E6%B3%A5%E5%9C%9F");
        commandMapping.put("珊瑚", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%8E%B7%E5%8F%96%E7%8F%8A%E7%91%9A");
        commandMapping.put("去别人岛", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%AE%BF%E9%97%AE%E5%90%8C%E4%BC%B4%E5%B2%9B%E5%B1%BF");
        commandMapping.put("合作", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E4%B8%8E%E5%90%8C%E4%BC%B4%E5%90%88%E4%BD%9C");
        commandMapping.put("红石", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%8E%B7%E5%8F%96%E7%BA%A2%E7%9F%B3");
        commandMapping.put("村民", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%8E%B7%E5%8F%96%E6%9D%91%E6%B0%91");
        commandMapping.put("铁锭", "url#https://wiki.islet.world/faq.html#%E6%88%91%E8%AF%A5%E5%A6%82%E4%BD%95%E8%8E%B7%E5%8F%96%E9%93%81-%E9%87%91-%E4%B8%8B%E5%B1%8A%E5%90%88%E9%87%91");
    }


    private static final Map<String, List<Pair<String, Long>>> playerChatDataMap = new HashMap<>();

    public static void preHandle(String playerName, String message) {
        if (!playerChatDataMap.containsKey(playerName)) {
            playerChatDataMap.put(playerName, new ArrayList<>());
        }

        List<Pair<String, Long>> pairs = playerChatDataMap.get(playerName);

        pairs.add(new Pair<>(message, System.currentTimeMillis()));

        //trim
        pairs.sort((o1, o2) -> (int) (o2.getValue() - o1.getValue()));
        pairs.removeIf(stringLongPair -> stringLongPair.getValue() < System.currentTimeMillis() - 10 * 1000);

        //check
        Map<String, Integer> map = new HashMap<>();
        for (Pair<String, Long> pair : pairs) {
            Integer orDefault = map.getOrDefault(pair.getKey(), 0);
            map.put(pair.getKey(), orDefault + 1);
        }

        map.forEach((s, integer) -> {
            if (integer >= 3) {
                PlayerParameter.set(playerName, "isBanned", "true");
                PlayerParameter.set(playerName, "bannedReason", "刷屏(三句话自动封禁18秒)");
                PlayerParameter.set(playerName, "pardonTime", (System.currentTimeMillis() + 18 * 1000) + "");
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
                if (player != null) {
                    player.disconnect(TextComponent.fromLegacyText("你已被封禁!" + "刷屏(三句话自动封禁18秒)"));
                    CirnoUtils.broadcastMessage(playerName + " 因刷屏被自动封禁。");
                }
            }
        });
    }


    public static void chatMessage(String channel, String sourcePlayer, String message) {
        ArrayList<ProxiedPlayer> targetPlayers = ChatChannel.getPlayersInChannel(channel).stream()
                .map(uuid -> ProxyServer.getInstance().getPlayer(uuid))
                .collect(Collectors.toCollection(ArrayList::new));

        String color = ChatChannel.getChannelColor(channel);

        //判断玩家是否刷屏
        if (!sourcePlayer.startsWith("§")) {
            String realName = sourcePlayer.replaceAll("§.", "");
            if (!realName.equalsIgnoreCase("CirnoBot")) {
                preHandle(sourcePlayer.replaceAll("§.", ""), message);
            }
        }

        //遍历文本，进行特殊替换。
        TextComponent mainText = new TextComponent();
        ArrayList<ProxiedPlayer> targetPlayerList = new ArrayList<>(targetPlayers);
        HashSet<ProxiedPlayer> notifyPlayers = new HashSet<>();
        //玩家名长度逆序排序
        targetPlayerList.sort((o1, o2) -> o2.getName().length() - o1.getName().length());
        int i = 0;
        int j = 0;
        outer:
        while (j < message.length()) {
            //首先判定在线玩家，优先匹配长ID
            for (ProxiedPlayer player : targetPlayerList) {
                //确定子文本
                String substring = message.substring(j);
                if (substring.startsWith(player.getName()) || substring.startsWith(player.getName().replaceAll("#", ""))) {
                    //匹配到了玩家，先把ID前的文本添加。
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);

                    //把玩家名异色处理,然后复原颜色
                    TextComponent textComponent = new TextComponent("§7" + player.getName() + "§r" + color);
                    //添加点击指令
                    String cmd = "/visit " + player.getName();
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
                    mainText.addExtra(textComponent);

                    //提示该玩家被At了
                    notifyPlayers.add(player);

                    //跳转到下一个匹配位置，并停止当前位置的后续匹配
                    j += player.getName().length();
                    i = j;
                    continue outer;
                }
            }


            //匹配关键字，添加超链接与下划线
            for (String s : commandMapping.keySet()) {
                String substring = message.substring(j);
                if (substring.startsWith(s)) {
                    //匹配到了，添加之前的文本
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);

                    //添加下划线与超链接
                    TextComponent textComponent = new TextComponent(color + "§n" + s + "§r" + color);
                    String cmd = commandMapping.get(s);
                    ArrayList<String> strings = new ArrayList<>(Arrays.asList(cmd.split("#")));
                    String operate = strings.get(0).toLowerCase(Locale.ROOT);
                    strings.remove(0);
                    String value = String.join("#", strings);

                    switch (operate) {
                        case "url" -> textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, value));
                        case "cmd" -> textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, value));
                        case "suggest" -> textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value));
                    }

                    //添加到总文本中
                    mainText.addExtra(textComponent);

                    //跳转到下一个匹配位置，并停止当前位置的后续匹配
                    j += s.length();
                    i = j;
                    continue outer;
                }
            }


            String substring = message.substring(j);
            String substringLower = substring.toLowerCase(Locale.ROOT);

            //匹配超链接
            if (substringLower.startsWith("https://") || substringLower.startsWith("http://")) {

                //如果是B站bv超链接，则自动替换为精简串，并添加下划线与超链接

                Pattern pattern = Pattern.compile("https?://www.bilibili.com/video/(BV[0-9a-zA-Z]+).*");
                Matcher matcher = pattern.matcher(substring);
                if (matcher.find()) {
                    String group = matcher.group();
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);
                    TextComponent textComponent = new TextComponent(color + "§n" + matcher.group(1) + "§r" + color);
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, group));
                    mainText.addExtra(textComponent);
                    j += group.length();
                    i = j;
                    continue;
                }
            }

            if (substringLower.startsWith("https://") || substringLower.startsWith("http://")) {
                //如果是普通超链接，则添加下划线与超链接
                Pattern pattern = Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
                Matcher matcher = pattern.matcher(substring);
                if (matcher.find()) {
                    String group = matcher.group();
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);
                    TextComponent textComponent = new TextComponent(color + "§n" + group + "§r" + color);
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, group));
                    mainText.addExtra(textComponent);
                    j += group.length();
                    i = j;
                    continue;
                }
            }

            if (substringLower.startsWith("{")) {
                //如果是图片，则用"[图片]"方式显示，并添加超链接下划线
                Pattern pattern = Pattern.compile("\\{url#(.*)#(.*)}");
                Matcher matcher = pattern.matcher(substring);
                if (matcher.find()) {
                    String group = matcher.group();
                    String text = matcher.group(1);
                    String url = matcher.group(2);
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);
                    TextComponent textComponent = new TextComponent(color + "§n" + text + "§r" + color);
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    mainText.addExtra(textComponent);
                    j += group.length();
                    i = j;
                    continue;
                }
            }


            //av
            if (substringLower.startsWith("av")) {
                Pattern pattern = Pattern.compile("[aA][vV][0-9]+");
                Matcher matcher = pattern.matcher(substring);
                if (matcher.find()) {
                    String group = matcher.group();
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);
                    TextComponent textComponent = new TextComponent(color + "§n" + group + "§r" + color);
                    String url = "https://www.bilibili.com/video/" + group;
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    mainText.addExtra(textComponent);
                    j += group.length();
                    i = j;
                    continue;
                }
            }

            //bv
            if (substringLower.startsWith("bv")) {
                Pattern pattern = Pattern.compile("[bB][vV][0-9a-zA-Z]+");
                Matcher matcher = pattern.matcher(substring);
                if (matcher.find()) {
                    String group = matcher.group();
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);
                    TextComponent textComponent = new TextComponent(color + "§n" + group + "§r" + color);
                    String url = "https://www.bilibili.com/video/" + group;
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    mainText.addExtra(textComponent);
                    j += group.length();
                    i = j;
                    continue;
                }
            }

            //cv
            if (substringLower.startsWith("cv")) {
                Pattern pattern = Pattern.compile("[cC][vV][0-9]+");
                Matcher matcher = pattern.matcher(substring);
                if (matcher.find()) {
                    String group = matcher.group();
                    TextComponent subMessageComponent = new TextComponent(color + message.substring(i, j));
                    mainText.addExtra(subMessageComponent);
                    TextComponent textComponent = new TextComponent(color + "§n" + group + "§r" + color);
                    String url = "https://www.bilibili.com/read/" + group;
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    mainText.addExtra(textComponent);
                    j += group.length();
                    i = j;
                    continue;
                }
            }
            j++;
        }

        mainText.addExtra(color + message.substring(i));
        TextComponent commonFinalText = new TextComponent();

//        if (!channel.equalsIgnoreCase("default")) {
//            commonFinalText.addExtra(color + "[" + channel + "频道]");
//        }

        {
            commonFinalText.addExtra("§r" + color + "<");
            String visitCmd = "/visit " + sourcePlayer.replaceAll("§.", "");
            BaseComponent[] nameComponents = new ComponentBuilder(color + sourcePlayer)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, visitCmd))
                    .create();
            TextComponent nameComponent = new TextComponent(nameComponents);
            commonFinalText.addExtra(nameComponent);
            commonFinalText.addExtra("§r" + color + ">");
            commonFinalText.addExtra(" ");
            commonFinalText.addExtra(mainText);
        }


        TextComponent collectionFinalText = new TextComponent();

//        if (!channel.equalsIgnoreCase("default")) {
//            collectionFinalText.addExtra(color + "[" + channel + "频道]");
//        }

        {
            collectionFinalText.addExtra("§r" + color + "<");
            String visitCmd = "/visit " + sourcePlayer.replaceAll("§.", "");
            BaseComponent[] nameComponents = new ComponentBuilder("§3" + sourcePlayer)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, visitCmd))
                    .create();
            TextComponent nameComponent = new TextComponent(nameComponents);
            collectionFinalText.addExtra(nameComponent);
            collectionFinalText.addExtra("§r" + color + ">");
            collectionFinalText.addExtra(" ");
            collectionFinalText.addExtra(mainText);
        }


        for (ProxiedPlayer player : targetPlayers) {
            Set<String> collections = UniversalChat.collections.get(player.getName());
            if (collections != null && collections.contains(sourcePlayer.replaceAll("§.", ""))) {
                player.sendMessage(ChatMessageType.CHAT, collectionFinalText);
            } else {
                player.sendMessage(ChatMessageType.CHAT, commonFinalText);
            }
        }
        for (ProxiedPlayer notifyPlayer : notifyPlayers) {
            notify(notifyPlayer.getName());
        }
    }

    public static void notify(String playerName) {
        PlaySoundObject playSoundObject = new PlaySoundObject(playerName, "ENTITY_PLAYER_LEVELUP");
        ServerMessageUtils.broadcastBungeeMessage("PlaySound", playSoundObject);
    }

}
