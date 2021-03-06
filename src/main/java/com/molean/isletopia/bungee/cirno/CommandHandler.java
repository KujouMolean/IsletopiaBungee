package com.molean.isletopia.bungee.cirno;

import java.util.*;

public class CommandHandler {

    private static final Map<String, BotCommandExecutor> executorMap = new HashMap<>();

    public static void setExecutor(String command, BotCommandExecutor executor) {
        executorMap.put(command.toLowerCase(Locale.ROOT), executor);
    }

    public static String handleCommand(long id, String fullCommand) {

        /*pre handle start*/
        List<String> args = new ArrayList<>(Arrays.asList(fullCommand.split(" ")));
        for (int i = 0; i < args.size(); i++) {
            args.set(i, args.get(i).trim());
        }

        args.removeIf(String::isEmpty);

        if (args.size() == 0) {
            return null;
        }

        String command = args.get(0);
        args.remove(0);

        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).startsWith("@")) {
                args.set(i, args.get(i).substring(1));
            }
        }

        /*pre handle end*/

        BotCommandExecutor botCommandExecutor = executorMap.get(command.toLowerCase(Locale.ROOT));

        if (botCommandExecutor == null) {
            return null;
        }

        if (!PermissionHandler.hasPermission(command.toLowerCase(Locale.ROOT), id)) {
            return "琪露诺不听你的!";
        }
        try {
            return botCommandExecutor.execute(id, args);
        } catch (Exception e) {
            e.printStackTrace();
            return "琪露诺停止了思考。";
        }

    }
}
