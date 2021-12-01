package com.molean.isletopia.bungee.cirno;

import com.molean.isletopia.bungee.cirno.command.*;
import com.molean.isletopia.bungee.cirno.command.ban.*;
import com.molean.isletopia.bungee.cirno.command.func.BeaconCommand;
import com.molean.isletopia.bungee.cirno.command.func.ElytraCommand;
import com.molean.isletopia.bungee.cirno.command.func.GiveCommand;
import com.molean.isletopia.bungee.cirno.command.group.*;
import com.molean.isletopia.bungee.cirno.command.info.HostNameCommand;
import com.molean.isletopia.bungee.cirno.command.info.IsOnlineCommand;
import com.molean.isletopia.bungee.cirno.command.info.ListCommand;
import com.molean.isletopia.bungee.cirno.command.info.PlayTimeCommand;
import com.molean.isletopia.bungee.cirno.command.manage.KillCommand;
import com.molean.isletopia.bungee.cirno.command.manage.ParameterCommand;
import com.molean.isletopia.bungee.cirno.command.manage.StatusCommand;
import com.molean.isletopia.bungee.cirno.command.owner.GrantOP;
import com.molean.isletopia.bungee.cirno.command.owner.Kick;
import com.molean.isletopia.bungee.cirno.command.owner.RevokeOP;
import com.molean.isletopia.bungee.cirno.command.owner.SetTitle;
import com.molean.isletopia.bungee.cirno.command.permission.PermissionCommand;
import com.molean.isletopia.bungee.cirno.command.permission.GrantCommand;
import com.molean.isletopia.bungee.cirno.command.permission.RevokeCommand;

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
        new DisableRegisterCommand();
        new SetTitle();
        new GrantOP();
        new RevokeOP();
        new Kick();
    }
}
