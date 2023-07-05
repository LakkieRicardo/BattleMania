package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.kyori.adventure.text.Component;

public class ItemStackBuilder {
    private ItemStack value;
    private String name;
    private List<String> lore;
    private String owner;
    private int count;

    public ItemStackBuilder(Material m) {
        this(new ItemStack(m));
    }

    public ItemStackBuilder(ItemStack item) {
        this(item, true);
    }

    public ItemStackBuilder(ItemStack item, boolean updateVars) {
        if (updateVars) {
            this.value = item;
            this.name = InventoryUtils.getItemName(item);
            this.lore = InventoryUtils.getItemLore(item);

            if (this.name == null) {
                this.name = "";
            }
            if (this.lore == null) {
                this.lore = new ArrayList<>();
            }

            ItemMeta itemMeta = item.getItemMeta();
            if (item.getType() == Material.PLAYER_HEAD) {
                OfflinePlayer owningPlayer = ((SkullMeta) itemMeta).getOwningPlayer();
                if (owningPlayer != null) {
                    this.owner = owningPlayer.getName();
                } else {
                    this.owner = null;
                }
            } else {
                this.owner = null;
            }
            this.count = item.getAmount();
        } else {
            this.value = item;
        }
    }

    public ItemStackBuilder withName(String name) {
        InventoryUtils.renameItem(this.value, name);
        this.name = name;
        return this;
    }

    public ItemStackBuilder withLore(List<Component> lore) {
        InventoryUtils.setItemLore(this.value, lore);
        return this;
    }

    public ItemStackBuilder withLoreLine(String lore) {
        if (InventoryUtils.getItemLore(this.value) == null) {
            InventoryUtils.setItemLore(this.value, lore);
            return this;
        }
        InventoryUtils.addItemLore(this.value, lore);
        this.lore = InventoryUtils.getItemLore(this.value);
        return this;
    }

    public ItemStackBuilder withSkullOwner(String owner) {
        if (this.value.getType() != Material.PLAYER_HEAD) {
            return this;
        }
        SkullMeta meta = (SkullMeta) this.value.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        this.value.setItemMeta(meta);
        this.owner = owner;
        return this;
    }

    public ItemStackBuilder withSkullOwner(OfflinePlayer player) {
        if (this.value.getType() != Material.PLAYER_HEAD) {
            return this;
        }
        SkullMeta meta = (SkullMeta) this.value.getItemMeta();
        meta.setOwningPlayer(player);
        this.value.setItemMeta(meta);
        this.owner = player.getName();
        return this;
    }

    public ItemStackBuilder withCount(int count) {
        this.value.setAmount(count);
        this.count = count;
        return this;
    }

    public ItemStack build() {
        return this.value;
    }

    public int getCount() {
        return this.count;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public String getName() {
        return this.name;
    }

    public String getOwner() {
        return this.owner;
    }

    public ItemStack getValue() {
        return this.value;
    }
}