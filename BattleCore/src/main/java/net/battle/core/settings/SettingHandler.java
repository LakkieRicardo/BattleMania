package net.battle.core.settings;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.Prefixes;
import net.battle.core.sql.impl.PlayerSettingsSql;
import net.kyori.adventure.text.Component;

public class SettingHandler {
    public static Inventory getSettingsInventory(String name) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Stats: " + name));
        ItemStack hub = new ItemStack(Material.PAPER);
        InventoryUtils.renameItem(hub, "§aHub Settings");
        InventoryUtils.setItem(inv, 2, 1, hub);
        ItemStack game = new ItemStack(Material.REPEATER);
        InventoryUtils.renameItem(game, "§aGame Settings");
        InventoryUtils.setItem(inv, 4, 1, game);
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta(meta);
        InventoryUtils.renameItem(skull, "§aPublic Settings");
        InventoryUtils.setItem(inv, 6, 1, skull);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static void updateSettingItem(ItemStack item, String stat, String uuid) {
        boolean enabled = PlayerSettingsSql.getSetting(uuid, stat);
        InventoryUtils.addItemLore(item, enabled ? "§a§lENABLED" : "§c§lDISABLED");
        if (enabled) {
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        }
    }

    public static String getSettingDisplayName(String settingName) {
        return "§a" + BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("settingDisplayNames." + settingName);
    }

    public static Inventory getGameSettings(String uuid, String name) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Game Stats: " + name));
        ItemStack leaveDeath = new ItemStack(Material.SKELETON_SKULL);
        InventoryUtils.renameItem(leaveDeath, getSettingDisplayName("game.leaveondeath"));
        InventoryUtils.setItemLore(leaveDeath, "§7Click to toggle leaving on death");
        updateSettingItem(leaveDeath, "game.leaveondeath", uuid);
        InventoryUtils.setItem(inv, 3, 1, leaveDeath);
        ItemStack particleIngame = new ItemStack(Material.REDSTONE);
        InventoryUtils.renameItem(particleIngame, getSettingDisplayName("game.particles"));
        InventoryUtils.setItemLore(particleIngame, "§7Click to toggle if you want to show particles ingame");
        updateSettingItem(particleIngame, "game.particles", uuid);
        InventoryUtils.setItem(inv, 5, 1, particleIngame);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getPublicSettings(String uuid, String name) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Public Stats: " + name));
        ItemStack playerChat = new ItemStack(Material.MAP);
        InventoryUtils.renameItem(playerChat, getSettingDisplayName("public.playerchat"));
        InventoryUtils.setItemLore(playerChat, "§7Click to toggle being able to see other players' messages");
        updateSettingItem(playerChat, "public.playerchat", uuid);
        InventoryUtils.setItem(inv, 1, 1, playerChat);
        ItemStack privateMessage = new ItemStack(Material.PAPER);
        InventoryUtils.renameItem(privateMessage, getSettingDisplayName("public.privatemsgs"));
        InventoryUtils.setItemLore(playerChat, "§7Click to toggle private messages");
        updateSettingItem(privateMessage, "public.privatemsgs", uuid);
        InventoryUtils.setItem(inv, 3, 1, privateMessage);
        ItemStack party = new ItemStack(Material.NAME_TAG);
        InventoryUtils.renameItem(party, getSettingDisplayName("public.partyrequests"));
        InventoryUtils.setItemLore(party, "§7Click to toggle party requests");
        updateSettingItem(party, "public.partyrequests", uuid);
        InventoryUtils.setItem(inv, 5, 1, party);
        ItemStack swear = new ItemStack(Material.NAME_TAG);
        InventoryUtils.renameItem(swear, getSettingDisplayName("public.showswears"));
        InventoryUtils.setItemLore(swear, "§7Disables or enables showing swear words");
        InventoryUtils.addItemLore(swear, "§e§lWARN: §7You must have 5+ hours in-game");
        InventoryUtils.addItemLore(swear, "§7to enable this.");
        updateSettingItem(swear, "public.showswears", uuid);
        InventoryUtils.setItem(inv, 7, 1, swear);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getHubSettings(String uuid, String name) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Hub Stats: " + name));
        ItemStack player = new ItemStack(Material.ENDER_PEARL);
        InventoryUtils.renameItem(player, getSettingDisplayName("hub.showplayers"));
        InventoryUtils.setItemLore(player, "§7Click to toggle being able to see other players in Lobby");
        updateSettingItem(player, "hub.showplayers", uuid);
        InventoryUtils.setItem(inv, 2, 1, player);
        ItemStack launching = new ItemStack(Material.FIREWORK_STAR);
        InventoryUtils.renameItem(launching, getSettingDisplayName("hub.playerlaunch"));
        InventoryUtils.setItemLore(launching, "§7Click to toggle player launching");
        updateSettingItem(launching, "hub.playerlaunch", uuid);
        InventoryUtils.setItem(inv, 4, 1, launching);
        ItemStack sprint = new ItemStack(Material.FEATHER);
        InventoryUtils.renameItem(sprint, getSettingDisplayName("hub.fastsprint"));
        InventoryUtils.setItemLore(sprint, "§7Click to toggle fast sprint");
        updateSettingItem(sprint, "hub.fastsprint", uuid);
        InventoryUtils.setItem(inv, 6, 1, sprint);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getModeratorHubSettings(String uuid, String name) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Hub Stats: " + name));
        ItemStack player = new ItemStack(Material.ENDER_PEARL);
        InventoryUtils.renameItem(player, getSettingDisplayName("hub.showplayers"));
        InventoryUtils.setItemLore(player, "§7Click to toggle being able to see other players in Lobby");
        updateSettingItem(player, "hub.showplayers", uuid);
        InventoryUtils.setItem(inv, 1, 1, player);
        ItemStack launching = new ItemStack(Material.FIREWORK_STAR);
        InventoryUtils.renameItem(launching, getSettingDisplayName("hub.playerlaunch"));
        InventoryUtils.setItemLore(launching, "§7Click to toggle player launching");
        updateSettingItem(launching, "hub.playerlaunch", uuid);
        InventoryUtils.setItem(inv, 3, 1, launching);
        ItemStack sprint = new ItemStack(Material.FEATHER);
        InventoryUtils.renameItem(sprint, getSettingDisplayName("hub.fastsprint"));
        InventoryUtils.setItemLore(sprint, "§7Click to toggle fast sprint");
        updateSettingItem(sprint, "hub.fastsprint", uuid);
        InventoryUtils.setItem(inv, 7, 1, sprint);
        ItemStack velocity = new ItemStack(Material.FEATHER);
        InventoryUtils.renameItem(velocity, getSettingDisplayName("hub.antivelocity"));
        InventoryUtils.setItemLore(velocity, "§7Click to toggle anti-velocity");
        updateSettingItem(velocity, "hub.antivelocity", uuid);
        InventoryUtils.setItem(inv, 5, 1, velocity);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getOperatorHubSettings(String uuid, String name) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Hub Stats: " + name));
        ItemStack player = new ItemStack(Material.ENDER_PEARL);
        InventoryUtils.renameItem(player, getSettingDisplayName("hub.showplayers"));
        InventoryUtils.setItemLore(player, "§7Click to toggle being able to see other players in Lobby");
        updateSettingItem(player, "hub.showplayers", uuid);
        InventoryUtils.setItem(inv, 0, 1, player);
        ItemStack launching = new ItemStack(Material.FIREWORK_STAR);
        InventoryUtils.renameItem(launching, getSettingDisplayName("hub.playerlaunch"));
        InventoryUtils.setItemLore(launching, "§7Click to toggle player launching");
        updateSettingItem(launching, "hub.playerlaunch", uuid);
        InventoryUtils.setItem(inv, 2, 1, launching);
        ItemStack sprint = new ItemStack(Material.FEATHER);
        InventoryUtils.renameItem(sprint, getSettingDisplayName("hub.fastsprint"));
        InventoryUtils.setItemLore(sprint, "§7Click to toggle fast sprint");
        updateSettingItem(sprint, "hub.fastsprint", uuid);
        InventoryUtils.setItem(inv, 8, 1, sprint);
        ItemStack velocity = new ItemStack(Material.FEATHER);
        InventoryUtils.renameItem(velocity, getSettingDisplayName("hub.antivelocity"));
        InventoryUtils.setItemLore(velocity, "§7Click to toggle anti-velocity");
        updateSettingItem(velocity, "hub.antivelocity", uuid);
        InventoryUtils.setItem(inv, 6, 1, velocity);
        ItemStack field = new ItemStack(Material.BARRIER);
        InventoryUtils.renameItem(field, getSettingDisplayName("hub.forcefield"));
        InventoryUtils.setItemLore(field, "§7Click to toggle force field");
        updateSettingItem(field, "hub.forcefield", uuid);
        InventoryUtils.setItem(inv, 4, 1, field);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    // TODO: This function needs to be able to figure out if this is a server-created inventory is just a renamed chest
    /**
     * Updated function to handle setting inventory clicks. It will change the clicked item to the updated setting, which it figures out by looking at the clicked item.
     * 
     * @param settingName SQL name of the setting to be updated
     * @param player Player who did the update
     * @param targetPlayer Player who receives the update. This can match the "player" argument
     */
    public static void handlePlayerUpdatedSettingGUI(InventoryClickEvent event, String settingName, Player player, OfflinePlayer targetPlayer) {
        ItemStack clickedItem = event.getCurrentItem();

        // Modify the clicked item
        boolean newSettingValue = !clickedItem.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Component enabledLoreValue = Component.text(newSettingValue ? "§a§lENABLED" : "§c§lDISABLED");
        List<Component> lore = InventoryUtils.getItemLoreAsComps(clickedItem);
        if (lore.size() == 0) {
            lore.add(enabledLoreValue);
        } else {
            lore.set(lore.size() - 1, enabledLoreValue);
        }
        InventoryUtils.setItemLore(clickedItem, lore);
        if (newSettingValue) {
            clickedItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        } else  {
            clickedItem.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        }

        // Update setting and notify player
        event.setCancelled(true);
        event.setCurrentItem(clickedItem);
        PlayerSettingsSql.updateSetting(player.getUniqueId().toString(), settingName, newSettingValue);
        String settingDisplayName = BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("settingDisplayNames." + settingName);
        if (player.getUniqueId().equals(targetPlayer.getUniqueId())) {
            player.sendMessage(Prefixes.ALERT + "Set §c" + settingDisplayName + "§f to " + newSettingValue);
        } else {
            player.sendMessage(Prefixes.ALERT + "Set §c" + settingDisplayName + "§fof §c" + targetPlayer.getName() + "§f to " + newSettingValue);
            if (targetPlayer.isOnline()) {
                Player targetOnline = Bukkit.getPlayer(targetPlayer.getUniqueId());
                targetOnline.sendMessage(Prefixes.ALERT + "Player §c" + player.getName() + "§f updated your §c" + settingDisplayName + "§f setting to " + newSettingValue);
            }
        }
    }

    // TODO: Update this javadoc when we move to the settings to Bungee
    /**
     * Retrieves a specific player's settings that are missing in the SQL database
     * based off the player UUID. Settings are stored under "settingList" in the
     * BMSettings interface and defaults under "settingDefaults"
     * 
     * @param playerUUID Settings the player belongs to
     * @return A list of all of the missing settings, or an empty list if there are
     *         none missing
     */
    public static List<String> getPlayerMissingSettings(String playerUUID) {
        List<String> settingsList = BMCorePlugin.ACTIVE_PLUGIN.getSettingsStringList("settingList");
        List<String> missingSettings = new ArrayList<>();
        for (String setting : settingsList) {
            if (!PlayerSettingsSql.doesSettingExist(playerUUID, setting)) {
                missingSettings.add(setting);
            }
        }
        return missingSettings;
    }

    /**
     * Adds default settings in database. WARNING: Please don't give this function
     * false information, use the {@link #getPlayerMissingSettings(String)}
     * function. It will create duplicates in the database if you do
     * 
     * @param playerUUID Player to add the settings with default values to
     */
    public static void insertPlayerDefaultSettings(String playerUUID, List<String> missingPlayerSettings) {
        for (String setting : missingPlayerSettings) {
            String defaultValueKey = "settingDefaults." + setting;
            if (!BMCorePlugin.ACTIVE_PLUGIN.getSettingsContains(defaultValueKey)) {
                throw new RuntimeException("Could not find default setting for " + setting + " using BMSettings key \""
                        + defaultValueKey + "\"");
            }
            boolean defaultValue = BMCorePlugin.ACTIVE_PLUGIN.getSettingsBoolean(defaultValueKey);
            PlayerSettingsSql.insertSetting(playerUUID, setting, defaultValue);
        }
    }
}