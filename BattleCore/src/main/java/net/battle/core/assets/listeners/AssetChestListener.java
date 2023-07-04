package net.battle.core.assets.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.ItemStackBuilder;

public class AssetChestListener implements Listener {
    public static final ItemStack assetItem = (new ItemStackBuilder(Material.CHEST)).withName("§aAssets").build();

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        ItemStack handItem = pl.getInventory().getItemInMainHand();
        if (handItem == null || handItem.getType() == Material.AIR) {
            return;
        }
        if (InventoryUtils.isItemSimilarTo(assetItem, handItem)) {
            e.setCancelled(true);
            pl.chat("/asset");
            return;
        }
    }
}