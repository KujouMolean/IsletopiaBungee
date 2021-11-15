package com.molean.isletopia.bungee.cirno.command.ban;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.bungee.parameter.PlayerParameter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IsBanCommand implements BotCommandExecutor {
    public IsBanCommand() {
        CommandHandler.setExecutor("isban", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "用法: /isban 玩家";
        } else {
            String player = args.get(0);

            String isBanned = PlayerParameter.get(args.get(0), "isBanned");
            String pardonTime = PlayerParameter.get(args.get(0), "pardonTime");
            String bannedReason = PlayerParameter.get(args.get(0), "bannedReason");
            if ("true".equals(isBanned)) {
                if (pardonTime != null && !pardonTime.isEmpty()) {
                    long l = Long.parseLong(pardonTime);

                    LocalDateTime localDateTime = new Timestamp(l).toLocalDateTime();
                    localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
                    String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    if (bannedReason != null && !bannedReason.isEmpty()) {

                        return player + "已被封禁至" + format + "。理由：" + bannedReason;
                    } else {
                        return player + "已被封禁至" + format + "。";
                    }


                } else {
                    if (bannedReason != null && !bannedReason.isEmpty()) {

                        return player + "已被永久封禁。理由：" + bannedReason;
                    } else {
                        return player + "已被永久封禁。";
                    }

                }
            } else {
                return player + "未封禁";
            }
        }


    }


}
