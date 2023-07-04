package net.battle.core.handlers;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import net.kyori.adventure.text.Component;

public final class Hologram {
    public Hologram(Location l, String text) {
        ArmorStand stand = (ArmorStand) l.getWorld().spawn(l, ArmorStand.class);
        stand.customName(Component.text(text));
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setVisible(false);
    }
}