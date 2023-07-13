package net.battle.core.layouts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class LayoutHolder implements InventoryHolder {

    protected final Inventory inventory;
    protected InvLayout layout;
    protected Object data;

    /**
     * Creates an inventory using the layout and makes this object its holder. This will not update any contents of the
     * inventory, the {@link #updateInventory(Player)} function needs to be called for that.
     * 
     * @param layout The layout to base the inventory off of
     */
    public LayoutHolder(InvLayout layout, Object data) {
        inventory = Bukkit.createInventory(this, layout.getLayout().length(), Component.text(layout.getTitle().replaceAll("&", "§")));
        InvLayout.LAYOUT_INVENTORIES.add(inventory);
        this.layout = layout;
        this.data = data;
    }

    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public InvLayout getLayout() {
        return layout;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * This function simply marks the inventory as a certain layout. It does not update the inventory's actual contents.
     * @param layout The layout to change the inventory holder to
     */
    void setLayout(InvLayout layout) {
        this.layout = layout;
    }

    public void updateInventory(Player viewer) {
        layout.updateInventory(inventory, viewer);
    }

}
