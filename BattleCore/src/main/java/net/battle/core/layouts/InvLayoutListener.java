package net.battle.core.layouts;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InvLayoutListener implements Listener {
    
    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent event) {
        InvLayout.registerInventoryClosed(event.getInventory());
    }

}
