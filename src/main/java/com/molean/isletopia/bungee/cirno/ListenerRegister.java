package com.molean.isletopia.bungee.cirno;

import com.molean.isletopia.bungee.cirno.command.owner.Mute;
import com.molean.isletopia.bungee.cirno.command.owner.Nick;
import com.molean.isletopia.bungee.cirno.command.owner.Unmute;
import com.molean.isletopia.bungee.cirno.event.GroupMessage;
import com.molean.isletopia.bungee.cirno.event.MemberJoin;

public class ListenerRegister {
    public ListenerRegister() {
        new GroupMessage();
        new MemberJoin();

    }
}
