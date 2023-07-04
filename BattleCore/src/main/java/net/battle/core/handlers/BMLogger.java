package net.battle.core.handlers;

import org.bukkit.Bukkit;

public class BMLogger {

    public static void info(String row) {
        Bukkit.getLogger().info(row);
    }

    public static void warning(String row) {
        Bukkit.getLogger().warning(row);
    }

    public static void severe(String row) {
        Bukkit.getLogger().severe(row);
    }
}