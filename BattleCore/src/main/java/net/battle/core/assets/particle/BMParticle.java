package net.battle.core.assets.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.battle.core.handlers.Prefixes;
import net.battle.core.layouts.INavigatorContentItem;

public abstract class BMParticle implements INavigatorContentItem {
    private static long updateCount;
    public static final List<BMParticle> particles = new ArrayList<>();

    public final List<UUID> players = new ArrayList<>();
    private String name;
    private ItemStack item;

    public BMParticle(String name) {
        this.name = name;
        this.item = createItem();
        particles.add(this);
    }

    public abstract void update(Player emitter, World world, long udpateCount);

    protected abstract ItemStack createItem();

    public ItemStack getItem() {
        return this.item;
    }

    public void equip(Player pl) {
        dequip(pl);
        this.players.add(pl.getUniqueId());
        pl.sendMessage(Prefixes.COMMAND + "Equipped particle §c" + this.name);
    }

    public void update(long updateCount) {
        for (UUID playerUUID : this.players) {
            if (Bukkit.getPlayer(playerUUID) == null) {
                this.players.remove(playerUUID);
                continue;
            }
            Player emitter = Bukkit.getPlayer(playerUUID);
            update(emitter, emitter.getWorld(), updateCount);
        }
    }

    public String getName() {
        return this.name;
    }

    public static void dequip(Player pl) {
        for (BMParticle particle : particles) {
            if (particle.players.contains(pl.getUniqueId())) {
                particle.players.remove(pl.getUniqueId());
                pl.sendMessage(Prefixes.COMMAND + "Disabled particle §c" + particle.getName());
            }
        }
    }

    public static void dequip(UUID player) {
        for (BMParticle particle : particles) {
            if (particle.players.contains(player)) {
                particle.players.remove(player);
            }
        }
    }

    public static BMParticle getPlayerParticle(UUID player) {
        for (BMParticle particle : particles) {
            if (particle.players.contains(player)) {
                return particle;
            }
        }
        return null;
    }

    public static void update() {
        for (BMParticle particle : particles) {
            particle.update(updateCount);
        }
        updateCount++;
    }
}