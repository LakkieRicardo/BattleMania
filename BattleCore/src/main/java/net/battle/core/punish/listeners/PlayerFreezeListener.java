package net.battle.core.punish.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.TempHandler;

public class PlayerFreezeListener implements Listener {

    public static final long MESSAGE_COOLDOWN_MS = TimeUnit.SECONDS.toMillis(2L);
    public Map<UUID, Long> cooldown = new HashMap<>();
    public List<String> msg = new ArrayList<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();
        Location from = player.getLocation();

        if (to.getX() == from.getX() && to.getY() == from.getY() && to.getZ() == from.getZ()) {
            return;
        }

        if (TempHandler.isPlayerFrozen(player)) {
            e.setCancelled(true);
            long nowMs = System.currentTimeMillis();
            if (!cooldown.containsKey(player.getUniqueId())
                    || cooldown.get(player.getUniqueId()) < nowMs + MESSAGE_COOLDOWN_MS) {

                player.sendMessage(Prefixes.ERROR + "You are frozen, you cannot move");
                this.cooldown.put(player.getUniqueId(), nowMs);
            }
        }
    }
}