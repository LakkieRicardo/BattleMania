package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TempHandler {
    private static final List<OfflinePlayer> frozen = new ArrayList<>();
    private static final List<Player> vanished = new ArrayList<>();

    public static void handlePlayerLeave(Player player) {

    }

    public static void handlePlayerJoin(Player player) {

    }

    public static synchronized boolean isPlayerFrozen(OfflinePlayer player) {
        return frozen.contains(player);
    }

    public static synchronized void addFrozenPlayer(OfflinePlayer player) {
        frozen.add(player);
    }

    public static synchronized boolean removeFrozenPlayer(OfflinePlayer player) {
        return frozen.remove(player);
    }

    public static synchronized boolean isPlayerVanished(Player player) {
        return vanished.contains(player);
    }

    public static synchronized void addVanishedPlayer(Player player) {
        player.setInvisible(true);
        vanished.add(player);
    }

    public static synchronized boolean removeVanishedPlayer(Player player) {
        player.setInvisible(false);
        return vanished.remove(player);
    }

    public static void handleTogglePlayerFrozen(Player executor, Player target) {
        if (TempHandler.isPlayerFrozen(target)) {
            target.sendMessage(Prefixes.ALERT + "You have been frozen by §c" + executor.getName() + "§f.");
            target.sendMessage(Prefixes.COMMAND + "You froze §c" + target.getName() + "§f.");
            TempHandler.addFrozenPlayer(target);
        } else {
            target.sendMessage(Prefixes.ALERT + "You have been unfrozen by §c" + executor.getName() + "§f.");
            target.sendMessage(Prefixes.COMMAND + "You have unfrozen §c" + target.getName() + "§f.");
            TempHandler.removeFrozenPlayer(target);
        }
    }

    public static void handleToggleOfflinePlayerFrozen(Player executor, OfflinePlayer target) {
        executor.sendMessage(Prefixes.WARNING + "The player you specified, §c" + target.getName() + "§f, is not currently online in this server.");
        executor.sendMessage(Prefixes.WARNING + "This means that they will not be frozen until they join this server.");
        if (TempHandler.isPlayerFrozen(target)) {
            TempHandler.removeFrozenPlayer(target);
            executor.sendMessage(Prefixes.COMMAND + "You unfroze " + target.getName());
        } else {
            TempHandler.addFrozenPlayer(target);
            executor.sendMessage(Prefixes.COMMAND + "You froze " + target.getName());
        }
    }

    public static void handleTogglePlayerVanish(Player target) {
        if (TempHandler.isPlayerVanished(target)) {
            target.sendMessage(Prefixes.COMMAND + "You vanished yourself.");
            TempHandler.addVanishedPlayer(target);
        } else {
            target.sendMessage(Prefixes.COMMAND + "You unvanished yourself.");
            TempHandler.removeVanishedPlayer(target);
        }
    }
}