package com.molean.isletopia.bungee.cirno;

import java.sql.SQLException;
import java.util.List;

public interface BotCommandExecutor {
    String execute(long id, List<String> args) throws Exception;
}
