package com.molean.isletopia.bungee.cirno.command.info;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.shared.database.MSPTDao;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;

import java.sql.SQLException;
import java.util.List;

public class Balance implements BotCommandExecutor {
    public Balance() {
        CommandHandler.setExecutor("balance", this);
    }

    @Override
    public String execute(long id, List<String> args) throws Exception {
        StringBuilder result = new StringBuilder();
        for (String server : PlatformRelatedUtils.getInstance().getIslandServers()) {
            if (server.startsWith("server")) {
                double v = -1;
                try {
                    v = MSPTDao.queryRecent7Days(server);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (v > 0) {
                    result.append(server.replace("server", "")).append("(%.4f) ".formatted(v));
                } else {
                    result.append(server.replace("server", "")).append("(未知) ");
                }
            }
        }
        return result.toString();
    }
}
