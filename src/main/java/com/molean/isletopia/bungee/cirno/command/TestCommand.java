package com.molean.isletopia.bungee.cirno.command;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;

import java.util.List;

public class TestCommand implements BotCommandExecutor {
    public TestCommand() {
        CommandHandler.setExecutor("test", this);
    }




    @Override
    public String execute(long id, List<String> args) {

        return null;
    }

}
