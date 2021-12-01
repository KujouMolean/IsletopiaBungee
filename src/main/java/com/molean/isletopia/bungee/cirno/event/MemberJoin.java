package com.molean.isletopia.bungee.cirno.event;

import com.molean.cirnobot.Robot;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class MemberJoin extends SimpleListenerHost {
    private boolean registered = false;
    public MemberJoin() {
        ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(BungeeRelatedUtils.getPlugin(), () -> {
            if (!registered && Robot.getBot() != null) {
                CirnoUtils.registerListener(this);
                registered = true;
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onMemberJoinRequest(MemberJoinRequestEvent event) {
        String message = event.getMessage();
        if (message.contains("梦幻之屿")) {
            event.accept();
        } else {
            event.reject(false, "答案错误");
        }
    }
}
