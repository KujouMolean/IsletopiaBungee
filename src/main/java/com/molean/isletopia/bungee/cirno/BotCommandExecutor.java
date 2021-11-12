package com.molean.isletopia.bungee.cirno;

import java.util.List;

public interface BotCommandExecutor {
    String execute(long id, List<String> args);
}
