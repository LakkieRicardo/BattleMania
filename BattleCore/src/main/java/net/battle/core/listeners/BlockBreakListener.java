package net.battle.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.battle.core.BMCorePlugin;
import net.battle.core.command.cmds.GamemodeCommand;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (BMCorePlugin.ACTIVE_PLUGIN.getAllowBlockBreak() && !GamemodeCommand.isPlayerGameMode(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (BMCorePlugin.ACTIVE_PLUGIN.getAllowBlockBreak() && !GamemodeCommand.isPlayerGameMode(e.getPlayer()))
            e.setCancelled(true);
    }
}