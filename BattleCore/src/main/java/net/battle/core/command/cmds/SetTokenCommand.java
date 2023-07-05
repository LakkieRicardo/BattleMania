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

public class SetTokenCommand implements CommandBase {
    public String getLabel() {
        return "settoken";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Set someone's number of tokens";
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

        int tokens;
        try {
            tokens = Integer.parseInt(args[1]);
        } catch (Exception e) {
            pl.sendMessage(Prefixes.ERROR + "Invalid number");
            return;
        }

        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetName);
        PlayerInfoSql.setToken(targetOffline, tokens);
        Player target;
        if ((target = Bukkit.getPlayerExact(targetName)) != null) {
            target.sendMessage(
                    Prefixes.UPDATE + "Your token has been set to §c" + tokens + " §fby §c" + pl.getName() + "§f.");
            ScoreboardHandler.updateScoreboard(target);
        } else {
            ProxyHandler.playerStatUpdated(pl.getName(), targetOffline.getUniqueId(), "token", tokens);
        }
        pl.sendMessage(Prefixes.UPDATE + "You set §c" + targetName + "§f's token to §c" + tokens + "§f.");
    }

    @Override
    public String getUsage() {
        return "/settoken <player> <token>";
    }
}