package net.battle.core.assets.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMMacro;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.assets.hats.gui.HatInventoryBuilder;
import net.battle.core.assets.particle.gui.ParticleInventoryBuilder;

public class AssetInventoryListener implements Listener {
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
        if (BMMacro.CTS.serialize(inv.title()).equalsIgnoreCase("Assets")) {
            e.setCancelled(true);
            if (item.getType() == Material.GHAST_TEAR) {
                pl.closeInventory();
                pl.openInventory(Gadget.GADGETS_INV);
                return;
            }
            if (item.getType() == Material.PLAYER_HEAD) {
                pl.closeInventory();
                pl.openInventory(HatInventoryBuilder.getInventory());
                return;
            }
            if (item.getType() == Material.REDSTONE) {
                pl.closeInventory();
                pl.openInventory(ParticleInventoryBuilder.create(0, pl.getUniqueId()));
                return;
            }
        }
    }
}