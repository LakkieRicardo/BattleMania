package net.battle.core.assets.hats.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.battle.core.assets.hats.Hat;

public class HatLeaveListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Hat.dequip(e.getPlayer());
    }
}