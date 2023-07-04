package net.battle.core.assets.hats.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.hats.Hat;
import net.battle.core.handlers.ItemStackBuilder;

public class HatCreeperSkull extends Hat {
    public HatCreeperSkull() {
        super("creeper");
    }

    protected ItemStack getItem() {
        return (new ItemStackBuilder(Material.CREEPER_HEAD)).withName("§aCreeper").build();
    }
}