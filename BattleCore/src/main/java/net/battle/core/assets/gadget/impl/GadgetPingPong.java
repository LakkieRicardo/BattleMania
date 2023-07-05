package net.battle.core.assets.gadget.impl;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import net.battle.core.BMCorePlugin;
import net.battle.core.BMTextConvert;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.assets.gadget.utils.CollisionData;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.InventoryUtils;
import net.kyori.adventure.text.Component;

public class GadgetPingPong extends Gadget {
    public GadgetPingPong() {
        super(LABEL_ITEM, "§cPing Pong", UUID.randomUUID(), 4.5F);
    }

    public void leftClickGadgetAction(Player pl) {
    }

    public void rightClickGadgetAction(Player pl) {
        execute(pl);
    }

    public void execute(Player pl) {
        final Snowball ball = (Snowball) pl.getWorld().spawn(pl.getEyeLocation(), Snowball.class);
        ball.setVelocity(pl.getLocation().getDirection().multiply(1.25D));
        ball.setShooter((ProjectileSource) pl);
        ball.customName(Component.text("ORIG"));
        Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
            public void run() {
                ball.customName(Component.text(BMTextConvert.CTS.serialize(ball.customName()) + " " + ball.getVelocity().getX() + " " + ball.getVelocity().getZ()));
            }
        }, 1L);
    }

    @EventHandler
    public void projectileHitEvent(ProjectileHitEvent e) {
        try {
            final Projectile proj = e.getEntity();
            if (proj.getType() != EntityType.SNOWBALL) {
                return;
            }
            if (((Entity) proj.getShooter()).getType() != EntityType.PLAYER) {
                return;
            }

            if (BMTextConvert.CTS.serialize(proj.customName()).startsWith("ORIG")) {
                CollisionData data = locationTest(proj.getLocation());
                final Snowball ball2 = (Snowball) proj.getWorld().spawn(proj.getLocation(), Snowball.class);
                ball2.setCustomNameVisible(false);
                Vector v = proj.getLocation().getDirection();
                boolean changeX = true;
                boolean changeZ = true;
                switch (data.getX()) {
                case 3:
                    changeX = false;
                    break;
                }
                switch (data.getZ()) {
                case 3:
                    changeZ = false;
                    break;
                }
                v.setY(0.3D);

                String[] components = BMTextConvert.CTS.serialize(proj.customName()).split(" ");
                double x = Double.parseDouble(components[1]);
                double z = Double.parseDouble(components[2]);
                if (changeX) {
                    if (Math.abs(x) < 1.0D) {
                        v.setX(-x);
                    } else {
                        v.setX(x);
                    }
                }
                if (changeZ) {
                    if (Math.abs(z) < 1.0D) {
                        v.setZ(-z);
                    } else {
                        v.setZ(z);
                    }
                }

                ball2.setVelocity(v);
                ball2.customName(Component.text("BOUNCE1"));
                Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
                    public void run() {
                        ball2.customName(Component.text(BMTextConvert.CTS.serialize(ball2.customName()) + " " + proj.getVelocity().getX() + " "
                                + proj.getVelocity().getZ()));
                    }
                }, 1L);
                ball2.setShooter(proj.getShooter());
                return;
            }
            if (BMTextConvert.CTS.serialize(proj.customName()).startsWith("BOUNCE1")) {
                CollisionData data = locationTest(proj.getLocation());
                final Snowball ball2 = (Snowball) proj.getWorld().spawn(proj.getLocation(), Snowball.class);
                ball2.setCustomNameVisible(false);
                Vector v = proj.getLocation().getDirection();
                boolean changeX = true;
                boolean changeZ = true;
                switch (data.getX()) {
                case 3:
                    changeX = false;
                    break;
                }
                switch (data.getZ()) {
                case 3:
                    changeZ = false;
                    break;
                }
                v.setY(0.3D);
                String[] components = BMTextConvert.CTS.serialize(proj.customName()).split(" ");
                double x = Double.parseDouble(components[1]);
                double z = Double.parseDouble(components[2]);
                if (changeX) {
                    if (Math.abs(x) < 1.0D) {
                        v.setX(-x);
                    } else {
                        v.setX(x);
                    }
                }
                if (changeZ) {
                    if (Math.abs(z) < 1.0D) {
                        v.setZ(-z);
                    } else {
                        v.setZ(z);
                    }
                }
                ball2.setVelocity(v);
                ball2.customName(Component.text("BOUNCE2"));
                Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
                    public void run() {
                        ball2.customName(Component.text(BMTextConvert.CTS.serialize(ball2.customName()) + " " + proj.getVelocity().getX() + " "
                                + proj.getVelocity().getZ()));
                    }
                }, 1L);
                ball2.setShooter(proj.getShooter());
                return;
            }
            if (BMTextConvert.CTS.serialize(proj.customName()).startsWith("BOUNCE2")) {
                CollisionData data = locationTest(proj.getLocation());
                final Snowball ball2 = (Snowball) proj.getWorld().spawn(proj.getLocation(), Snowball.class);
                ball2.setCustomNameVisible(false);
                Vector v = proj.getLocation().getDirection();
                boolean changeX = true;
                boolean changeZ = true;
                switch (data.getX()) {
                case 3:
                    changeX = false;
                    break;
                }
                switch (data.getZ()) {
                case 3:
                    changeZ = false;
                    break;
                }
                v.setY(0.3D);
                String[] components = BMTextConvert.CTS.serialize(proj.customName()).split(" ");
                double x = Double.parseDouble(components[1]);
                double z = Double.parseDouble(components[2]);
                if (changeX) {
                    if (Math.abs(x) < 1.0D) {
                        v.setX(-x);
                    } else {
                        v.setX(x);
                    }
                }
                if (changeZ) {
                    if (Math.abs(z) < 1.0D) {
                        v.setZ(-z);
                    } else {
                        v.setZ(z);
                    }
                }
                ball2.setVelocity(v);
                ball2.customName(Component.text("BOUNCE3"));
                Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, new Runnable() {
                    public void run() {
                        ball2.customName(Component.text(BMTextConvert.CTS.serialize(ball2.customName()) + " " + proj.getVelocity().getX() + " "
                                + proj.getVelocity().getZ()));
                    }
                }, 1L);
                ball2.setShooter(proj.getShooter());

                return;
            }
        } catch (Exception exception) {
        }
    }

    public CollisionData locationTest(Location l) {
        CollisionData data = new CollisionData();
        World w = l.getWorld();
        Location newLocation = l;
        Block xTest = w.getBlockAt(l.add(0.5D, 0.0D, 0.0D));
        Block yTest = w.getBlockAt(l.add(0.0D, 0.5D, 0.0D));
        Block zTest = w.getBlockAt(l.add(0.0D, 0.0D, 0.5D));
        Block xTest2 = w.getBlockAt(l.subtract(0.5D, 0.0D, 0.0D));
        Block yTest2 = w.getBlockAt(l.subtract(0.0D, 0.5D, 0.0D));
        Block zTest2 = w.getBlockAt(l.subtract(0.0D, 0.0D, 0.5D));
        if (xTest.getType() == Material.AIR && xTest2.getType() == Material.AIR) {
            data.setX(3);
        }
        if (zTest.getType() == Material.AIR && zTest2.getType() == Material.AIR) {
            data.setZ(3);
        }
        if (yTest.getType() == Material.AIR && yTest2.getType() == Material.AIR) {
            data.setY(3);
        }

        data.setX((xTest.getType() == Material.AIR) ? 1 : 2);
        if (data.getX() == 1 && data.getX() != 3) {
            newLocation.setX(newLocation.getX() + 1.0D);
        } else {
            newLocation.setX(newLocation.getX() - 1.0D);
        }
        data.setY((yTest.getType() == Material.AIR) ? 1 : 2);
        if (data.getY() == 1 && data.getY() != 3) {
            newLocation.setY(newLocation.getY() + 1.0D);
        } else {
            newLocation.setY(newLocation.getY() - 1.0D);
        }
        data.setZ((zTest.getType() == Material.AIR) ? 1 : 2);
        if (data.getZ() == 1 && data.getZ() != 3) {
            newLocation.setZ(newLocation.getZ() + 1.0D);
        } else {
            newLocation.setZ(newLocation.getZ() - 1.0D);
        }

        data.setNewLocation(newLocation.add(0.0D, 0.5D, 0.0D));

        return data;
    }

    public CollisionData locationTestDebug(Location l, CommandSender debugMessenger) {
        debugMessenger.sendMessage("Orig. Location Test Location: " + l.getX() + "," + l.getY() + ", " + l.getZ());
        CollisionData data = new CollisionData();
        World w = l.getWorld();
        Location newLocation = l;
        Block xTest = w.getBlockAt(l.add(1.0D, 0.0D, 0.0D));
        Block yTest = w.getBlockAt(l.add(0.0D, 1.0D, 0.0D));
        Block zTest = w.getBlockAt(l.add(0.0D, 0.0D, 1.0D));
        Block xTest2 = w.getBlockAt(l.subtract(1.0D, 0.0D, 0.0D));
        Block yTest2 = w.getBlockAt(l.subtract(0.0D, 1.0D, 0.0D));
        Block zTest2 = w.getBlockAt(l.subtract(0.0D, 0.0D, 1.0D));
        if (xTest.getType() == Material.AIR && xTest2.getType() == Material.AIR) {
            data.setX(3);
        }
        if (zTest.getType() == Material.AIR && zTest2.getType() == Material.AIR) {
            data.setZ(3);
        }
        if (yTest.getType() == Material.AIR && yTest2.getType() == Material.AIR) {
            data.setY(3);
        }
        debugMessenger.sendMessage("---------------");
        debugMessenger.sendMessage("New X: " + newLocation.getX());
        debugMessenger.sendMessage("New Y: " + newLocation.getY());
        debugMessenger.sendMessage("New Z: " + newLocation.getZ());

        data.setX((xTest.getType() == Material.AIR) ? 1 : 2);
        if (data.getX() == 1 && data.getX() != 3) {
            newLocation.setX(newLocation.getX() + 1.0D);
        } else {
            newLocation.setX(newLocation.getX() - 1.0D);
        }
        debugMessenger.sendMessage("---------------");
        debugMessenger.sendMessage("New X: " + newLocation.getX());
        debugMessenger.sendMessage("New Y: " + newLocation.getY());
        debugMessenger.sendMessage("New Z: " + newLocation.getZ());
        data.setY((yTest.getType() == Material.AIR) ? 1 : 2);
        if (data.getY() == 1 && data.getY() != 3) {
            newLocation.setY(newLocation.getY() + 1.0D);
        } else {
            newLocation.setY(newLocation.getY() - 1.0D);
        }
        debugMessenger.sendMessage("---------------");
        debugMessenger.sendMessage("New X: " + newLocation.getX());
        debugMessenger.sendMessage("New Y: " + newLocation.getY());
        debugMessenger.sendMessage("New Z: " + newLocation.getZ());
        data.setZ((zTest.getType() == Material.AIR) ? 1 : 2);
        if (data.getZ() == 1 && data.getZ() != 3) {
            newLocation.setZ(newLocation.getZ() + 1.0D);
        } else {
            newLocation.setZ(newLocation.getZ() - 1.0D);
        }

        debugMessenger.sendMessage("---------------");
        debugMessenger.sendMessage("New X: " + newLocation.getX());
        debugMessenger.sendMessage("New Y: " + newLocation.getY());
        debugMessenger.sendMessage("New Z: " + newLocation.getZ());

        data.setNewLocation(newLocation.add(0.0D, 0.5D, 0.0D));
        debugMessenger.sendMessage("---------------");
        debugMessenger.sendMessage("New X: " + newLocation.getX());
        debugMessenger.sendMessage("New Y: " + newLocation.getY());
        debugMessenger.sendMessage("New Z: " + newLocation.getZ());

        return data;
    }

    public Location getLocationFromString(String loc) {
        String[] components = loc.split(" ");
        BMLogger.info(loc);

        return new Location(Bukkit.getWorld("world"), Double.parseDouble(components[0]), Double.parseDouble(components[1]), Double.parseDouble(components[2]));
    }

    public String getStringFromLocation(Location loc) {
        return loc.getX() + " " + loc.getY() + " " + loc.getZ();
    }

    public Vector calculateBounceAngle(Vector orig, WallOrientation wall) {
        double toFlip = getBounceFlip(orig, wall);
        VectorComponent comp = getFlippingComponent(wall);
        toFlip = -toFlip;
        if (comp == VectorComponent.X) {
            return new Vector(toFlip, orig.getY(), orig.getZ());
        }
        if (comp == VectorComponent.Y) {
            return new Vector(orig.getX(), toFlip, orig.getZ());
        }
        if (comp == VectorComponent.Z) {
            return new Vector(orig.getX(), orig.getY(), toFlip);
        }
        return null;
    }

    public VectorComponent getFlippingComponent(WallOrientation wall) {
        switch (wall) {
        case HORIZONTAL:
            return VectorComponent.X;
        case UP_DOWN:
            return VectorComponent.Z;
        case VERTICAL:
            return VectorComponent.Y;
        }
        return null;
    }

    public double getBounceFlip(Vector v, WallOrientation wall) {
        switch (getFlippingComponent(wall)) {
        case X:
            return v.getX();
        case Y:
            return v.getY();
        case Z:
            return v.getZ();
        }
        return -1.0D;
    }

    private enum VectorComponent {
        X, Y, Z;
    }

    public enum WallOrientation {
        HORIZONTAL, VERTICAL, UP_DOWN;
    }

    private static final ItemStack LABEL_ITEM = new ItemStack(Material.GHAST_TEAR);
    static {
        ItemMeta meta = LABEL_ITEM.getItemMeta();
        meta.displayName(Component.text("§cPing Pong Ball"));
        LABEL_ITEM.setItemMeta(meta);
        InventoryUtils.setItemLore(LABEL_ITEM, "§7Right click to throw a ping pong ball!");
    }

}