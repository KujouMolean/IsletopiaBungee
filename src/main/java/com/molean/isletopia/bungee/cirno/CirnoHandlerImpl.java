package com.molean.isletopia.bungee.cirno;

import com.molean.cirnobot.CirnoHandler;
import com.molean.cirnobot.Robot;
import net.mamoe.mirai.message.data.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CirnoHandlerImpl implements CirnoHandler {

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

    @SuppressWarnings("all")
    public static String getPlainMessage(MessageChain rawMessage) {
        boolean hasQuote = false;
        for (SingleMessage singleMessage : rawMessage) {
            if (singleMessage instanceof QuoteReply) {
                hasQuote = true;
                break;
            }
        }
        StringBuilder plainMessage = new StringBuilder();

        for (SingleMessage singleMessage : rawMessage) {
            String subMessage;
            if (singleMessage instanceof QuoteReply) {
                subMessage = getText((QuoteReply) singleMessage);
            } else if (hasQuote && singleMessage instanceof At && ((At) singleMessage).getTarget() == Robot.getBot().getId()) {
                subMessage = "";
            } else if (singleMessage instanceof At) {
                long target = ((At) singleMessage).getTarget();
                subMessage = "@" + CirnoUtils.getNameCardByQQ(target);

            } else if (singleMessage instanceof PlainText) {
                subMessage = singleMessage.contentToString();
            } else if (singleMessage instanceof Image) {
                if (singleMessage instanceof net.mamoe.mirai.internal.message.OnlineImage) {
                    String url = ((net.mamoe.mirai.internal.message.OnlineImage) singleMessage).getOriginUrl();
                    subMessage = "{{{url#" + singleMessage.contentToString() + "#" + url + "}}}";
                } else {
                    subMessage = singleMessage.contentToString();
                }

            } else if (singleMessage instanceof Voice) {
                String url = ((Voice) singleMessage).getUrl();
                subMessage = "{{{url#" + singleMessage.contentToString() + "#" + url + "}}}";
            } else if (singleMessage instanceof Face) {
                subMessage = singleMessage.contentToString();
            } else {
                String s = singleMessage.contentToString();
                if (s.startsWith("<")) {
                    subMessage = "";
                }else{
                    subMessage = singleMessage.contentToString();
                }

            }
            plainMessage.append(subMessage);
        }
        return plainMessage.toString();
    }


    @Override
    public void handler(long id, String nameCard, String plainMessage, MessageChain messageChain) {
        CirnoUtils.broadcastChat(CirnoUtils.getNameCardByQQ(id), getPlainMessage(messageChain));
        //command handle
        if (plainMessage.startsWith("/")) {
            String command = plainMessage.substring(1);
            String s = CommandHandler.handleCommand(id, command);
            if (s != null) {
                CirnoUtils.broadcastMessage(s);
            }
        }
    }
}
