package net.battle.test.security;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.battle.test.BMTestPlugin;
import net.kyori.adventure.text.Component;

public class SecurityCamera {
    private static List<SecurityCamera> registeredCameras = new ArrayList<>();

    private String name;
    private String armorStand;
    private String armorStandWorld;

    private final Location location;
    private ArmorStand loadedStand;

    public SecurityCamera(String name, String armorStand, Location loc) {
        this.name = name;
        this.armorStand = armorStand;
        this.location = loc;
        setArmorStandWorld(loc.getWorld().getName());
        createArmorStand();
        registeredCameras.add(this);
    }

    private void createArmorStand() {
        this.loadedStand = (ArmorStand) this.location.getWorld().spawn(this.location, ArmorStand.class);
        this.loadedStand.setVisible(false);
        this.loadedStand.setGravity(false);
        this.loadedStand.setCanPickupItems(false);
        ItemStack skull = new ItemStack(Material.SKELETON_SKULL);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(SecurityHandler.SECURITY_USERNAME));
        skull.setItemMeta((ItemMeta) meta);
        this.loadedStand.getEquipment().setHelmet(skull);
        ;
        this.loadedStand.customName(Component.text(SecurityHandler.SECURITY_PREFIX + this.name));
        this.loadedStand.setCustomNameVisible(true);
    }

    public void deleteArmorStand() {
        World world = this.location.getWorld();
        for (LivingEntity le : world.getLivingEntities()) {
            if (le.getUniqueId().toString().equalsIgnoreCase(this.armorStand)) {
                le.remove();
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArmorStand() {
        return this.armorStand;
    }

    public void setArmorStand(String armorStand) {
        this.armorStand = armorStand;
    }

    public void saveToConfig() {
    }

    public Location getLocation() {
        return this.location;
    }

    public String getArmorStandWorld() {
        return this.armorStandWorld;
    }

    public void setArmorStandWorld(String armorStandWorld) {
        this.armorStandWorld = armorStandWorld;
    }

    public static SecurityCamera readFromConfig(String name) {
        if (isCameraLoaded(name)) {
            return getByName(name);
        }
        FileConfiguration config = BMTestPlugin.ACTIVE_PLUGIN.getConfig();
        String armorStand = config.getString(name + ".armor stand");
        if (armorStand == null || armorStand.equals("")) {
            return null;
        }
        Location location = null;
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity le : world.getLivingEntities()) {
                if (le.getUniqueId().toString().equalsIgnoreCase(armorStand)) {
                    location = le.getLocation();
                }
            }
        }
        if (location == null) {
            return null;
        }

        return new SecurityCamera(name, armorStand, location);
    }

    public static SecurityCamera getByName(String name) {
        for (SecurityCamera camera : registeredCameras) {
            if (camera.getName().equalsIgnoreCase(name)) {
                return camera;
            }
        }
        return null;
    }

    public static void registerCamera(String name) {
    }

    public static boolean doesCameraExist(String name) {
        return BMTestPlugin.ACTIVE_PLUGIN.getConfig().getStringList("Cameras").contains(name);
    }

    public static boolean isCameraLoaded(String name) {
        return (getByName(name) != null);
    }

    public static List<SecurityCamera> getRegisteredCameras() {
        return registeredCameras;
    }
}