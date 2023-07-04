package net.battle.core.punish.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.battle.core.handlers.TempHandler;
import net.battle.core.proxy.ProxyHandler;

public class FreezeCommand implements CommandBase {
    public String getLabel() {
        return "freeze";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Toggle a player's freeze";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.moderatorPermission(executor)) {
            executor.sendMessage("§4§lERROR§8 > §cNot enough permission");
            return;
        }
        if (args.length != 1) {
            CommandHandler.sendUsage(executor, this);
            return;
        }
        if (!ProxyHandler.hasPlayerPlayedBefore(args[0])) {
            executor.sendMessage("§4§lERROR§8 > §cInvalid player");
            return;
        }
        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(args[0]);
        if (targetOffline.isOnline()) {
            Player targetOnline = Bukkit.getPlayerExact(args[0]);
            TempHandler.handleTogglePlayerFrozen(executor, targetOnline);
            return;
        }
        TempHandler.handleToggleOfflinePlayerFrozen(executor, targetOffline);
    }

    @Override
    public String getUsage() {
        return "/freeze <player>";
    }
}