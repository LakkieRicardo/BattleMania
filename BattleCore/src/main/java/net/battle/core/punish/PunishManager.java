package net.battle.core.punish;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.punish.gui.ManageUserHandler;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.records.PlayerPunishInfo;
import net.battle.core.sql.records.PunishType;

public class PunishManager {
    public static final int MUTE_SEVERITY_1 = 1;
    public static final int MUTE_SEVERITY_2 = 7;
    public static final int MUTE_SEVERITY_3 = 31;
    public static final int EXPLOT_SEVERITY_1 = 5;
    public static final int CLIENT_SEVERITY_1 = 1;
    public static final int CLIENT_SEVERITY_2 = 7;
    public static final int CLIENT_SEVERITY_3 = 31;
    public static final int PERMANENT_SEVERITY = -1;

    public static final SimpleDateFormat PUNISHMENT_DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");
    public static final SimpleDateFormat PUNISHMENT_TIME_FORMAT = new SimpleDateFormat("hh:mm:ss aa zzz");

    public static void updatePlayerPunishments(String uuid, PunishType type) {
        List<PlayerPunishInfo> punishments = PunishmentSql.getPlayerActivePunishments(uuid, type);
        for (PlayerPunishInfo punishment : punishments) {
            if (punishment.expiration() != null && System.currentTimeMillis() > punishment.expiration().getTime()) {
                PunishmentSql.updatePunishmentIsActive(punishment.id(), false);
            }
        }
    }

    public static List<String> getBanMessage(PlayerPunishInfo ban) {
        List<String> lines = new ArrayList<>();
        lines.add(BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("servertitle") + " §c§lPunishments");
        lines.add("§7You have been §c§lBANNED§7!");
        lines.add("§7By: §c" + Bukkit.getOfflinePlayer(UUID.fromString(ban.punisherUUID())).getName());
        lines.add("§7Reason: §c" + ban.reason());
        if (ban.expiration() == null) {
            lines.add("§7Ban duration: §c§lPERMANENT§7!");
        } else {
            lines.add("§7Ban duration: §c§l" + PUNISHMENT_DATE_FORMAT.format(ban.expiration()) + " at " + PUNISHMENT_TIME_FORMAT.format(ban.expiration()));
        }
        lines.add("");
        lines.add("§7Appeal at: §c" + BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("websiteurl") + "/appeal");
        return lines;
    }

    public static List<String> getMuteMessages(PlayerPunishInfo mute) {
        List<String> lines = new ArrayList<>();
        lines.add("§7You are muted!");
        lines.add("§7Reason: §c" + mute.reason());
        lines.add("§7By: §c" + Bukkit.getOfflinePlayer(UUID.fromString(mute.punisherUUID())).getName());
        if (mute.expiration() == null) {
            lines.add("§7Expiration: §cNever");
        } else {
            lines.add("§7Expiration: §c" + PUNISHMENT_DATE_FORMAT.format(mute.expiration()) + " at " + PUNISHMENT_TIME_FORMAT.format(mute.expiration()));
        }
        lines.add("");
        lines.add("§7Appeal At: §c" + BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("websiteurl") + "/appeal");
        return lines;
    }

    /**
     * Handles disabling a punishment in SQL
     * 
     * @param disabler      The player who is disabling the punishment (will be sent the message)
     * @param targetOffline The player whose punishment is being disabled
     * @param punishType
     * @param clickedItem
     * @return Whether the punishment was found and disabled
     */
    public static boolean handleDisablePunishment(Player disabler, OfflinePlayer targetOffline, PunishType punishType, ItemStack clickedItem) {
        List<String> itemLore = InventoryUtils.getItemLore(clickedItem);
        boolean foundId = false;
        int punishmentId = 0;
        for (String lore : itemLore) {
            if (lore.startsWith(ManageUserHandler.PUNISH_ID_FIELD_PREFIX)) {
                try {
                    punishmentId = Integer.parseInt(lore.substring(ManageUserHandler.PUNISH_ID_FIELD_PREFIX.length()));
                    foundId = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        }
        if (!foundId) {
            BMLogger.warning("Could not find ID for " + targetOffline.getName() + "'s punishment");
            return false;
        }
        try {
            PunishmentSql.updatePunishmentIsActive(punishmentId, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;

    }
}