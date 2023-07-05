package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.battle.core.BMCorePlugin;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.pod.PlayerInfo;
import net.kyori.adventure.text.Component;

public class ScoreboardHandler {
    public static final ScoreboardManager MANAGER = Bukkit.getScoreboardManager();
    public static final Scoreboard BLANK_BOARD = MANAGER.getNewScoreboard();
    public static final List<Scoreboard> REGISTERED = new ArrayList<>();

    public static final List<Player> CLEAR_SCOREBOARD = new ArrayList<>();

    public static void updateScoreboard(Player pl) {
        Scoreboard board = pl.getScoreboard();

        Team rank = board.getTeam("%rankname");
        Team level = board.getTeam("%level");
        Team ingot = board.getTeam("%ingot");
        Team token = board.getTeam("%token");
        Team time = board.getTeam("%time");

        PlayerInfo info = PlayerInfoSql.getPlayerInfo(pl);
        rank.suffix(Component.text(RankHandler.getRankFromSQLName(info.getSqlRank()).name()));
        level.suffix(Component.text(info.getLevel()));
        ingot.suffix(Component.text(info.getIngot()));
        token.suffix(Component.text(info.getToken()));
        time.suffix(Component.text(info.getTime() + " hours"));
        NametagHandler.integrateWithScoreboardNametag(board, pl, RankHandler.getPlayerRank(pl));
    }

    public static void setupScoreboard(Player pl) {
        Scoreboard board = setScoreboard(pl);
        String objName = genRandomString(14);
        // TODO need to figure out how to get server name
        Objective obj = board.registerNewObjective(objName, Criteria.DUMMY,
                Component.text("§a§l" + BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("servertitle") + " Test"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team rank = board.registerNewTeam("%rankname");
        Team level = board.registerNewTeam("%level");
        Team ingot = board.registerNewTeam("%ingot");
        Team token = board.registerNewTeam("%token");
        Team time = board.registerNewTeam("%time");
        obj.getScore("§0").setScore(15);
        obj.getScore("§cRank").setScore(14);
        rank.addEntry("§r§7");
        obj.getScore("§r§7").setScore(13);
        obj.getScore("§1").setScore(12);
        obj.getScore("§cLevel").setScore(11);
        level.addEntry("§r§r§7");
        obj.getScore("§r§r§7").setScore(10);
        obj.getScore("§2").setScore(9);
        obj.getScore("§cIngot").setScore(8);
        ingot.addEntry("§r§r§r§7");
        obj.getScore("§r§r§r§7").setScore(7);
        obj.getScore("§3").setScore(6);
        obj.getScore("§cToken").setScore(5);
        token.addEntry("§r§r§r§r§7");
        obj.getScore("§r§r§r§r§7").setScore(4);
        obj.getScore("§4").setScore(3);
        obj.getScore("§cIn-Game Time").setScore(2);
        time.addEntry("§r§r§r§r§r§7");
        obj.getScore("§r§r§r§r§r§7").setScore(1);
        NametagHandler.integrateWithScoreboardNametag(board, pl, RankHandler.getPlayerRank(pl));
    }

    private static Scoreboard setScoreboard(Player pl) {
        Scoreboard board = MANAGER.getNewScoreboard();
        pl.setScoreboard(board);
        REGISTERED.add(board);
        return board;
    }

    public static void updateScoreboards() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            updateScoreboard(pl);
        }
    }

    public static void setupScoreboards() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            setupScoreboard(pl);
        }
    }

    public static void toggleClearScoreboard(Player pl) {
        if (!CLEAR_SCOREBOARD.contains(pl)) {
            CLEAR_SCOREBOARD.add(pl);
        } else {
            CLEAR_SCOREBOARD.remove(pl);
        }
    }

    public static void enableClearScoreboard(Player pl) {
        if (!CLEAR_SCOREBOARD.contains(pl)) {
            CLEAR_SCOREBOARD.add(pl);
        }
    }

    public static void disableClearScoreboard(Player pl) {
        if (CLEAR_SCOREBOARD.contains(pl)) {
            CLEAR_SCOREBOARD.remove(pl);
        }
    }

    public static void toggleClearScoreboards() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            toggleClearScoreboard(pl);
        }
    }

    public static String genRandomString(int length) {
        char[] chars = new char[length];
        char[] allRandom = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890".toCharArray();
        for (int i = 0; i < length; i++) {
            chars[i] = allRandom[(new Random()).nextInt(allRandom.length)];
        }
        return new String(chars);
    }
}