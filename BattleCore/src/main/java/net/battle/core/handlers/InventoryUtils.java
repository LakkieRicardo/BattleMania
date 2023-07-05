package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.battle.core.BMTextConvert;
import net.kyori.adventure.text.Component;

public class InventoryUtils {
    public static ItemStack renameItem(ItemStack item, Component name) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack renameItem(ItemStack item, String name) {
        return renameItem(item, Component.text(name));
    }

    public static ItemStack setItemLore(ItemStack item, List<Component> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setItemLore(ItemStack item, Component lore) {
        return setItemLore(item, Arrays.asList(new Component[] { lore }));
    }

    public static ItemStack setItemLore(ItemStack item, String lore) {
        return setItemLore(item, Arrays.asList(new Component[] { Component.text(lore) }));
    }

    public static ItemStack addItemLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        List<Component> newLore = meta.lore();
        if (newLore == null) {
            newLore = new ArrayList<>();
        }
        newLore.add(Component.text(lore));
        meta.lore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack editItem(ItemStack item, Component name, List<Component> lore) {
        return setItemLore(renameItem(item, name), lore);
    }

    public static ItemStack getPlayerSkull(String player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player));
        skull.setItemMeta((ItemMeta) meta);
        return skull;
    }

    public static List<String> getItemLore(ItemStack item) {
        List<Component> compsLore = item.getItemMeta().lore();
        if (compsLore == null) {
            compsLore = new ArrayList<>();
        }
        List<String> result = new ArrayList<String>(compsLore.size());
        for (Component compLore : compsLore) {
            result.add(BMTextConvert.CTS.serialize(compLore));
        }
        return result;
    }

    public static List<Component> getItemLoreAsComps(ItemStack item) {
        return item.getItemMeta().lore();
    }

    public static String getItemName(ItemStack item) {
        if (item == null || item.getItemMeta() == null || item.getItemMeta().displayName() == null) {
            return "";
        }
        return BMTextConvert.LCTS.serialize(item.getItemMeta().displayName());
    }

    public static boolean isItemSimilarTo(ItemStack item, ItemStack item2) {
        if (item == null || item2 == null) {
            return false;
        }
        if (item.getItemMeta() == null || item2.getItemMeta() == null) {
            return false;
        }
        if (!item.getItemMeta().hasDisplayName() || !item2.getItemMeta().hasDisplayName()) {
            return false;
        }
        return (item.getType() == item2.getType() && BMTextConvert.CTS.serialize(item.getItemMeta().displayName())
                .equalsIgnoreCase(BMTextConvert.CTS.serialize(item2.getItemMeta().displayName())));
    }

    public static Inventory fillBlanks(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, BLANK_ITEM);
            }
        }
        return inv;
    }

    public static Inventory setItem(Inventory inv, int right, int down, ItemStack item) {
        inv.setItem(right + down * 9, item);
        return inv;
    }

    public static boolean compareItemNames(ItemStack item, String name, boolean caseSensitive) {
        return caseSensitive ? getItemName(item).equals(name) : getItemName(item).equalsIgnoreCase(name);
    }

    public static Inventory duplicateInventory(InventoryHolder holder, InventoryView inv) {
        Inventory newInv = Bukkit.createInventory(holder, inv.countSlots(), inv.title());
        for (int i = 0; i < inv.countSlots(); i++) {
            newInv.setItem(i, inv.getItem(i));
        }
        return newInv;
    }

    public static boolean loreContainsString(ItemMeta itemMeta, String term) {
        for (Component comp : itemMeta.lore()) {
            if (BMTextConvert.CTS.serialize(comp).contains(term)) {
                return true;
            }
        }
        return false;
    }

    public static final ItemStack BLANK_ITEM = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    static {
        renameItem(BLANK_ITEM, Component.text("§r"));
    }
}