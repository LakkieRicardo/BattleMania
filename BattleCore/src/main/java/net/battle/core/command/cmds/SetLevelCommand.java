package net.battle.core.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
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
            pl.sendMessage("§4§lERROR§8 > §cInvalid player");
            return;
        }

        int levels;
        try {
            levels = Integer.parseInt(args[1]);
        } catch (Exception e) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid number");
            return;
        }

        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetName);
        PlayerInfoSql.setLevel(targetOffline, levels);
        Player target;
        if ((target = Bukkit.getPlayerExact(targetName)) != null) {
            target.sendMessage(
                    "§6§lUPDATE§8 > §fYour level has been set to §c" + levels + " §fby §c" + pl.getName() + "§f.");
            ScoreboardHandler.updateScoreboard(target);
        }
        pl.sendMessage("§6§lUPDATE§8 > §fYou set §c" + targetName + "§f's level to §c" + levels + "§f.");
    }

    @Override
    public String getUsage() {
        return "/setlevel <player> <level>";
    }
}