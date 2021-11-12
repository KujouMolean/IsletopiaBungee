package com.molean.isletopia.bungee.cirno.command.info;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.shared.message.ServerMessageUtils;
import com.molean.isletopia.shared.pojo.req.PlayTimeRequest;

import java.util.List;

public class PlayTimeCommand implements BotCommandExecutor {

    public PlayTimeCommand() {
        CommandHandler.setExecutor("playtime", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 1) {
            return "/playtime [player] [reason]";
        }
        PlayTimeRequest playTimeRequest = new PlayTimeRequest();
        playTimeRequest.setPlayer(args.get(0));
        ServerMessageUtils.sendServerBungeeMessage("server1", "PlayTimeRequest", playTimeRequest);

        return null;
    }

}
