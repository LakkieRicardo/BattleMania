package net.battle.core.assets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMCorePlugin;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.assets.hats.gui.HatInventoryBuilder;
import net.battle.core.assets.particle.BMParticle;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.ItemStackBuilder;
import net.kyori.adventure.text.Component;

public class AssetHandler {
    
    private static final ItemStack item = (new ItemStackBuilder(Material.CHEST)).withName("§aAssets").build();

    public static void init() {
        InventoryUtils.fillBlanks(Gadget.GADGETS_INV);
        HatInventoryBuilder.createInventory();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BMCorePlugin.ACTIVE_PLUGIN, () -> BMParticle.update(), 1L, 1L);
    }

    public static Inventory getAssetInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Assets"));
        InventoryUtils.setItem(inv, 2, 1, (new ItemStackBuilder(Material.GHAST_TEAR)).withName("§aGadgets").build());
        InventoryUtils.setItem(inv, 4, 1, (new ItemStackBuilder(Material.PLAYER_HEAD)).withName("§aHats").build());
        InventoryUtils.setItem(inv, 6, 1, (new ItemStackBuilder(Material.REDSTONE)).withName("§aParticles").build());
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static ItemStack getAssetItem() {
        return item;
    }
}