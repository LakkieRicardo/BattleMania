package net.battle.core.sql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.BMLogger;

public class PlayerSettingsSql {

    public static synchronized void updateSetting(String uuid, String name, boolean value) {
        BMLogger.info("Updating player setting " + uuid + "#" + name + " to " + value);
        if (!doesSettingExist(uuid, name)) {
            insertSetting(uuid, name, value);
            return;
        }

        BMCorePlugin.ACTIVE_PLUGIN.sqlConn.execute("UPDATE player_settings SET flag_value=? WHERE uuid=? AND setting_name=?", value, uuid, name);
    }

    public static synchronized void insertSetting(String uuid, String name, boolean value) {
        BMCorePlugin.ACTIVE_PLUGIN.sqlConn.execute("INSERT INTO player_settings (uuid, setting_name, flag_value) VALUES (?, ?, ?)", uuid, name, value);
    }

    public static synchronized boolean doesSettingExist(String uuid, String name) {
        try {
            ResultSet rs = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT * FROM player_settings WHERE uuid=? AND setting_name=?", uuid, name);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized boolean getSetting(String uuid, String name) {
        try {
            ResultSet rs = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT * FROM player_settings WHERE uuid=? AND setting_name=?", uuid, name);
            if (rs.next()) {
                return rs.getBoolean("flag_value");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}