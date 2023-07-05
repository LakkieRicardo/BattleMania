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

public class WarnCommand implements CommandBase {
    public String getLabel() {
        return "warn";
    }

    public String[] getAliases() {
        return new String[] { "warning", "w" };
    }

    @Override
    public String getDescription() {
        return "Add warning to a player";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.helperPermission(pl)) {
            CommandHandler.sendPerms(pl);

            return;
        }
        if (args.length < 2) {
            CommandHandler.sendUsage(pl, this);
            return;
        }

        if (!ProxyHandler.hasPlayerPlayedBefore(args[0])) {
            pl.sendMessage(Prefixes.ERROR + "Invalid player");
            return;
        }

        String reason = CommandHandler.getSpacedArgument(args, " ", 1);

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        String targetName = target.getName();
        String targetUUID = target.getUniqueId().toString();

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (RankHandler.helperPermission(all)) {
                all.sendMessage(Prefixes.PUNISH + "The user §c" + targetName + "§f has been warned by §c" + pl.getName());
            }
        }

        PlayerPunishInfo warnInfo = new PlayerPunishInfo(0, targetUUID, pl.getUniqueId().toString(), null, true, PunishmentType.WARN, reason);
        PunishmentSql.insertNewPlayerPunishment(warnInfo);

        Player targetOnline = Bukkit.getPlayerExact(targetName);
        if (targetOnline != null) {
            targetOnline.sendMessage(Prefixes.PUNISH + "You have been warned by §c" + pl.getName());
            targetOnline.sendMessage(Prefixes.PUNISH + "Reason: §c" + reason);
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (RankHandler.helperPermission(all)) {
                all.sendMessage(Prefixes.PUNISH + "Player §c" + targetName + "§f has been warned by §c" + pl.getName());
                all.sendMessage(Prefixes.PUNISH + "Reason: §c" + reason);
            }
        }
    }

    @Override
    public String getUsage() {
        return "/warn <player> <message...>";
    }
}