package net.battle.core.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.Component;

public final class EntityHandler {
    public EntityHandler(Class<? extends Entity> entity, boolean showName, String name, Location loc, Vector direction) {
        final Entity staticEntity = loc.getWorld().spawn(loc, entity);
        if (showName) {
            staticEntity.customName(Component.text(name));
            staticEntity.setCustomNameVisible(true);
        }

        // TODO need to figure out how to spawn entity with no AI
        // Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
        //     public void run() {
        //         Entity nmsEntity = ((CraftEntity) staticEntity).getHandle();
        //         NBTTagCompound tag = nmsEntity.getNBTTag();
        //         if (tag == null) {
        //             tag = new NBTTagCompound();
        //         }
        //         nmsEntity.c(tag);
        //         tag.setInt("NoAI", 1);
        //         nmsEntity.f(tag);
        //         if (staticEntity instanceof LivingEntity) {
        //             ((LivingEntity) staticEntity).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999, 255, true, false));
        //         }
        //     }
        // }, 30L);
        staticEntity.teleport(staticEntity.getLocation().add(0.0D, 0.5D, 0.0D));
        staticEntity.teleport(loc);
        staticEntity.getLocation().setYaw(loc.getYaw());
    }
}