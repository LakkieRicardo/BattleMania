package net.battle.core.assets.hats;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Hat {
    public static final List<Hat> hats = new ArrayList<>();

    public static String[] names;
    private ItemStack item;
    private String name;

    public Hat(String name) {
        hats.add(this);
        this.item = getItem();
        this.name = name;
        names = remakeNames();
    }

    protected abstract ItemStack getItem();

    public ItemStack getMadeItem() {
        return this.item;
    }

    public String getName() {
        return this.name;
    }

    public void equip(Player pl) {
        pl.getInventory().setHelmet(getMadeItem());
        pl.sendMessage("§9§lCOMMAND§8 > §fEquipped hat §c" + this.name);
    }

    public static void dequip(Player pl) {
        pl.getInventory().setHelmet(new ItemStack(Material.AIR));
        pl.sendMessage("§9§lCOMMAND§8 > §fYou dequipped your §chat");
    }

    private static String[] remakeNames() {
        String[] names = new String[hats.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = ((Hat) hats.get(i)).name;
        }
        return names;
    }
}