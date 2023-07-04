package net.battle.core.assets.gadget.impl;

import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.battle.core.BMCorePlugin;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.handlers.InventoryUtils;
import net.kyori.adventure.text.Component;

public class GadgetApple extends Gadget {
    public GadgetApple() {
        super(APPLE_LABEL, "§cApple", UUID.randomUUID(), 1.5F);
    }

    public void leftClickGadgetAction(Player pl) {
        execute(pl);
    }

    public void rightClickGadgetAction(Player pl) {
        execute(pl);
    }

    public void execute(final Player pl) {
        pl.playSound(pl.getLocation(), Sound.ENTITY_GENERIC_EAT, 3.0F, 1.25F);
        pl.sendActionBar(Component.text("§c§lNAM!"));
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
            public void run() {
                pl.playSound(pl.getLocation(), Sound.ENTITY_GENERIC_EAT, 3.0F, 1.0F);
                pl.sendActionBar(Component.text("§c§lNAM! §cNAM!"));
            }
        }, 4L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
            public void run() {
                pl.playSound(pl.getLocation(), Sound.ENTITY_GENERIC_EAT, 3.0F, 0.75F);
                pl.sendActionBar(Component.text("§c§lNAM! §cNAM! §7NAM!"));
            }
        }, 8L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
            public void run() {
                pl.playSound(pl.getLocation(), Sound.ENTITY_PLAYER_BURP, 3.0F, 0.5F);
                pl.sendActionBar(Component.text("§c§l*burp*"));
            }
        }, 16L);
    }

    private static final ItemStack APPLE_LABEL = new ItemStack(Material.APPLE);
    static {
        ItemMeta meta0 = APPLE_LABEL.getItemMeta();
        meta0.displayName(Component.text("§cApple"));
        meta0.lore(Arrays.asList(new Component[] { Component.text("§7Just an apple...") }));
        APPLE_LABEL.setItemMeta(meta0);
        InventoryUtils.setItemLore(APPLE_LABEL, "§7Right click to eat an apple!");
    }
}