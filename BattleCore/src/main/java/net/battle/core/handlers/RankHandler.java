package net.battle.core.handlers;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.sql.impl.PlayerInfoSql;

public final class RankHandler {
    public static Rank getPlayerRank(Player pl) {
        String rank = PlayerInfoSql.getPlayerInfo((OfflinePlayer) pl).getSqlRank();
        for (Rank r : Rank.values()) {
            if (r.getSQLName().equalsIgnoreCase(rank)) {
                return r;
            }
        }
        return null;
    }

    public static Rank getRankFromName(String name) {
        for (Rank r : Rank.values()) {
            if (r.getSQLName().equalsIgnoreCase(name)) {
                return r;
            }
        }
        return null;
    }

    public static boolean playerPermission(Player pl) {
        return true;
    }

    public static boolean explorerPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.EXPLORER && r != Rank.EMPEROR && r != Rank.KING && r != Rank.HELPER && r != Rank.BUILDER && r != Rank.MODERATOR && r != Rank.EXPMOD
                && r != Rank.DEVELOPER && r != Rank.OPERATOR && !pl.isOp() && r != Rank.OWNER && !pl.isOp());
    }

    public static boolean emperorPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.EMPEROR && r != Rank.KING && r != Rank.HELPER && r != Rank.BUILDER && r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER
                && r != Rank.OPERATOR && !pl.isOp() && r != Rank.OWNER && !pl.isOp());
    }

    public static boolean kingPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.KING && r != Rank.HELPER && r != Rank.BUILDER && r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR
                && !pl.isOp() && r != Rank.OWNER && !pl.isOp());
    }

    public static boolean helperPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.HELPER && r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR && !pl.isOp() && r != Rank.OWNER
                && !pl.isOp());
    }

    public static boolean moderatorPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR && !pl.isOp() && r != Rank.OWNER && !pl.isOp());
    }

    public static boolean expModPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR && !pl.isOp() && r != Rank.OWNER && !pl.isOp());
    }

    public static boolean developerPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.DEVELOPER && r != Rank.OPERATOR && !pl.isOp() && r != Rank.OWNER && !pl.isOp());
    }

    public static boolean operatorPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.OPERATOR && !pl.isOp() && r != Rank.OWNER && !pl.isOp());
    }

    public static boolean ownerPermission(Player pl) {
        Rank r = getPlayerRank(pl);
        return !(r != Rank.OWNER && !pl.isOp());
    }

    public static boolean explorerPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.EXPLORER && r != Rank.EMPEROR && r != Rank.KING && r != Rank.HELPER && r != Rank.BUILDER && r != Rank.MODERATOR && r != Rank.EXPMOD
                && r != Rank.DEVELOPER && r != Rank.OPERATOR && r != Rank.OWNER);
    }

    public static boolean emperorPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.EMPEROR && r != Rank.KING && r != Rank.HELPER && r != Rank.BUILDER && r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER
                && r != Rank.OPERATOR && r != Rank.OWNER);
    }

    public static boolean kingPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.KING && r != Rank.HELPER && r != Rank.BUILDER && r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR
                && r != Rank.OWNER);
    }

    public static boolean helperPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.HELPER && r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR && r != Rank.OWNER);
    }

    public static boolean moderatorPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.MODERATOR && r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR && r != Rank.OWNER);
    }

    public static boolean expModPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.EXPMOD && r != Rank.DEVELOPER && r != Rank.OPERATOR && r != Rank.OWNER);
    }

    public static boolean developerPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.DEVELOPER && r != Rank.OPERATOR && r != Rank.OWNER);
    }

    public static boolean operatorPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return !(r != Rank.OPERATOR && r != Rank.OWNER);
    }

    public static boolean ownerPermission(String uuid) {
        Rank r = getRankFromName(PlayerInfoSql.getPlayerInfo(uuid).getSqlRank());
        return (r == Rank.OWNER);
    }

    public static String getAllRanks() {
        StringBuilder ranks = new StringBuilder();
        for (int i = 0; i < (Rank.values()).length; i++) {
            if (i == 0) {
                ranks.append(Rank.values()[i].getSQLName());
            } else {
                ranks.append(", ");
                ranks.append(Rank.values()[i].getGameName());
            }
        }
        return ranks.toString();
    }
}