package net.battle.test.listeners;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.Prefixes;
import net.battle.test.BMTestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GoldAppleListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerEatApple(FoodLevelChangeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player pl = (Player) e.getEntity();
        if (pl.getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE
                && pl.getInventory().getItemInMainHand().getAmount() == 1)
            if (BMCorePlugin.ACTIVE_PLUGIN.random.nextBoolean()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) BMTestPlugin.ACTIVE_PLUGIN, new Runnable() {
                    public void run() {
                        byte b;
                        int i;
                        PotionEffectType[] arrayOfPotionEffectType;
                        for (i = (arrayOfPotionEffectType = PotionEffectType.values()).length, b = 0; b < i;) {
                            PotionEffectType type = arrayOfPotionEffectType[b];
                            if (type != null)
                                pl.removePotionEffect(type);
                            b++;
                        }

                        pl.sendMessage(Prefixes.ALERT + "You ate a §c§ldreadful§f apple");
                        PotionEffect effect = new PotionEffect(PotionEffectType.WITHER, 120, 2, false, true);
                        pl.addPotionEffect(effect);
                        pl.setHealth(10.0D);
                    }
                }, 2L);
            } else {
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) BMTestPlugin.ACTIVE_PLUGIN, new Runnable() {
                    public void run() {
                        byte b;
                        int i;
                        PotionEffectType[] arrayOfPotionEffectType;
                        for (i = (arrayOfPotionEffectType = PotionEffectType.values()).length, b = 0; b < i;) {
                            PotionEffectType type = arrayOfPotionEffectType[b];
                            if (type != null)
                                pl.removePotionEffect(type);
                            b++;
                        }

                        pl.sendMessage(Prefixes.ALERT + "You ate a §c§lgourmet§f apple!");
                        PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, 120, 3, false, true);
                        pl.addPotionEffect(effect);
                        pl.setHealth(20.0D);
                    }
                }, 2L);
            }
    }
}