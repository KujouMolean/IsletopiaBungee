package com.molean.isletopia.bungee.cirno.command.info;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.shared.database.PlayTimeStatisticsDao;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import com.molean.isletopia.shared.pojo.req.PlayTimeRequest;
import com.molean.isletopia.shared.utils.UUIDUtils;

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
        long recent30d = PlayTimeStatisticsDao.getRecentPlayTime(uuid, l - 30L * 24 * 60 * 60 * 1000);
        long recent7d = PlayTimeStatisticsDao.getRecentPlayTime(uuid, l - 7L * 24 * 60 * 60 * 1000);
        long recent3d = PlayTimeStatisticsDao.getRecentPlayTime(uuid, l - 3L * 24 * 60 * 60 * 1000);
        recent30d /= 1000 * 60 * 60;
        recent7d /= 1000 * 60 * 60;
        recent3d /= 1000 * 60 * 60;
        return player + " 最近游戏情况: " +
                "3天内" + recent3d + "小时, " +
                "7天内" + recent7d + "小时, " +
                "30天内" + recent30d + "小时.";
    }

}
