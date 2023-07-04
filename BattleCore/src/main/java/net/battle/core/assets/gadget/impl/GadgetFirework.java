package net.battle.core.assets.gadget.impl;

import java.util.Random;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import net.battle.core.assets.gadget.Gadget;
import net.battle.core.handlers.InventoryUtils;

public class GadgetFirework extends Gadget {
    public GadgetFirework() {
        super(FIREWORK_LABEL, "§cFirework", UUID.randomUUID(), 5.0F);
    }

    public void leftClickGadgetAction(Player pl) {
        FireworkEffect.Type type;
        Firework fw = (Firework) pl.getWorld().spawn(pl.getLocation(), Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();

        switch ((new Random()).nextInt(3)) {
        case 0:
            type = FireworkEffect.Type.BURST;
            break;
        case 1:
            type = FireworkEffect.Type.CREEPER;
            break;
        case 2:
            type = FireworkEffect.Type.STAR;
        default:
            type = FireworkEffect.Type.BALL;
            break;
        }
        FireworkEffect fwe = FireworkEffect.builder().with(type).withColor(Color.RED).withFade(Color.GRAY).flicker(false).build();
        fwm.addEffect(fwe);
        fw.setFireworkMeta(fwm);
    }

    public void rightClickGadgetAction(Player pl) {
        FireworkEffect.Type type;
        Firework fw = (Firework) pl.getWorld().spawn(pl.getLocation(), Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();

        switch ((new Random()).nextInt(5)) {
        case 0:
            type = FireworkEffect.Type.BALL;
            break;

        case 1:
            type = FireworkEffect.Type.BALL_LARGE;
            break;

        case 2:
            type = FireworkEffect.Type.BURST;
            break;
        case 3:
            type = FireworkEffect.Type.CREEPER;
            break;
        case 4:
            type = FireworkEffect.Type.STAR;
        default:
            type = FireworkEffect.Type.BALL;
            break;
        }
        FireworkEffect fwe = FireworkEffect.builder().with(type).withColor(new Color[] { Color.RED, Color.WHITE, Color.BLUE }).flicker(true).build();
        fwm.addEffect(fwe);
        fw.setFireworkMeta(fwm);
    }

    public static final ItemStack FIREWORK_LABEL = new ItemStack(Material.FIREWORK_STAR);
    static {
        InventoryUtils.renameItem(FIREWORK_LABEL, "§cFirework");
        InventoryUtils.setItemLore(FIREWORK_LABEL, "§7Launch a firework!");
    }
}