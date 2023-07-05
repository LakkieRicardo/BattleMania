package net.battle.core.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.BMTextConvert;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.Rank;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.records.PlayerInfo;
import net.kyori.adventure.text.Component;

public class SetRankCommand implements CommandBase {
    public String getLabel() {
        return "setrank";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Set someone's rank";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.operatorPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }

        if (args.length != 2) {
            CommandHandler.sendUsage(pl, this);
            return;
        }

        String targetName = args[0];
        if (!ProxyHandler.hasPlayerPlayedBefore(targetName)) {
            pl.sendMessage(Prefixes.ERROR + "Invalid player.");
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (RankHandler.getRankFromSQLName(args[1]) == null) {
            pl.sendMessage(Prefixes.ERROR + "Invalid rank.");
            return;
        }
        Rank r = RankHandler.getRankFromSQLName(args[1]);
        if (!RankHandler.ownerPermission(pl) && (r == Rank.OWNER || r == Rank.OPERATOR)) {
            pl.sendMessage(Prefixes.ERROR + "You do not have enough permission to set that player to that rank");
            return;
        }

        PlayerInfo info = PlayerInfoSql.getPlayerInfo((OfflinePlayer) pl);
        info = info.withRank(r.getSQLName());
        PlayerInfoSql.setRank(target, r.getSQLName());
        pl.sendMessage(
                Prefixes.UPDATE + "You updated §c" + target.getName() + "§f's rank to §c" + r.getSQLName() + "§f.");
        Player targetOnline;
        if ((targetOnline = Bukkit.getPlayerExact(targetName)) != null) {
            targetOnline.sendMessage(
                    Prefixes.UPDATE + "Your rank has been updated to §c" + r.getSQLName() + " §fby §c" + pl.getName()
                            + "§f.");
            targetOnline
                    .playerListName(Component.text(RankHandler.getRankFromSQLName(info.sqlRank()).getGameName() + " §a"
                            + targetOnline.getName()));
            targetOnline.playerListName(Component.text(BMTextConvert.CTS.serialize(targetOnline.playerListName()).trim()));
        } else {
            ProxyHandler.playerStatUpdated(pl.getName(), target.getUniqueId(), "rank", r.getSQLName());
        }
    }

    @Override
    public String getUsage() {
        return "/setrank <player> <rank>";
    }
}