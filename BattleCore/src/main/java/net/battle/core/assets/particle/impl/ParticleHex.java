package net.battle.core.assets.particle.impl;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.particle.BMParticle;
import net.battle.core.assets.particle.handlers.ParticleUtility;
import net.battle.core.handlers.ItemStackBuilder;

public class ParticleHex extends BMParticle {
    public ParticleHex() {
        super("hex");
    }

    public void update(Player emitter, World world, long updateCount) {
        if (ParticleUtility.hasPlayerMoved(emitter)) {
            if (updateCount % 4L == 0L) {
                world.spawnParticle(Particle.PORTAL, emitter.getLocation().add(0, 0.5f, 0), 10, 0, 0, 0, 0);
            }
        } else if (updateCount % 10L == 0L) {
            final float step = 0.3F;
            final float radius = 0.9F;
            final float verticalScale = 1.2F;
            for (float xyz = 0.0F; xyz < Math.PI * 2; xyz += step) {
                float offX = (float) Math.cos(xyz) * radius;
                float offZ = (float) Math.sin(xyz) * radius;

                float yAbove = (float) Math.sin(xyz) * verticalScale + 1;
                float yBelow = (float) Math.sin(xyz + Math.PI) * verticalScale + 1;

                
                world.spawnParticle(Particle.PORTAL, emitter.getLocation().add(offX, yAbove, offZ), 2, 0, 0, 0, 0);
                world.spawnParticle(Particle.PORTAL, emitter.getLocation().add(offX, yBelow, offZ), 2, 0, 0, 0, 0);
            }
        }
    }

    protected ItemStack createItem() {
        return (new ItemStackBuilder(Material.BONE_MEAL)).withName("§aHex").build();
    }
}