package net.battle.core.punish.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.pod.PlayerPunishInfo;
import net.battle.core.sql.pod.PunishmentType;

public class ReportCommand implements CommandBase {
    public String getLabel() {
        return "report";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Report a player with reason";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (args.length < 2) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        if (ProxyHandler.hasPlayerPlayedBefore(args[0])) {
            pl.sendMessage(Prefixes.ERROR + "Invalid player");
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        String msg = CommandHandler.getSpacedArgument(args, " ", 1);

        PlayerPunishInfo reportInfo = new PlayerPunishInfo(0, target.getUniqueId().toString(),
                pl.getUniqueId().toString(), null, true, PunishmentType.REPORT, msg);
        PunishmentSql.insertNewPlayerPunishment(reportInfo);
        pl.sendMessage(Prefixes.COMMAND + "You have reported §c" + target.getName() + "§f, Reason: §c" + msg);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(target.getUniqueId())) {
                online.sendMessage(Prefixes.COMMAND + "You have been reported by §c" + pl.getName() + "§f for " + msg);
            } else if (RankHandler.helperPermission(online)) {
                online.sendMessage(
                        Prefixes.PUNISH + "Player §c" + pl.getName() + "§f has reported §c" + target.getName());
                online.sendMessage(Prefixes.PUNISH + "Reason: " + msg);
            }
        }
    }

    @Override
    public String getUsage() {
        return "/report <player> <msg...>";
    }
}