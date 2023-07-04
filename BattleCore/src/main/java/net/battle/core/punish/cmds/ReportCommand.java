package net.battle.core.punish.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
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
            pl.sendMessage("§4§lERROR§8 > §cInvalid player");
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        String msg = CommandHandler.getSpacedArgument(args, " ", 1);

        PlayerPunishInfo reportInfo = new PlayerPunishInfo(0, target.getUniqueId().toString(),
                pl.getUniqueId().toString(), null, true, PunishmentType.REPORT, msg);
        PunishmentSql.insertNewPlayerPunishment(reportInfo);
        pl.sendMessage("§9§lCOMMAND§8 > §fYou have reported §c" + target.getName() + "§f, Reason: §c" + msg);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(target.getUniqueId())) {
                online.sendMessage("§9§lCOMMAND§8 > §fYou have been reported by §c" + pl.getName() + "§f for " + msg);
            } else if (RankHandler.helperPermission(online)) {
                online.sendMessage(
                        "§a§lPUNISH§8 > §fPlayer §c" + pl.getName() + "§f has reported §c" + target.getName());
                online.sendMessage("§a§lPUNISH§8 > §fReason: " + msg);
            }
        }
    }

    @Override
    public String getUsage() {
        return "/report <player> <msg...>";
    }
}