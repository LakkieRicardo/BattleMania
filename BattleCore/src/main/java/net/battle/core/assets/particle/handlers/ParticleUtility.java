package net.battle.core.assets.particle.handlers;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParticleUtility implements Listener {
    private static Map<String, Long> lastMove = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Location last = e.getFrom();
        Location next = e.getTo();
        if (last.getWorld() == next.getWorld() && last.getX() == next.getX() && last.getY() == next.getY() && last.getZ() == next.getZ()) {
            return;
        }
        lastMove.put(e.getPlayer().getUniqueId().toString(), Long.valueOf(System.currentTimeMillis()));
    }

    public static boolean hasPlayerMoved(OfflinePlayer pl, long deltaMillis) {
        if (pl == null || !lastMove.containsKey(pl.getUniqueId().toString())) {
            return false;
        }
        return (System.currentTimeMillis() - deltaMillis <= ((Long) lastMove.get(pl.getUniqueId().toString())).longValue());
    }

    public static boolean hasPlayerMoved(OfflinePlayer pl) {
        return hasPlayerMoved(pl, 300L);
    }
}