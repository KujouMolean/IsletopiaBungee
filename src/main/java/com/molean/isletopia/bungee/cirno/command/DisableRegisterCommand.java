package com.molean.isletopia.bungee.cirno.command;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.individual.PlayerRegister;

import java.util.List;

public class DisableRegisterCommand implements BotCommandExecutor {
    public DisableRegisterCommand() {
        CommandHandler.setExecutor("toggleregister", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (PlayerRegister.isAllowRegister()) {
            PlayerRegister.setAllowRegister(false);
            return "服务器将禁止离线玩家注册，直到下一次服务器重启。";
        } else {
            return "服务器将允许离线玩家注册。";
        }

    }
}
