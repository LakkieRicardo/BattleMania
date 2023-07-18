package net.battle.core.settings.inv;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.RankHandler;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutHolder;
import net.battle.core.layouts.plinv.PlayerInvData;
import net.battle.core.layouts.plinv.PlayerInvLayout;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.impl.PlayerSettingsSql;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SettingInvHandler {

    private final PlayerInvLayout invHome, invHub, invGame, invPublic;

    /**
     * Loads all of the inventory layouts
     * 
     * @throws RuntimeException Thrown if one of the layout definitions is missing from InventoryLayouts.json.
     */
    public SettingInvHandler() throws RuntimeException {
        invHome = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("settings_home").orElseThrow(() -> new RuntimeException("settings_home layout must be defined")));
        invHub = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("settings_hub").orElseThrow(() -> new RuntimeException("settings_hub layout must be defined")));
        invGame = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("settings_game").orElseThrow(() -> new RuntimeException("settings_game layout must be defined")));
        invPublic = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("settings_public").orElseThrow(() -> new RuntimeException("settings_public layout must be defined")));

        invHub.getEffects().add(SettingInvHandler::applyEffect);
        invGame.getEffects().add(SettingInvHandler::applyEffect);
        invPublic.getEffects().add(SettingInvHandler::applyEffect);
    }

    public PlayerInvLayout getHomeLayout() {
        return invHome;
    }

    public PlayerInvLayout getHubLayout() {
        return invHub;
    }

    public PlayerInvLayout getGameLayout() {
        return invGame;
    }

    public PlayerInvLayout getPublicLayout() {
        return invPublic;
    }

    public static String getSettingDisplayName(String settingName) {
        return BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("settingDisplayNames." + settingName);
    }

    // TODO: Update this javadoc when we move to the settings to Bungee
    /**
     * Retrieves a specific player's settings that are missing in the SQL database based off the player UUID. Settings are
     * stored under "settingList" in the BMSettings interface and defaults under "settingDefaults"
     *
     * @param playerUUID Settings the player belongs to
     * @return A list of all of the missing settings, or an empty list if there are none missing
     */
    public static List<String> getPlayerMissingSettings(String playerUUID) {
        List<String> settingsList = BMCorePlugin.ACTIVE_PLUGIN.getSettingsStringList("settingList");
        List<String> missingSettings = new ArrayList<>();
        for (String setting : settingsList) {
            if (!PlayerSettingsSql.playerHasSetting(playerUUID, setting)) {
                missingSettings.add(setting);
            }
        }
        return missingSettings;
    }

    /**
     * Adds default settings in database. WARNING: Please don't give this function false information, use the
     * {@link #getPlayerMissingSettings(String)} function. It will create duplicates in the database if you do
     *
     * @param playerUUID Player to add the settings with default values to
     */
    public static void insertPlayerDefaultSettings(String playerUUID, List<String> missingPlayerSettings) {
        for (String setting : missingPlayerSettings) {
            boolean defaultValue = getDefaultSettingValue(setting);
            PlayerSettingsSql.insertSetting(playerUUID, setting, defaultValue);
        }
    }

    public static boolean getDefaultSettingValue(String setting) {
        String defaultValueKey = "settingDefaults." + setting;
        if (!BMCorePlugin.ACTIVE_PLUGIN.getSettingsContains(defaultValueKey)) {
            throw new RuntimeException("Could not find default setting for " + setting + " using BMSettings key \"" + defaultValueKey + "\"");
        }
        return BMCorePlugin.ACTIVE_PLUGIN.getSettingsBoolean(defaultValueKey);
    }

    private static void applyEffect(Inventory inv, Player viewer) {
        var holder = (LayoutHolder) inv.getHolder();
        var layout = (PlayerInvLayout) holder.getLayout();
        var data = (PlayerInvData) holder.getData();

        OfflinePlayer targetPlayer = data.target();
        for (int i = 0; i < layout.getLayout().length(); i++) {
            char current = layout.getLayout().charAt(i);
            JSONObject definition = layout.getItemDefines().get(current);
            if (definition.containsKey("button_id")) {
                String buttonId = (String) definition.get("button_id");
                if (buttonId.equals("back")) {
                    continue;
                }
                ItemStack currentItem = inv.getItem(i);
                boolean currentSetting = PlayerSettingsSql.getSetting(targetPlayer.getUniqueId().toString(), buttonId);
                if ((buttonId.equals("public.showswears") && PlayerInfoSql.getPlayerInfo(targetPlayer).ingameHours() < 5.0F
                        && !RankHandler.ownerPermission(targetPlayer.getUniqueId().toString()))
                        || (buttonId.equals("hub.antivelocity") && !RankHandler.moderatorPermission(targetPlayer.getUniqueId().toString()))
                        || (buttonId.equals("hub.forcefield") && !RankHandler.operatorPermission(targetPlayer.getUniqueId().toString()))) {
                    // Mark this item as inaccessible
                    InventoryUtils.renameItem(currentItem, "§4" + SettingInvHandler.getSettingDisplayName(buttonId));
                    InventoryUtils.insertItemLore(currentItem, "§4§lINACCESSIBLE", 0);
                    currentItem.setType(Material.IRON_BARS);
                } else {
                    InventoryUtils.renameItem(currentItem, "§a" + SettingInvHandler.getSettingDisplayName(buttonId));
                }
                Component defaultComp;
                if (SettingInvHandler.getDefaultSettingValue(buttonId)) {
                    defaultComp = Component.text("enabled").color(TextColor.color(128, 213, 128));
                } else {
                    defaultComp = Component.text("disabled").color(TextColor.color(213, 128, 128));
                }
                InventoryUtils.addItemLore(currentItem, Component.text("Default: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(defaultComp));
                InventoryUtils.addItemLore(currentItem, currentSetting ? "§a§lENABLED" : "§c§lDISABLED");
                if (currentSetting) {
                    currentItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                }
                ItemMeta itemMeta = currentItem.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                currentItem.setItemMeta(itemMeta);
                inv.setItem(i, currentItem);
            }
        }

    }
}