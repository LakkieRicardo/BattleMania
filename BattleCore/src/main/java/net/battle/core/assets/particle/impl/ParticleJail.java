package net.battle.core.assets.particle.impl;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.particle.BMParticle;
import net.battle.core.assets.particle.handlers.ParticleUtility;
import net.battle.core.handlers.ItemStackBuilder;

public class ParticleJail extends BMParticle {
    public ParticleJail(String name) {
        super(name);
    }

    public ParticleJail() {
        super("jail");
    }

    public void update(Player emitter, World world, long updateCount) {
        if (ParticleUtility.hasPlayerMoved((OfflinePlayer) emitter)) {
            if (updateCount % 2L == 0L) {
                world.spawnParticle(Particle.FIREWORKS_SPARK, emitter.getLocation().add(0, 0.5f, 0), 4, 0, 0, 0, 0);
            }
        } else if (updateCount % 5L == 0L) {
            final float step = 0.3F;
            final float radius = 0.7F;

            int currentStep = 0;
            for (float xz = 0.0F; xz < Math.PI * 2; xz += step) {
                float x = (float) Math.cos(xz) * radius;
                float z = (float) Math.sin(xz) * radius;

                world.spawnParticle(Particle.CRIT_MAGIC, emitter.getLocation().add(x, 2.25f, z), 2, 0, 0, 0, 0);
                world.spawnParticle(Particle.CRIT_MAGIC, emitter.getLocation().add(x, 0.25f, z), 2, 0, 0, 0, 0);

                float stepVertical = 0.75F;
                if (currentStep % 3 == 0) {
                    for (float y = 0.5F; y < 2.5F; y += stepVertical) {
                        world.spawnParticle(Particle.FIREWORKS_SPARK, emitter.getLocation().add(x, y, z), 1, 0, 0, 0, 0);
                    }
                }

                currentStep++;
            }
        }
    }

    protected ItemStack createItem() {
        return (new ItemStackBuilder(Material.IRON_BARS)).withName("§aJail").build();
    }
}