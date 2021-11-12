package com.molean.isletopia.bungee.cirno;

import com.molean.crinobot.CrinoHandler;
import com.molean.crinobot.Robot;
import com.molean.isletopia.bungee.IsletopiaBungee;
import net.mamoe.mirai.message.data.*;
import net.md_5.bungee.api.ProxyServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CirnoHandlerImpl implements CrinoHandler {

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
                subMessage = singleMessage.contentToString();
            } else if (singleMessage instanceof PlainText) {
                subMessage = singleMessage.contentToString();
            } else if (singleMessage instanceof Image) {

                if (singleMessage instanceof net.mamoe.mirai.internal.message.OnlineImage) {
                    String url = ((net.mamoe.mirai.internal.message.OnlineImage) singleMessage).getOriginUrl();
                    subMessage = "{url#" + singleMessage.contentToString() + "#" + url + "}";
                } else {
                    subMessage = singleMessage.contentToString();
                }

            } else if (singleMessage instanceof Voice) {
                String url = ((Voice) singleMessage).getUrl();
                subMessage = "{url#" + singleMessage.contentToString() + "#" + url + "}";
            } else if (singleMessage instanceof Face) {
                subMessage = singleMessage.contentToString();
            } else {
                subMessage = "";
            }
            plainMessage.append(subMessage);
        }
        return plainMessage.toString();
    }


    @Override
    public void handler(long id, String nameCard, String plainMessage, MessageChain messageChain) {
        CirnoUtils.broadcastChat(nameCard, getPlainMessage(messageChain));

        //command handle
        if (plainMessage.startsWith("/")) {
            String command = plainMessage.substring(1);
            String s = CommandHandler.handleCommand(id, command);
            if (s != null) {
                CirnoUtils.broadcastMessage(s);
            }
        }


        ProxyServer.getInstance().getScheduler().runAsync(IsletopiaBungee.getPlugin(), () -> {
            boolean hasAt = false;
            boolean hasQuote = false;
            for (SingleMessage singleMessage : messageChain) {
                if (singleMessage instanceof QuoteReply) {
                    hasQuote = true;
                }
                if (singleMessage instanceof At) {
                    if (((At) singleMessage).getTarget() == Robot.getBot().getId()) {
                        hasAt = true;
                    }
                }
            }
            if (hasAt && !hasQuote) {
                String s = plainMessage.replaceAll("@" + Robot.getBot().getId(), "");
                String response = CirnoUtils.getResponse(id + "", s);
                response = response.replaceAll("小微", "琪露诺");
                CirnoUtils.broadcastMessage(response);
            }
        });


    }
}
