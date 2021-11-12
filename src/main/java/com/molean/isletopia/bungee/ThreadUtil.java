package com.molean.isletopia.bungee;

public class ThreadUtil {
    public static void sleepForSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void sleepInMillis(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
