package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TempHandler {
    private static final List<Player> frozen = new ArrayList<>();
    private static final List<Player> vanished = new ArrayList<>();

    public static void handlePlayerLeave(Player player) {

    }

    public static void handlePlayerJoin(Player player) {

    }

    public static synchronized boolean isPlayerFrozen(Player player) {
        return frozen.contains(player);
    }

    public static synchronized void addFrozenPlayer(Player player) {
        frozen.add(player);
    }

    public static synchronized boolean removeFrozenPlayer(Player player) {
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

    // TODO implement these methods
    public static boolean getOfflinePlayerFrozen(OfflinePlayer plOffline) {
        return false;
    }

    public static void setOfflinePlayerFrozen(OfflinePlayer plOffline, boolean b) {
    }

    public static void handleTogglePlayerFrozen(Player executor, Player target) {
        if (TempHandler.isPlayerFrozen(target)) {
            target.sendMessage("§e§lALERT§8 > §fYou have been frozen by §c" + executor.getName() + "§f.");
            target.sendMessage("§9§lCOMMAND§8 > §fYou froze §c" + target.getName() + "§f.");
            TempHandler.addFrozenPlayer(target);
        } else {
            target.sendMessage("§e§lALERT§8 > §fYou have been unfrozen by §c" + executor.getName() + "§f.");
            target.sendMessage("§9§lCOMMAND§8 > §fYou have unfrozen §c" + target.getName() + "§f.");
            TempHandler.removeFrozenPlayer(target);
        }
    }

    // TODO: should this even be allowed?
    public static void handleToggleOfflinePlayerFrozen(Player executor, OfflinePlayer target) {
        // TODO: explain what this means?
        executor.sendMessage("§6§lWARNING§8 > §fThe player you specified, §c" + target.getName()
                + "§f, is not currently online in this server.");
        if (TempHandler.getOfflinePlayerFrozen(target)) {
            TempHandler.setOfflinePlayerFrozen(target, false);
            executor.sendMessage("§9§lCOMMAND§8 > §fYou unfroze " + target.getName());
        } else {
            TempHandler.setOfflinePlayerFrozen(target, false);
            executor.sendMessage("§9§lCOMMAND§8 > §fYou froze " + target.getName());
        }
    }

    public static void handleTogglePlayerVanish(Player target) {
        if (TempHandler.isPlayerVanished(target)) {
            target.sendMessage("§9§lCOMMAND§8 > §fYou vanished yourself.");
            TempHandler.addVanishedPlayer(target);
        } else {
            target.sendMessage("§9§lCOMMAND§8 > §fYou unvanished yourself.");
            TempHandler.removeVanishedPlayer(target);
        }
    }
}