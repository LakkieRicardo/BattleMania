package net.battle.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.battle.core.BMTextConvert;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.InventoryUtils;
import net.kyori.adventure.text.Component;

public class PotionHandler implements Listener {

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
        ItemStack clicked = e.getCurrentItem();
        Material m = clicked.getType();

        if (BMTextConvert.CTS.serialize(inv.title()).startsWith("Potion Type: ")) {
            e.setCancelled(true);
            if (m == Material.BARRIER) {
                byte b;
                int i;
                PotionEffectType[] arrayOfPotionEffectType;
                for (i = (arrayOfPotionEffectType = PotionEffectType.values()).length, b = 0; b < i;) {
                    PotionEffectType type = arrayOfPotionEffectType[b];
                    if (type != null) {

                        if (type != PotionEffectType.DAMAGE_RESISTANCE) {

                            if (pl.hasPotionEffect(type))
                                pl.removePotionEffect(type);
                        }
                    }
                    b++;
                }

                pl.sendMessage("§9§lCOMMAND§8 > §fYou cleared your potion effects");
                pl.closeInventory();
                return;
            }
            if (m == Material.POTION) {
                String name = InventoryUtils.getItemName(clicked);

                pl.closeInventory();
                pl.openInventory(getEditInventory(name.replaceFirst("§c", ""), BMTextConvert.CTS.serialize(inv.title()).replaceFirst("Potion Type: ", "")));
                pl.sendMessage("§9§lCOMMAND§8 > §fOpening edit inventory");

                return;
            }
            return;
        }
        if (BMTextConvert.CTS.serialize(inv.title()).contains(" ") && PotionEffectType.getByName(BMTextConvert.CTS.serialize(inv.title()).split(" ")[0]) != null) {
            e.setCancelled(true);
            if (m == Material.POTION) {
                return;
            }
            if (m == Material.LIME_CONCRETE || m == Material.GREEN_CONCRETE || m == Material.YELLOW_CONCRETE || m == Material.PINK_CONCRETE
                    || m == Material.RED_CONCRETE) {
                EditType itemType = getEditType(InventoryUtils.getItemName(clicked));
                if (!clicked.containsEnchantment(Enchantment.DURABILITY)) {
                    for (int i = 0; i < inv.countSlots(); i++) {
                        ItemStack item = inv.getItem(i);
                        Material itemMat = item.getType();
                        if (itemMat == Material.LIME_CONCRETE || itemMat == Material.GREEN_CONCRETE || itemMat == Material.YELLOW_CONCRETE
                                || itemMat == Material.PINK_CONCRETE || itemMat == Material.RED_CONCRETE) {
                            EditType type = getEditType(InventoryUtils.getItemName(item));
                            if (type != null && itemType != null && type == itemType) {
                                for (int j = 0; j < Enchantment.values().length; j++) {
                                    Enchantment ench = Enchantment.values()[j];
                                    item.removeEnchantment(ench);
                                }

                            }
                        }
                    }

                }
                clicked.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                return;
            }
            if (m == Material.EMERALD) {
                String type = "";
                for (int i = 0; i < inv.countSlots(); i++) {
                    ItemStack item = inv.getItem(i);
                    if (item.getType() == Material.POTION) {
                        type = InventoryUtils.getItemName(item).replaceFirst("§c", "");
                    }
                }

                PotionEffect potion = new PotionEffect(PotionEffectType.getByName(type), 600, 0, false, false);
                Player t = CommandHandler.getPlayer(BMTextConvert.CTS.serialize(inv.title()).replaceFirst(type + " ", ""));
                if (t == null) {
                    pl.sendMessage("§4§lERROR§8 > §cPlayer left");
                    return;
                }
                t.addPotionEffect(potion);
                pl.closeInventory();
                t.sendMessage("§9§lCOMMAND§8 > §fYou got the default potion from " + pl.getName());
                if (t != pl) {
                    pl.sendMessage("§9§lCOMMAND§8 > §fApplied default potion for §c" + t.getName());
                }
                return;
            }
            if (m == Material.EMERALD_BLOCK) {
                int duration = -1;
                int amplifier = -1;
                int visible = -1;
                for (int i = 0; i < inv.countSlots(); i++) {
                    ItemStack item = inv.getItem(i);
                    Material itemMat = item.getType();
                    if (getEditType(InventoryUtils.getItemName(item)) == EditType.DURATION && item.containsEnchantment(Enchantment.DURABILITY)) {
                        switch (itemMat) {
                        case LIME_CONCRETE:
                            duration = 60;
                            break;
                        case GREEN_CONCRETE:
                            duration = 120;
                            break;
                        case YELLOW_CONCRETE:
                            duration = 300;
                            break;
                        case PINK_CONCRETE:
                            duration = 1200;
                            break;
                        case RED_CONCRETE:
                            duration = 9999;
                            break;
                        default:
                            break;
                        }
                    }
                    if (getEditType(InventoryUtils.getItemName(item)) == EditType.AMPLIFIER && item.containsEnchantment(Enchantment.DURABILITY)) {
                        switch (itemMat) {
                        case LIME_CONCRETE:
                            amplifier = 1;
                            break;
                        case GREEN_CONCRETE:
                            amplifier = 2;
                            break;
                        case YELLOW_CONCRETE:
                            amplifier = 5;
                            break;
                        case PINK_CONCRETE:
                            amplifier = 20;
                            break;
                        case RED_CONCRETE:
                            amplifier = 255;
                            break;
                        default:
                            break;
                        }
                    }
                    if (getEditType(InventoryUtils.getItemName(item)) == EditType.VISIBLE && item.containsEnchantment(Enchantment.DURABILITY)) {
                        if (itemMat == Material.GREEN_CONCRETE) {
                            visible = 1;
                        }

                        if (itemMat == Material.RED_CONCRETE) {
                            visible = 0;
                        }
                    }
                }

                if (duration == -1) {
                    pl.sendMessage("§4§lERROR§8 > §cNo duration selected");

                    return;
                }
                if (amplifier == -1) {
                    pl.sendMessage("§4§lERROR§8 > §cNo amplifier selected");

                    return;
                }
                if (visible == -1) {
                    pl.sendMessage("§4§lERROR§8 > §cNo visiblity selected");

                    return;
                }
                String type = "";
                for (int j = 0; j < inv.countSlots(); j++) {
                    ItemStack content = inv.getItem(j);
                    if (content.getType() == Material.POTION)
                        type = InventoryUtils.getItemName(content).replaceFirst("§c", "");
                }

                PotionEffect potion = new PotionEffect(PotionEffectType.getByName(type), duration * 20, amplifier, (visible == 1), (visible == 1));
                Player t = CommandHandler.getPlayer(BMTextConvert.CTS.serialize(inv.title()).replaceFirst(type + " ", ""));
                if (t == null) {
                    pl.sendMessage("§4§lERROR§8 > §cPlayer left");
                    return;
                }
                t.addPotionEffect(potion);
                pl.closeInventory();
                t.sendMessage("§9§lCOMMAND§8 > §fYou got potion: §c" + PotionEffectType.getByName(type).getName() + "§f Duration: §c"
                        + ((duration == 9999) ? "§lFOREVER " : (duration / 60 + "min ")) + "§fAmplifier: §c" + amplifier + "§f Visible: §c"
                        + ((visible == 1) ? "Yes" : "No") + "§f from §c" + pl.getName());
                if (t != pl) {
                    pl.sendMessage("§9§lCOMMAND§8 > §fYou gave yourself Type: §c" + PotionEffectType.getByName(type).getName() + "§f Duration: §c"
                            + ((duration == 9999) ? "§lFOREVER " : (duration / 60 + "min ")) + "§fAmplifier: §c" + amplifier + "§f Visible: §c"
                            + ((visible == 1) ? "Yes" : "No"));
                }
                return;
            }
            if (m == Material.BARRIER) {
                String type = "";
                for (int i = 0; i < inv.countSlots(); i++) {
                    ItemStack content = inv.getItem(i);
                    if (content.getType() == Material.POTION)
                        type = InventoryUtils.getItemName(content).replaceFirst("§c", "");
                }

                Player t = CommandHandler.getPlayer(BMTextConvert.CTS.serialize(inv.title()).replaceFirst(type + " ", ""));
                if (t == null) {
                    pl.sendMessage("§4§lERROR§8 > §cPlayer left");
                    return;
                }
                t.removePotionEffect(PotionEffectType.getByName(type));
                t.sendMessage("§9§lCOMMAND§8 > §fYou got §c" + type + "§f removed from §c" + pl.getName());
                if (t != pl) {
                    pl.sendMessage("§9§lCOMMAND§8 > §fYou removed §c" + type + "§f from §c" + t.getName());
                }
            }
        }
    }

    public static Inventory getPotionTypeInventory(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Potion Type: " + name));
        ItemStack remove = new ItemStack(Material.BARRIER);
        InventoryUtils.renameItem(remove, "§cClear Potions");
        inv.setItem(0, remove);
        int counter = 1;
        byte b;
        int i;
        PotionEffectType[] arrayOfPotionEffectType;
        for (i = (arrayOfPotionEffectType = PotionEffectType.values()).length, b = 0; b < i;) {
            PotionEffectType type = arrayOfPotionEffectType[b];
            if (type != null) {

                ItemStack potion = new ItemStack(Material.POTION);
                InventoryUtils.renameItem(potion, "§c" + type.getName().toUpperCase());
                InventoryUtils.setItemLore(potion, "§7§oClick To Move Further");
                inv.setItem(counter, potion);
                counter++;
            }
            b++;
        }
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getEditInventory(String potion, String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text(potion + " " + name));
        ItemStack potionItem = new ItemStack(Material.POTION);
        InventoryUtils.renameItem(potionItem, "§c" + potion);
        InventoryUtils.setItem(inv, 4, 0, potionItem);

        ItemStack duration0 = new ItemStack(Material.LIME_CONCRETE);
        InventoryUtils.renameItem(duration0, "§cDuration: 1:00");
        InventoryUtils.setItem(inv, 0, 1, duration0);

        ItemStack duration1 = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(duration1, "§cDuration: 2:00");
        InventoryUtils.setItem(inv, 2, 1, duration1);

        ItemStack duration2 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(duration2, "§cDuration: 5:00");
        InventoryUtils.setItem(inv, 4, 1, duration2);

        ItemStack duration3 = new ItemStack(Material.PINK_CONCRETE);
        InventoryUtils.renameItem(duration3, "§cDuration: 20:00");
        InventoryUtils.setItem(inv, 6, 1, duration3);

        ItemStack duration4 = new ItemStack(Material.RED_CONCRETE);
        InventoryUtils.renameItem(duration4, "§cDuration: FOREVER");
        InventoryUtils.setItem(inv, 8, 1, duration4);

        ItemStack amplifier0 = new ItemStack(Material.LIME_CONCRETE);
        InventoryUtils.renameItem(amplifier0, "§cAmplifier: 1");
        InventoryUtils.setItem(inv, 0, 3, amplifier0);

        ItemStack amplifier1 = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(amplifier1, "§cAmplifier: 2");
        InventoryUtils.setItem(inv, 2, 3, amplifier1);

        ItemStack amplifier2 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(amplifier2, "§cAmplifier: 5");
        InventoryUtils.setItem(inv, 4, 3, amplifier2);

        ItemStack amplifier3 = new ItemStack(Material.PINK_CONCRETE);
        InventoryUtils.renameItem(amplifier3, "§cAmplifier: 20");
        InventoryUtils.setItem(inv, 6, 3, amplifier3);

        ItemStack amplifier4 = new ItemStack(Material.RED_CONCRETE);
        InventoryUtils.renameItem(amplifier4, "§cAmplifier: 255");
        InventoryUtils.setItem(inv, 8, 3, amplifier4);

        ItemStack visible = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(visible, "§cVisible");
        InventoryUtils.setItem(inv, 2, 5, visible);

        ItemStack invisible = new ItemStack(Material.RED_CONCRETE);
        InventoryUtils.renameItem(invisible, "§cInvisible");
        InventoryUtils.setItem(inv, 6, 5, invisible);

        ItemStack skip = new ItemStack(Material.EMERALD);
        InventoryUtils.renameItem(skip, "§cSkip(30s,x1,visible");
        InventoryUtils.setItem(inv, 4, 2, skip);

        ItemStack get = new ItemStack(Material.EMERALD_BLOCK);
        InventoryUtils.renameItem(get, "§cGet Potion");
        InventoryUtils.setItem(inv, 4, 4, get);

        ItemStack remove = new ItemStack(Material.BARRIER);
        InventoryUtils.renameItem(remove, "§cRemove Potion");
        InventoryUtils.setItem(inv, 4, 5, remove);

        InventoryUtils.fillBlanks(inv);

        return inv;
    }

    enum EditType {
        DURATION, AMPLIFIER, VISIBLE;
    }

    public static EditType getEditType(String itemName) {
        if (itemName == null) {
            return null;
        }
        String name = itemName.toLowerCase();
        if (name.contains("duration")) {
            return EditType.DURATION;
        }
        if (name.contains("amplifier")) {
            return EditType.AMPLIFIER;
        }
        if (name.contains("visible")) {
            return EditType.VISIBLE;
        }
        return null;
    }
}