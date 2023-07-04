package net.battle.test.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class FadeListener
        implements Listener {
    private static final boolean enabled = true; // TODO figure out what this should be set to

    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        if (enabled) {
            e.setCancelled(true);
        }
    }
}