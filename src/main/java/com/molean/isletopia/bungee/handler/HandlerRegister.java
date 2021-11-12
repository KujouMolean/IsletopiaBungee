package com.molean.isletopia.bungee.handler;

public class HandlerRegister {
    public HandlerRegister() {
        new ServerBumpHandler();
        new CommonResponseHandler();
        new SwitchServerHandler();
    }
}
