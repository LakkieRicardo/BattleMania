package net.battle.core.sql.impl;

import java.sql.ResultSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.ScoreboardHandler;
import net.battle.core.sql.pod.PlayerInfo;

public class PlayerInfoSql {

    private PlayerInfoSql() {
    }

    public static PlayerInfo getPlayerInfo(OfflinePlayer pl) {
        return getPlayerInfo(pl.getUniqueId().toString());
    }

    public static PlayerInfo getPlayerInfo(String uuid) {
        try {
            ResultSet rs = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT * FROM player WHERE UUID=?", uuid);
            if (rs.next()) {
                return new PlayerInfo(uuid, rs.getString("RANK"), rs.getInt("LEVEL"), rs.getInt("INGOT"), rs.getInt("TOKEN"), rs.getFloat("TIME"));
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Either inserts new player information if there is no row in the DB, or updates all the existing information
     * 
     * @param pi New player info whose values will be used to update the SQL DB
     */
    public static synchronized void updateOrInsertInfo(PlayerInfo pi) {
        try {
            ResultSet rsSearch = BMCorePlugin.ACTIVE_PLUGIN.sqlConn.queryAndFetch("SELECT uuid FROM player WHERE UUID=?", pi.playerUUID());
            if (rsSearch.next()) {
                // This row exists, update it
                BMCorePlugin.ACTIVE_PLUGIN.sqlConn.execute("UPDATE player SET `LEVEL`=?, `RANK`=?, `INGOT`=?, `TOKEN`=?, `TIME`=? WHERE `UUID`=?", pi.level(), pi.sqlRank(), pi.ingot(), pi.token(), pi.ingameHours(), pi.playerUUID());
            } else {
                BMCorePlugin.ACTIVE_PLUGIN.sqlConn.execute("INSERT INTO player (`UUID`, `LEVEL`, `RANK`, `INGOT`, `TOKEN`, `TIME`) VALUES (?, ?, ?, ?, ?, ?)", pi.playerUUID(), pi.level(), pi.sqlRank(), pi.ingot(), pi.token(), pi.ingameHours());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (Bukkit.getOfflinePlayer(UUID.fromString(pi.playerUUID())).isOnline()) {
            ScoreboardHandler.updateScoreboard((Player) Bukkit.getOfflinePlayer(UUID.fromString(pi.playerUUID())));
        }
    }

    // TODO: Make these functions into separate SQL UPDATE statements

    public static void setLevel(OfflinePlayer pl, int level) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withLevel(level));
    }

    public static void setRank(OfflinePlayer pl, String rank) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withRank(rank));
    }

    public static void setIngot(OfflinePlayer pl, int ingot) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withIngot(ingot));
    }

    public static void setToken(String pl, int token) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withToken(token));
    }

    public static void setTime(String pl, float time) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withIngameHours(time));
    }

    public static void setLevel(String uuid, int level) {
        PlayerInfo info = getPlayerInfo(uuid);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withLevel(level));
    }

    public static void setRank(String pl, String rank) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withRank(rank));
    }

    public static void setIngot(String pl, int ingot) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withIngot(ingot));
    }

    public static void setToken(OfflinePlayer pl, int token) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withToken(token));
    }

    public static void setTime(OfflinePlayer pl, float time) {
        PlayerInfo info = getPlayerInfo(pl);
        if (info == null) {
            return;
        }
        updateOrInsertInfo(info.withIngameHours(time));
    }
}