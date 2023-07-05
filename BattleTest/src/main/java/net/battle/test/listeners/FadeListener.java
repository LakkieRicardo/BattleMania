package net.battle.test.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

import net.battle.test.BMTestPlugin;

public class FadeListener implements Listener {

    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        if (!BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_FADE)) {
            e.setCancelled(true);
        }
    }
}