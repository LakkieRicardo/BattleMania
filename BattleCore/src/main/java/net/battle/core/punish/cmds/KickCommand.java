package net.battle.core.punish.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;

public class KickCommand implements CommandBase {
    public String getLabel() {
        return "kick";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Kick a player with reason";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.operatorPermission(executor)) {
            executor.sendMessage(Prefixes.ERROR + "Not enough permission");
            return;
        }

        if (args.length < 2) {
            CommandHandler.sendUsage(executor, this);
            return;
        }
        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            executor.sendMessage(Prefixes.ERROR + "Invalid player");
            return;
        }
        targetName = target.getName();

        String reason = CommandHandler.getSpacedArgument(args, " ", 1);
        if (reason.length() < 4) {
            executor.sendMessage(Prefixes.ERROR + "Please enter a more detailed reason");

            return;
        }
        ProxyHandler.kickPlayer(executor, targetName, formatKickMessage(targetName, reason, executor.getName()));
    }

    public static String formatKickMessage(String player, String reason, String kicker) {
        return "§a§l" + BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("servertitle")
                + " §c§lPunishments\n\n§7You have" + " been §c§lKICKED!\n§7Reason: §c" + reason + "\n§7By: §c"
                + kicker;
    }

    @Override
    public String getUsage() {
        return "/kick <player> <reason...>";
    }
}