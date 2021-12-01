package com.molean.isletopia.bungee.cirno.event;

import com.molean.cirnobot.CirnoBot;
import com.molean.cirnobot.Robot;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.shared.platform.BungeeRelatedUtils;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMessage extends SimpleListenerHost {
    private boolean registered = false;
    public GroupMessage() {
        ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(BungeeRelatedUtils.getPlugin(), () -> {
            if (!registered && Robot.getBot() != null) {
                CirnoUtils.registerListener(this);
                registered = true;
            }
        }, 1, 1, TimeUnit.SECONDS);


    }
    private static String getText(QuoteReply quoteReply) {
        MessageChain originalMessage = quoteReply.getSource().getOriginalMessage();
        String originText = originalMessage.contentToString();
        Pattern pattern = Pattern.compile("<([a-zA-Z0-9_]{3,18})> .*");
        Matcher matcher = pattern.matcher(originText);
        if (matcher.matches()) {
            String group = matcher.group(1);
            System.out.println(group);
            return "@" + group + " ";
        } else {
            return quoteReply.contentToString();
        }
    }

    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (event.getGroup().getId() != 483653595) {
            return;
        }
        if (Calendar.getInstance().getTimeInMillis() / 1000 - event.getTime() > 30) {
            return;
        }

        boolean hasQuote = false;
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof QuoteReply) {
                hasQuote = true;
                break;
            }
        }

        String nameCard = event.getSender().getNameCard();
        long id = event.getSender().getId();
        StringBuilder plainMessage = new StringBuilder();
        MessageChain rawMessage = event.getMessage();
        for (SingleMessage singleMessage : rawMessage) {
            String subMessage;
            if (singleMessage instanceof QuoteReply) {
                subMessage = getText((QuoteReply) singleMessage);
            } else if (hasQuote && singleMessage instanceof At && ((At) singleMessage).getTarget() == Robot.getBot().getId()) {
                subMessage = "";
            } else if (singleMessage instanceof At) {
                subMessage = singleMessage.contentToString();
            } else if (singleMessage instanceof PlainText) {
                subMessage = singleMessage.contentToString();
            } else if (singleMessage instanceof Image) {
                subMessage = singleMessage.contentToString();
            } else if (singleMessage instanceof Voice) {
                subMessage = singleMessage.contentToString();
            } else if (singleMessage instanceof Face) {
                subMessage = singleMessage.contentToString();
            } else {
                subMessage = "";
            }
            plainMessage.append(subMessage);
        }
        if (CirnoBot.getCirnoHandler() != null) {
            CirnoBot.getCirnoHandler().handler(id, nameCard, plainMessage.toString(), event.getMessage());
        }
    }
}
