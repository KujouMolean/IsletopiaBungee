package com.molean.isletopia.bungee.cirno.command.ban;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class TBanCommand implements BotCommandExecutor {
    public TBanCommand() {
        CommandHandler.setExecutor("tban", this);
    }


    private static long parse(String time) {
        int week = 0;
        int year = 0;
        int century = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int month = 0;
        int millionSecond = 0;

        int i = 0;


        while (time.substring(i).matches("[0-9].*")) {

            StringBuilder amountStringBuilder = new StringBuilder();
            while (i < time.length() && Character.isDigit(time.substring(i).charAt(0))) {
                amountStringBuilder.append(time.substring(i).charAt(0));
                i++;
            }


            int amount = Integer.parseInt(amountStringBuilder.toString());


            StringBuilder operatorStringBuilder = new StringBuilder();
            while (i < time.length() && Character.isAlphabetic(time.substring(i).charAt(0))) {
                operatorStringBuilder.append(time.substring(i).charAt(0));
                i++;
            }
            String o = operatorStringBuilder.toString();

            switch (o.toLowerCase(Locale.ROOT)) {
                case "w" -> {
                    week += amount;
                }
                case "c" -> {
                    century += amount;
                }
                case "h" -> {
                    hour += amount;
                }
                case "y" -> {
                    year += amount;
                }
                case "d" -> {
                    day += amount;
                }
                case "m" -> {
                    minute += amount;
                }
                case "s" -> {
                    second += amount;
                }
                case "mon" -> {
                    month += amount;
                }
                case "ms" -> {
                    millionSecond += amount;
                }
            }
        }

        long timeMillis = 0;
        timeMillis += (long) century * 100 * 365 * 24 * 60 * 60 * 1000;
        timeMillis += (long) year * 365 * 24 * 60 * 60 * 1000;
        timeMillis += (long) week * 7 * 24 * 60 * 60 * 1000;
        timeMillis += (long) month * 30 * 24 * 60 * 60 * 1000;
        timeMillis += (long) day * 24 * 60 * 60 * 1000;
        timeMillis += (long) hour * 60 * 60 * 1000;
        timeMillis += (long) minute * 60 * 1000;
        timeMillis += (long) second * 1000;
        timeMillis += millionSecond;

        return timeMillis;
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 2) {
            return "用法: /tban 玩家 时间 原因\n" +
                    "例如: /tban 玩家 3w20c10y3d5m16s200ms 太弱了";
        } else {

            String player = args.get(0);
            String timeString = args.get(1);
            long duration;

            try {
                duration = parse(timeString);
            } catch (Exception e) {
                return "失败，时间解析错误。";
            }
            long l = System.currentTimeMillis() + duration;
            PlayerParameter.set(args.get(0), "isBanned", "true");
            PlayerParameter.set(args.get(0), "pardonTime", l+ "");
            String reason;
            if (args.size() >= 2) {
                ArrayList<String> strings = new ArrayList<>(args);
                strings.remove(0);
                strings.remove(0);
                reason = String.join(" ", strings);
                PlayerParameter.set(args.get(0), "bannedReason", reason);
            } else {
                reason = "";
            }
            LocalDateTime localDateTime = new Timestamp(l).toLocalDateTime();
            localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
            String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(player);
            if (proxiedPlayer != null) {
                proxiedPlayer.disconnect(TextComponent.fromLegacyText("你已被封禁至" + format + "!" + reason));
            }
            return "琪露诺机器人将 " + args.get(0) + " 关禁闭到" + format + " 。";
        }
    }

}
