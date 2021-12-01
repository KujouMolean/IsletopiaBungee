package com.molean.isletopia.bungee.cirno.command;

import com.molean.cirnobot.Robot;
import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;

import java.util.List;

public class TestCommand implements BotCommandExecutor {
    public TestCommand() {
        CommandHandler.setExecutor("test", this);
    }


    @Override
    public String execute(long id, List<String> args) {
        Group group = Robot.getBot().getGroup(11L);
        assert group != null;
        NormalMember botAsMember = group.getBotAsMember();

        int joinTimestamp = botAsMember.getJoinTimestamp();
        int lastSpeakTimestamp = botAsMember.getLastSpeakTimestamp();
        return null;
    }

}
