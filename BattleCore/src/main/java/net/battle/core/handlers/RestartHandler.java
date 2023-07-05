package net.battle.core.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;

public class RestartHandler {
    public static void startTimer() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, () -> lastMinute3(), 20 * 60 * 60 * 2); // Ticks, seconds, minutes, hours
    }

    private static void lastMinute3() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(Prefixes.ALERT + "Restarting server in 3 minutes");
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, () -> lastMinute2(), 1200L);
    }

    private static void lastMinute2() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(Prefixes.ALERT + "Restarting server in 2 minutes");
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, () -> lastMinute1(), 1200L);
    }

    private static void lastMinute1() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(Prefixes.ALERT + "Restarting server in 1 minute");
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, () -> restartServerWarn(), 1200L);
    }

    private static void restartServerWarn() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(Prefixes.ALERT + "Restarting the server in 10 seconds");
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, () -> restartServer(), 200L);
    }

    private static void restartServer() {
        Bukkit.shutdown();
    }
}