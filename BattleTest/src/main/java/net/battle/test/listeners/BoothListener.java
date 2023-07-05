package net.battle.test.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.battle.core.handlers.Prefixes;
import net.battle.test.handlers.Booth;

public class BoothListener implements Listener {
    @EventHandler
    public void onMoveIntoReservedBooth(PlayerMoveEvent e) {
        Player pl = e.getPlayer();
        for (Booth booth : Booth.BOOTHS) {
            if (booth.getRegion().isInside(pl) && booth.isOccupied() && !booth.getInvited().contains(pl.getName())) {
                e.setCancelled(true);
                pl.sendMessage(Prefixes.ALERT + "This booth is currently reserved!");
            }
        }
    }
}
