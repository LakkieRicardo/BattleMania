package net.battle.core.assets.hats.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.hats.Hat;
import net.battle.core.handlers.ItemStackBuilder;

public class HatGlass extends Hat {
    
    public HatGlass() {
        super("glass_helmet");
    }

    protected ItemStack getItem() {
        return (new ItemStackBuilder(Material.CYAN_STAINED_GLASS)).withName("§aGlass Helmet").build();
    }
}