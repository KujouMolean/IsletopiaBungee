package com.molean.isletopia.bungee.cirno.command.info;

import com.google.gson.JsonParser;
import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.shared.database.PlayerStatsDao;
import com.molean.isletopia.shared.utils.UUIDUtils;

import java.sql.SQLException;
import java.util.*;

public class Rank implements BotCommandExecutor {
    public Rank() {
        CommandHandler.setExecutor("rank", this);
    }

    @Override
    public String execute(long id, List<String> args) throws SQLException {
        //rank minecraft:custom minecraft:play_time
        if (args.size() < 2) {
            return "/rank [type] [item] ([player])";
        }

        Map<UUID, String> map = PlayerStatsDao.queryAll();
        HashMap<UUID, Integer> records = new HashMap<>();

        JsonParser jsonParser = new JsonParser();
        map.forEach((uuid, stats) -> {
            int record = 0;
            try {
                record = jsonParser.parse(stats).getAsJsonObject()
                        .get("stats").getAsJsonObject()
                        .get("minecraft:" + args.get(0)).getAsJsonObject()
                        .get("minecraft:" + args.get(1)).getAsInt();
            } catch (Exception ignored) {
            }
            if (record > 0) {
                records.put(uuid, record);
            }
        });
        if (records.size() == 0) {
            return "笨蛋，你确定有" + args.get(0) + "-" + args.get(1) + "这个统计吗?";
        }


        PriorityQueue<UUID> priorityQueue = new PriorityQueue<>((o1, o2) -> records.get(o2) - records.get(o1));
        priorityQueue.addAll(records.keySet());

        if (args.size() > 2) {
            String s = args.get(2);
            UUID uuid = UUIDUtils.get(s);
            if (uuid == null) {
                return "不存在名为" + s + "的玩家";
            }
            int rank = 0;
            int count = 1;
            while (!priorityQueue.isEmpty()) {
                if (uuid.equals(priorityQueue.poll())) {
                    rank = count;
                    break;
                }
                count++;
            }
            if (rank > 0) {
                return "玩家%s以%d的成绩在%s-%s中位列第%d名。超越了%.2f%%的玩家".formatted(s, records.get(uuid), args.get(0), args.get(1), rank, (records.size() - rank + 1) * 100.0 / records.size());
            } else {
                return "未找到相关排名信息。";
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < priorityQueue.size() && i < 20; i++) {
            UUID poll = priorityQueue.poll();
            if (poll == null) {
                continue;
            }
            String name = UUIDUtils.get(poll);
            if (name == null) {
                continue;
            }
            stringBuilder.append("%s(%d) ".formatted(name, records.get(poll)));
        }

        return stringBuilder.toString();
    }

}
