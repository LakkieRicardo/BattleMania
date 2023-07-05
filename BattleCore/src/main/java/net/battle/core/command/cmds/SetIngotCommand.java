package net.battle.core.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.handlers.ScoreboardHandler;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.sql.impl.PlayerInfoSql;

public class SetIngotCommand implements CommandBase {
    public String getLabel() {
        return "setingot";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Set someone's number of ingots";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.developerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }

        if (args.length != 2) {
            CommandHandler.sendUsage(pl, this);
            return;
        }

        String targetName = args[0];
        if (!ProxyHandler.hasPlayerPlayedBefore(targetName)) {
            pl.sendMessage(Prefixes.ERROR + "Invalid player");
            return;
        }

        int ingots;
        try {
            ingots = Integer.parseInt(args[1]);
        } catch (Exception e) {
            pl.sendMessage(Prefixes.ERROR + "Invalid number");
            return;
        }

        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetName);
        PlayerInfoSql.setIngot(targetOffline, ingots);
        Player target;
        if ((target = Bukkit.getPlayerExact(targetName)) != null) {
            target.sendMessage(Prefixes.UPDATE + "Your ingot has been set to §c" + ingots + " §fby §c" + pl.getName() + "§f.");
            ScoreboardHandler.updateScoreboard(target);
        } else {
            ProxyHandler.playerStatUpdated(pl.getName(), targetOffline.getUniqueId(), "ingot", ingots);
        }
        pl.sendMessage(Prefixes.UPDATE + "You set §c" + targetName + "§f's ingot to §c" + ingots + "§f.");
    }

    @Override
    public String getUsage() {
        return "/setingot <player> <ingot>";
    }

}