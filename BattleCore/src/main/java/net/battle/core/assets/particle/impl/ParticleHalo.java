package net.battle.core.assets.particle.impl;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.particle.BMParticle;
import net.battle.core.assets.particle.handlers.ParticleUtility;
import net.battle.core.handlers.ItemStackBuilder;

public class ParticleHalo extends BMParticle {
    public ParticleHalo() {
        super("halo");
    }

    public void update(Player emitter, World world, long updateCount) {
        if (ParticleUtility.hasPlayerMoved(emitter)) {
            if (updateCount % 4L == 0L) {
                world.spawnParticle(Particle.FLAME, emitter.getLocation().add(0, 0.5f, 0), 2, 0, 0, 0, 0); // Last 5 numbers are count, offset X Y and Z, and extra(speed)
            }
        } else if (updateCount % 10L == 0L) {
            final float step = 0.4F;
            final float radius = 0.6F;
            for (float xz = 0.0F; xz < Math.PI * 2; xz += step) {
                float offX = (float) Math.cos(xz) * radius;
                float offZ = (float) Math.sin(xz) * radius;
                world.spawnParticle(Particle.FLAME, emitter.getLocation().add(offX, 2.25F, offZ), 1, 0, 0, 0, 0);
            }
        }
    }

    protected ItemStack createItem() {
        return (new ItemStackBuilder(Material.GOLD_BLOCK)).withName("§aHalo").build();
    }
}