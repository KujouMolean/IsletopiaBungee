package com.molean.isletopia.bungee.cirno.command.manage;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import com.molean.isletopia.bungee.cirno.PermissionHandler;
import com.molean.isletopia.bungee.parameter.ContactParameter;
import com.molean.isletopia.bungee.parameter.GroupParameter;
import com.molean.isletopia.bungee.parameter.HostNameParameter;
import com.molean.isletopia.bungee.parameter.PlayerParameter;
import com.molean.isletopia.shared.service.UniversalParameter;
import com.molean.isletopia.shared.utils.UUIDUtils;

import java.util.List;
import java.util.Locale;

public class ParameterCommand implements BotCommandExecutor {
    public ParameterCommand() {
        CommandHandler.setExecutor("parameter", this);
    }

    @Override
    public String execute(long id, List<String> args) {
        if (args.size() < 4) {
            return "用法: /parameter item target operator key [value]";
        }
        String item = args.get(0);
        String target = args.get(1);
        String operator = args.get(2);
        String key = args.get(3);

        switch (item.toLowerCase(Locale.ROOT)) {
            case "player"->{
                switch (operator.toLowerCase(Locale.ROOT)) {
                    case "set"->{
                        if (args.size() < 5) {
                            return "用法: /parameter item target set key value";
                        }else{
                            String value = args.get(4);
                            PlayerParameter.set(target, key, value);
                            return "OK";
                        }
                    }
                    case "view"->{
                        String s = PlayerParameter.get(target, key);
                        return s == null ? "null" : s;
                    }
                    case "unset"->{
                        PlayerParameter.unset(target, key);
                        return "OK";
                    }
                }
            }

            case "hostname"->{
                switch (operator.toLowerCase(Locale.ROOT)) {
                    case "set"->{
                        if (args.size() < 5) {
                            return "用法: /parameter item target set key value";
                        }else{
                            String value = args.get(4);
                            HostNameParameter.set(target, key, value);
                            return "OK";
                        }
                    }
                    case "view"->{
                        String s = HostNameParameter.get(target, key);
                        return s == null ? "null" : s;
                    }
                    case "unset"->{
                        HostNameParameter.unset(target, key);
                        return "OK";
                    }
                }

            }
            case "contact"->{

                if (target.startsWith("@")) {
                    target = target.substring(1);
                }

                long l;
                try {
                    l = Long.parseLong(target);
                } catch (Exception e) {
                    return "用法: /parameter item targetID set key value";
                }


                switch (operator.toLowerCase(Locale.ROOT)) {
                    case "set"->{
                        if (args.size() < 5) {
                            return "用法: /parameter item target set key value";
                        }else{
                            String value = args.get(4);
                            ContactParameter.set(l, key, value);
                            return "OK";
                        }
                    }
                    case "view"->{
                        String s = ContactParameter.get(l, key);
                        return s == null ? "null" : s;
                    }
                    case "unset"->{
                        ContactParameter.unset(l, key);
                        return "OK";
                    }
                }

            }
            case "group"->{
                switch (operator.toLowerCase(Locale.ROOT)) {
                    case "set"->{
                        if (args.size() < 5) {
                            return "用法: /parameter item target set key value";
                        }else{
                            String value = args.get(4);
                            GroupParameter.set(target, key, value);
                            return "OK";
                        }
                    }
                    case "view"->{
                        String s = GroupParameter.get(target, key);
                        return s == null ? "null" : s;
                    }
                    case "unset"->{
                        GroupParameter.unset(target, key);
                        return "OK";
                    }
                }
            }
            case "universal"->{
                switch (operator.toLowerCase(Locale.ROOT)) {
                    case "set"->{
                        if (args.size() < 5) {
                            return "用法: /parameter item target set key value";
                        }else{
                            String value = args.get(4);
                            UniversalParameter.setParameter(UUIDUtils.get(target), key, value);

                            return "OK";
                        }
                    }
                    case "view"->{
                        String s = UniversalParameter.getParameter(UUIDUtils.get(target), key);
                        return s == null ? "null" : s;
                    }
                    case "unset"->{
                        UniversalParameter.unsetParameter(UUIDUtils.get(target), key);
                        return "OK";
                    }
                }
            }
        }

        return "用法: /parameter item target operator key [value]";
    }
}
