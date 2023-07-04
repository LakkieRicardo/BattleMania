package net.battle.core.handlers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.sql.impl.PlayerInfoSql;

public class TimeHandler {
    public static void updateTime() {
        for (Player pl : Bukkit.getOnlinePlayers())
            PlayerInfoSql.setTime((OfflinePlayer) pl, PlayerInfoSql.getPlayerInfo((OfflinePlayer) pl).getTime() + 0.1F);
    }
}