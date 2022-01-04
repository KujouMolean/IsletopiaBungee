package com.molean.isletopia.bungee.cirno.command.manage;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.shared.database.MSPTDao;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;

import java.sql.SQLException;
import java.util.List;

public class MSPT implements BotCommandExecutor {
    public MSPT() {
        CommandHandler.setExecutor("mspt", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "/mspt [server|servers] ";
        }

        String serverName = args.get(0);
        if ("servers".equals(serverName)) {
            double sum = 0;
            int count = 0;
            StringBuilder result = new StringBuilder();
            for (String server : PlatformRelatedUtils.getInstance().getIslandServers()) {
                if (server.startsWith("server")) {
                    double v = -1;
                    try {
                        v = MSPTDao.queryLastMSPT(server);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if (v > 0) {
                        sum+=v;
                        count++;
                        result.append(server).append("(%.2f) ".formatted(v));
                    } else {
                        result.append(server).append("(未知) ");
                    }
                }
            }
            if (count != 0) {
                return result + "\n" + ("平均值:%.2f").formatted(sum / count);
            } else {
                return result.toString();
            }
        } else {
            try {
                double v = MSPTDao.queryLastMSPT(serverName);
                if (v > 0) {
                    return (serverName + "的MSPT为%.2f").formatted(v);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "查不到" + serverName + "的mspt";
        }
    }
}

