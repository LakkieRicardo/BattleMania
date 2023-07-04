package net.battle.core.assets.hats.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.hats.Hat;
import net.battle.core.handlers.ItemStackBuilder;

public class HatWool extends Hat {
    public HatWool() {
        super("wool");
    }

    protected ItemStack getItem() {
        return (new ItemStackBuilder(Material.WHITE_WOOL)).withName("§aWool").build();
    }
}