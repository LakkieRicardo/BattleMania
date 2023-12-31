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

public class SetLevelCommand implements CommandBase {
    public String getLabel() {
        return "setlevel";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Set someone's levels";
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

        int levels;
        try {
            levels = Integer.parseInt(args[1]);
        } catch (Exception e) {
            pl.sendMessage(Prefixes.ERROR + "Invalid number");
            return;
        }

        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetName);
        PlayerInfoSql.setLevel(targetOffline, levels);
        Player target;
        if ((target = Bukkit.getPlayerExact(targetName)) != null) {
            target.sendMessage(
                    Prefixes.UPDATE + "Your level has been set to §c" + levels + " §fby §c" + pl.getName() + "§f.");
            ScoreboardHandler.updateScoreboard(target);
        } else {
            ProxyHandler.playerStatUpdated(pl.getName(), targetOffline.getUniqueId(), "level", levels);
        }
        pl.sendMessage(Prefixes.UPDATE + "You set §c" + targetName + "§f's level to §c" + levels + "§f.");
    }

    @Override
    public String getUsage() {
        return "/setlevel <player> <level>";
    }
}