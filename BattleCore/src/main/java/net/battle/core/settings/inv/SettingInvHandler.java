package net.battle.core.settings.inv;

import java.util.ArrayList;
import java.util.List;

import net.battle.core.BMCorePlugin;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.plinv.PlayerInvLayout;
import net.battle.core.sql.impl.PlayerSettingsSql;

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

        invHub.getEffects().add(new SettingInvEffect());
        invGame.getEffects().add(new SettingInvEffect());
        invPublic.getEffects().add(new SettingInvEffect());
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
}