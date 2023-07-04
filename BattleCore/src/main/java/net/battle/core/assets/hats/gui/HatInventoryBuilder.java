package net.battle.core.assets.hats.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.hats.Hat;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.ItemStackBuilder;
import net.kyori.adventure.text.Component;

public class HatInventoryBuilder {
    private static Inventory inv;

    public static void createInventory() {
        inv = Bukkit.createInventory(null, 54, Component.text("Hats"));
        inv.addItem(new ItemStack[] { (new ItemStackBuilder(Material.BARRIER)).withName("§aDequip Hat").build() });
        for (Hat hat : Hat.hats) {
            inv.addItem(new ItemStack[] { hat.getMadeItem() });
        }
        InventoryUtils.fillBlanks(inv);
    }

    public static Inventory getInventory() {
        return inv;
    }
}