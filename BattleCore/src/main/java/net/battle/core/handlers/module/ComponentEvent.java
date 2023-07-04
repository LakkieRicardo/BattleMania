package net.battle.core.handlers.module;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.ServerEvent;

import net.battle.core.BMCorePlugin;

public class ComponentEvent extends Component implements Listener {
    public ComponentEvent(ComponentType type, Module parent) {
        super(type, parent);
    }

    @EventHandler
    public void onPlayer(PlayerEvent event) {
        if (getParent().getRunnable() != null) {
            getParent().getRunnable().onPlayerEventFire(event);
        }
    }

    @EventHandler
    public void onEvent(Event event) {
        if (getParent().getRunnable() != null) {
            getParent().getRunnable().onEventFire(event);
        }
    }

    @EventHandler
    public void onEntity(EntityEvent event) {
        if (getParent().getRunnable() != null) {
            getParent().getRunnable().onEntityEventFire(event);
        }
    }

    @EventHandler
    public void onInventory(InventoryEvent event) {
        if (getParent().getRunnable() != null) {
            getParent().getRunnable().onInventoryEventFire(event);
        }
    }

    @EventHandler
    public void onServer(ServerEvent event) {
        if (getParent().getRunnable() != null) {
            getParent().getRunnable().onServerEventFire(event);
        }
    }

    public void register() {
        if (getParent().getRunnable() != null) {
            Bukkit.getPluginManager().registerEvents(this, BMCorePlugin.ACTIVE_PLUGIN);
        }
    }
}