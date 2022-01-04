package com.molean.isletopia.bungee.cirno.command.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.shared.database.PlayTimeStatisticsDao;
import com.molean.isletopia.shared.database.PlayerStatsDao;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import com.molean.isletopia.shared.pojo.req.PlayTimeRequest;
import com.molean.isletopia.shared.utils.UUIDUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PlayTimeCommand implements BotCommandExecutor {

    public PlayTimeCommand() {
        CommandHandler.setExecutor("playtime", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "/playtime [player] [reason]";
        }
        String player = args.get(0);
        long l = System.currentTimeMillis();
        UUID uuid = UUIDUtils.get(player);
        if (uuid == null) {
            return "不存在名为" + player + "的玩家";
        }
        long recent30d = PlayTimeStatisticsDao.getRecentPlayTime(uuid, l - 30L * 24 * 60 * 60 * 1000);
        long recent7d = PlayTimeStatisticsDao.getRecentPlayTime(uuid, l - 7L * 24 * 60 * 60 * 1000);
        long recent3d = PlayTimeStatisticsDao.getRecentPlayTime(uuid, l - 3L * 24 * 60 * 60 * 1000);
        recent30d /= 1000 * 60 * 60;
        recent7d /= 1000 * 60 * 60;
        recent3d /= 1000 * 60 * 60;
        String result = player + " 最近游戏情况: " +
                "3天内" + recent3d + "小时, " +
                "7天内" + recent7d + "小时, " +
                "30天内" + recent30d + "小时.";

        try {
            if (PlayerStatsDao.exist(uuid)) {
                String s = PlayerStatsDao.queryForce(uuid);
                JsonElement parse = new JsonParser().parse(s);
                int playtime = parse.getAsJsonObject().get("stats").getAsJsonObject()
                        .get("minecraft:custom").getAsJsonObject()
                        .get("minecraft:play_time").getAsInt();
                if (playtime > 0) {
                    result = result + " 累计时长" + (playtime / (20 * 60 * 60)) + "小时.";
                }
            }
        } catch (Exception ignored) {
        }
        return result;
    }

}
