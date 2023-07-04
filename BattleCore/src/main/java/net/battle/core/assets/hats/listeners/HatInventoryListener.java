package net.battle.core.assets.hats.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMMacro;
import net.battle.core.assets.hats.Hat;
import net.battle.core.handlers.InventoryUtils;

public class HatInventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e == null) {
            return;
        }
        if (e.getInventory() == null) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        Player pl = (Player) e.getWhoClicked();
        InventoryView inv = e.getView();
        ItemStack item = e.getCurrentItem();
        if (BMMacro.CTS.serialize(inv.title()).equals("Hats")) {
            e.setCancelled(true);
            if (item.getType() == Material.BARRIER) {
                Hat.dequip(pl);
                pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5.0F, 0.5F);
                pl.closeInventory();
                return;
            }
            for (Hat hat : Hat.hats) {
                if (InventoryUtils.isItemSimilarTo(hat.getMadeItem(), item)) {
                    hat.equip(pl);
                    pl.closeInventory();
                    pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5.0F, 0.5F);
                    return;
                }
            }
        }
    }
}