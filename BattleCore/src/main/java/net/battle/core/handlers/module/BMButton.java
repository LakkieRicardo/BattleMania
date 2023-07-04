package net.battle.core.handlers.module;

import org.bukkit.inventory.ItemStack;

import net.battle.core.handlers.ItemStackBuilder;

public class BMButton {
    private final ItemStack item;
    private final ItemStackBuilder builder;
    private final boolean usingBuilder;
    private final String parent;
    private final int position;

    public BMButton(ItemStack item, ItemStackBuilder builder, boolean usingBuilder, String parent, int position) {
        this.item = item;
        this.builder = builder;
        this.usingBuilder = usingBuilder;
        this.parent = parent;
        this.position = position;
    }

    public ItemStackBuilder getBuilder() {
        return this.builder;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public String getParentInventoryName() {
        return this.parent;
    }

    public int getPosition() {
        return this.position;
    }

    public boolean isUsingBuilder() {
        return this.usingBuilder;
    }

    public static BMButton createSimple(ItemStack item, String parentInv, int pos) {
        return new BMButton(item, null, false, parentInv, pos);
    }
}