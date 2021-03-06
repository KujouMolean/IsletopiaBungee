package com.molean.isletopia.bungee.cirno;

import com.molean.isletopia.bungee.cirno.command.*;
import com.molean.isletopia.bungee.cirno.command.ban.*;
import com.molean.isletopia.bungee.cirno.command.func.BeaconCommand;
import com.molean.isletopia.bungee.cirno.command.func.ElytraCommand;
import com.molean.isletopia.bungee.cirno.command.func.GiveCommand;
import com.molean.isletopia.bungee.cirno.command.group.*;
import com.molean.isletopia.bungee.cirno.command.info.*;
import com.molean.isletopia.bungee.cirno.command.manage.*;
import com.molean.isletopia.bungee.cirno.command.owner.*;
import com.molean.isletopia.bungee.cirno.command.permission.PermissionCommand;
import com.molean.isletopia.bungee.cirno.command.permission.GrantCommand;
import com.molean.isletopia.bungee.cirno.command.permission.RevokeCommand;

import java.util.Random;

public class CommandsRegister {
    public CommandsRegister() {
        new BanCommand();
        new PardonCommand();
        new BanIpCommand();
        new PardonIpCommand();
        new GrantCommand();
        new RevokeCommand();
        new PermissionCommand();
        new HostNameCommand();
        new ListCommand();
        new GiveCommand();
        new TestCommand();
        new ElytraCommand();
        new BeaconCommand();
        new PlayTimeCommand();
        new TBanCommand();
        new ParameterCommand();
        new IsBanCommand();
        new StatusCommand();
        new KillCommand();
        new AddMember();
        new GGrant();
        new GPermission();
        new GRevoke();
        new Members();
        new RemoveMember();
        new IsOnlineCommand();
        new SetTitle();
        new GrantOP();
        new RevokeOP();
        new Kick();
        new Sudo();
        new MSPT();
        new Rank();
        new Mute();
        new Unmute();
        new Nick();
        new UUIDCommand();
        new Balance();
        new Broadcast();
        new WBan();
        new WPardon();
        new WBanList();
    }
}
