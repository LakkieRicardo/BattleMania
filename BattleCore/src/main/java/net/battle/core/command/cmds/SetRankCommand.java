package net.battle.core.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.BMTextConvert;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Rank;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.pod.PlayerInfo;
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
            pl.sendMessage("§4§lERROR§8 > §cInvalid player.");
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (RankHandler.getRankFromSQLName(args[1]) == null) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid rank.");
            return;
        }
        Rank r = RankHandler.getRankFromSQLName(args[1]);
        if (!RankHandler.ownerPermission(pl) && (r == Rank.OWNER || r == Rank.OPERATOR)) {
            pl.sendMessage("§4§lERROR§8 > §cYou do not have enough permission to set that player to that rank");
            return;
        }

        PlayerInfo info = PlayerInfoSql.getPlayerInfo((OfflinePlayer) pl);
        info.setSqlRank(r.getSQLName());
        PlayerInfoSql.setRank(target, r.getSQLName());
        pl.sendMessage(
                "§6§lUPDATE§8 > §fYou updated §c" + target.getName() + "§f's rank to §c" + r.getSQLName() + "§f.");
        Player targetOnline;
        if ((targetOnline = Bukkit.getPlayerExact(targetName)) != null) {
            targetOnline.sendMessage(
                    "§6§lUPDATE§8 > §fYour rank has been updated to §c" + r.getSQLName() + " §fby §c" + pl.getName()
                            + "§f.");
            targetOnline
                    .playerListName(Component.text(RankHandler.getRankFromSQLName(info.getSqlRank()).getGameName() + " §a"
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