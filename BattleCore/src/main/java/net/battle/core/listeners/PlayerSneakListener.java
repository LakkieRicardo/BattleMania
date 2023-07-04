package net.battle.core.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.battle.core.command.cmds.SuspectSpectateCommand;

public class PlayerSneakListener implements Listener {
    @EventHandler
    public void onPlayerLeaveSpectate(PlayerToggleSneakEvent e) {
        Player pl = e.getPlayer();

        if (!pl.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (SuspectSpectateCommand.lastLocation.containsKey(pl.getName())) {
            pl.teleport((Location) SuspectSpectateCommand.lastLocation.get(pl.getName()));
            SuspectSpectateCommand.lastLocation.remove(pl.getName());
        }
        if (SuspectSpectateCommand.lastGamemode.containsKey(pl.getName())) {
            pl.setGameMode((GameMode) SuspectSpectateCommand.lastGamemode.get(pl.getName()));
            SuspectSpectateCommand.lastGamemode.remove(pl.getName());
        }
    }

}