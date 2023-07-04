package net.battle.core.handlers.module;

import org.bukkit.inventory.Inventory;

public class InventoryMeta {
    private final Inventory real;
    private final int size;
    private final String title;

    public InventoryMeta(Inventory real, int size, String title) {
        this.real = real;
        this.size = size;
        this.title = title;
    }

    public Inventory getReal() {
        return this.real;
    }

    public int getSize() {
        return this.size;
    }

    public String getTitle() {
        return this.title;
    }
}