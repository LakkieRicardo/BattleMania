package net.battle.core.assets.gadget.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMCorePlugin;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.Prefixes;

public class GadgetFlyingSword extends Gadget {
    private Map<String, Integer> schedularIDs = new HashMap<>();
    private Map<OfflinePlayer, Long> startTime = new HashMap<>();
    private double multiplier = 0.5D;

    public GadgetFlyingSword() {
        super(labelItem, "§cFlying Sword", UUID.randomUUID(), 10.0F);
    }

    public void leftClickGadgetAction(Player pl) {
    }

    public void rightClickGadgetAction(Player pl) {
        pl.setVelocity(pl.getLocation().getDirection().multiply(this.multiplier));
        this.schedularIDs.put(pl.getName(), Integer.valueOf(Bukkit.getScheduler().scheduleSyncRepeatingTask(BMCorePlugin.ACTIVE_PLUGIN, () -> updateSword(pl), 3L, 3L)));
        this.startTime.put(pl, Long.valueOf(System.currentTimeMillis()));
    }

    public void updateSword(Player pl) {
        if (!pl.isBlocking()) {
            if (pl.getGameMode() != GameMode.CREATIVE && pl.getGameMode() != GameMode.SPECTATOR) {
                pl.setAllowFlight(false);
            }
            cancelTask(pl.getName());
            return;
        }
        pl.setAllowFlight(true);
        pl.setVelocity(pl.getLocation().getDirection().multiply(this.multiplier));
        long current = System.currentTimeMillis();
        long delta = current - ((Long) this.startTime.get(pl)).longValue();
        if (delta >= 8000L) {
            this.startTime.remove(pl);
            pl.sendMessage(Prefixes.GADGET + "You have exceeded the max time of 8 seconds using the flying sword");
            if (pl.getGameMode() != GameMode.CREATIVE && pl.getGameMode() != GameMode.SPECTATOR) {
                pl.setAllowFlight(false);
            }
            cancelTask(pl.getName());
        }
    }

    public void cancelTask(String player) {
        Bukkit.getScheduler().cancelTask(((Integer) this.schedularIDs.get(player)).intValue());
        this.schedularIDs.remove(player);
    }

    private static ItemStack labelItem = new ItemStack(Material.DIAMOND_SWORD);
    static {
        InventoryUtils.setItemLore(labelItem, "§7Hold down right click to fly!");
        InventoryUtils.renameItem(labelItem, "§cFlying Sword");
    }
}