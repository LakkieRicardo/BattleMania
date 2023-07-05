package net.battle.core.sql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.battle.core.BMCorePlugin;
import net.battle.core.sql.records.PlayerPunishInfo;
import net.battle.core.sql.records.PunishType;

/**
 * SQL Functions to interact with punishments table
 */
public class PunishmentSql {

    public static List<PlayerPunishInfo> getPlayerPunishmentHistory(String uuid) {
        List<PlayerPunishInfo> activePunishs = new ArrayList<>();
        try {
            ResultSet rs = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT `id`, `punishee_uuid`, `punisher_uuid`, `expiration`, `is_active`, `reason`, `punishment_type` FROM punishments WHERE `punishee_uuid`=? ORDER BY `expiration` DESC;", uuid);
            while (rs.next()) {
                activePunishs.add(new PlayerPunishInfo(rs.getInt("id"), rs.getString("punishee_uuid"), rs.getString("punisher_uuid"), rs.getDate("expiration"), rs.getBoolean("is_active"), PunishType.valueOf(rs.getString("punishment_type").toUpperCase()), rs.getString("reason")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return activePunishs;
    }

    public static List<PlayerPunishInfo> getPlayerPunishmentHistory(String uuid, PunishType type) {
        List<PlayerPunishInfo> activePunishs = new ArrayList<>();
        try {
            ResultSet rs = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT `id`, `punishee_uuid`, `punisher_uuid`, `expiration`, `is_active`, `reason` FROM punishments WHERE `punishee_uuid`=? AND `punishment_type`=? ORDER BY `expiration` DESC;", uuid, type.name().toLowerCase());
            while (rs.next()) {
                activePunishs.add(new PlayerPunishInfo(rs.getInt("id"), rs.getString("punishee_uuid"), rs.getString("punisher_uuid"), rs.getDate("expiration"), rs.getBoolean("is_active"), type, rs.getString("reason")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return activePunishs;
    }

    public static List<PlayerPunishInfo> getPlayerActivePunishments(String uuid, PunishType type) {
        List<PlayerPunishInfo> allPunishs = new ArrayList<>();
        try {
            ResultSet rs = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT `id`, `punishee_uuid`, `punisher_uuid`, `expiration`, `is_active`, `reason` FROM punishments WHERE `punishee_uuid`=? AND `punishment_type`=? AND `is_active`=true ORDER BY `expiration` DESC;", uuid, type.name().toLowerCase());
            while (rs.next()) {
                allPunishs.add(new PlayerPunishInfo(rs.getInt("id"), rs.getString("punishee_uuid"), rs.getString("punisher_uuid"), rs.getDate("expiration"), rs.getBoolean("is_active"), type, rs.getString("reason")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return allPunishs;
    }

    public static PlayerPunishInfo getPlayerPunishmentById(int id) {
        try {
            ResultSet rs = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT `id`, `punishee_uuid`, `punisher_uuid`, `expiration`, `is_active`, `reason` FROM punishments WHERE `id`=?;", id);
            if (rs.next()) {
                return new PlayerPunishInfo(rs.getInt("id"), rs.getString("punishee_uuid"), rs.getString("punisher_uuid"), rs.getDate("expiration"), rs.getBoolean("is_active"), PunishType.valueOf(rs.getString("punishment_type").toUpperCase()), rs.getString("reason"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int insertNewPlayerPunishment(PlayerPunishInfo info) {
        try {
            ResultSet rsKeys = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.executeAndFetchGeneratedKeys("INSERT INTO punishments (punishee_uuid, punisher_uuid, expiration, is_active, punishment_type, reason) VALUES (?, ?, ?, ?, ?, ?)", info.recipientUUID(), info.punisherUUID(), info.expiration(), info.isActive(), info.type().name().toLowerCase(), info.reason());
            if (rsKeys.next()) {
                return rsKeys.getInt(1);
            } else {
                throw new SQLException("Failed to insert punishment " + info);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Updates whether a punishment is active. This should always be called before using the punishment.
     * 
     * @param id       ID of the row
     * @param isActive Value to update to
     */
    public static void updatePunishmentIsActive(int id, boolean isActive) {
        BMCorePlugin.ACTIVE_PLUGIN.sqlConn.execute("UPDATE punishments SET is_active=? WHERE id=?", isActive, id);
    }

    public static void deletePunishment(int id) {
        BMCorePlugin.ACTIVE_PLUGIN.sqlConn.execute("DELETE FROM punishments WHERE id=?", id);
    }
}