package net.battle.core.handlers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import net.kyori.adventure.text.Component;

public class NametagHandler {

    public static void integrateWithScoreboardNametag(Scoreboard board, Player pl, Rank rank) {
        String teamRankName = pl.getName();
        for (Player all : Bukkit.getOnlinePlayers()) {
            Scoreboard allBoard = all.getScoreboard();
            if (allBoard.getTeam(teamRankName) == null) {
                allBoard.registerNewTeam(teamRankName);
            }
            allBoard.getTeam(teamRankName).prefix(Component.text(rank.getGameName().equalsIgnoreCase("") ? "§a"
                    : (rank.getGameName() + "§a ")));
            allBoard.getTeam(teamRankName).addPlayer((OfflinePlayer) pl);
            all.setScoreboard(allBoard);
        }
    }
}