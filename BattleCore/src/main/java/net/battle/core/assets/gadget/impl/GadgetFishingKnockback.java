package net.battle.core.assets.gadget.impl;

import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMCorePlugin;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.Prefixes;

public class GadgetFishingKnockback extends Gadget {
    public static boolean enabled = true;

    public GadgetFishingKnockback() {
        super(LABEL_ITEM, "§cFishing Rod", UUID.randomUUID(), 1.0F);
        this.usingLeft = false;
        this.usingRight = false;
    }

    @EventHandler
    public void onFishingHookDamage(EntityDamageByEntityEvent e) {
        if (!enabled) {
            return;
        }
        if (!(e.getDamager() instanceof FishHook)) {
            return;
        }

        FishHook hook = (FishHook) e.getDamager();
        e.getEntity().setVelocity(hook.getLocation().getDirection().multiply(5));
        if (!(hook.getShooter() instanceof Player)) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player pl = (Player) e.getEntity();
        Player shooter = (Player) hook.getShooter();

        boolean luck = (BMCorePlugin.ACTIVE_PLUGIN.random.nextInt(100) == 0);

        pl.sendMessage(Prefixes.ALERT + "Player §c" + shooter.getName() + "§f's fishing hook hit you!" + (luck ? " You deserved it! §cThat's rude." : ""));
        shooter.sendMessage(Prefixes.ALERT + "You hit §c" + pl.getName() + "§f with your fishing rod!"
                + (luck ? " You told him they deserved it. §cThat's rude." : ""));
    }

    public void leftClickGadgetAction(Player pl) {
    }

    public void rightClickGadgetAction(Player pl) {
    }

    public static final ItemStack LABEL_ITEM = new ItemStack(Material.FISHING_ROD);
    static {
        InventoryUtils.renameItem(LABEL_ITEM, "§aFishing Rod");
    }
}