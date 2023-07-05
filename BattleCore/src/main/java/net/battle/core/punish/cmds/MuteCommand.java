package net.battle.core.punish.cmds;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

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

public class MuteCommand implements CommandBase {
    public String getLabel() {
        return "mute";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Mute a player with reason";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.helperPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }

        if (args.length < 3) {
            CommandHandler.sendUsage(pl, this);
            return;
        }

        if (!ProxyHandler.hasPlayerPlayedBefore(args[0])) {
            pl.sendMessage(Prefixes.ERROR + "Invalid player.");
            return;
        }
        // TODO: To add Bungee support all of the Bukkit.getOfflinePlayer calls will
        // need to be replaced with a Bungee.getBMPlayer call
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        Player targetOnline = Bukkit.getPlayerExact(args[0]);

        String reason = CommandHandler.getSpacedArgument(args, " ", 2);

        if (args[1].equalsIgnoreCase("permanent")) {
            if (!RankHandler.developerPermission(pl)) {
                pl.sendMessage(Prefixes.ERROR + "Only Developer+ can mute permanently.");
                return;
            }

            if (targetOnline != null) {
                targetOnline
                        .sendMessage(Prefixes.PUNISH + "You've been muted by: §c" + pl.getName() + "§f §c§lPERMANENTLY");
                targetOnline.sendMessage(Prefixes.PUNISH + "Reason: " + reason);
            }

            for (Player serverPlayer : Bukkit.getOnlinePlayers()) {
                if (RankHandler.helperPermission(serverPlayer)) {
                    serverPlayer.sendMessage(Prefixes.PUNISH + "The user §c" + target.getName()
                            + "§f has been muted §c§lPERMANENTLY§f by §c" + pl.getName());
                }
            }

            PlayerPunishInfo muteInfo = new PlayerPunishInfo(0, target.getUniqueId().toString(),
                    pl.getUniqueId().toString(),
                    null, true, PunishmentType.MUTE,
                    reason);
            PunishmentSql.insertNewPlayerPunishment(muteInfo);
            return;
        }

        try {
            int dayCount = Integer.parseInt(args[1]);

            if (!RankHandler.developerPermission(pl)) {
                if (!RankHandler.moderatorPermission(pl) && dayCount > 1) {
                    pl.sendMessage(Prefixes.ERROR + "Helpers can only mute for up to 1 days.");
                    return;
                }
                if (!RankHandler.expModPermission(pl) && dayCount > 7) {
                    pl.sendMessage(Prefixes.ERROR + "Moderators can only mute for up to 7 days");
                    return;
                }
                if (dayCount > 31) {
                    pl.sendMessage(Prefixes.ERROR + "Experienced moderators can only mute for up to 1 month(31 days)");
                    return;
                }
            }

            if (targetOnline != null) {
                targetOnline.sendMessage(Prefixes.PUNISH + "You've been muted by: §c" + pl.getName() + "§f for §c§l"
                        + dayCount + " DAYS");
                targetOnline.sendMessage(Prefixes.PUNISH + "Reason: " + reason);
            }

            for (Player serverPlayer : Bukkit.getOnlinePlayers()) {
                if (RankHandler.helperPermission(serverPlayer)) {
                    serverPlayer.sendMessage(Prefixes.PUNISH + "The user §c" + target.getName()
                            + "§f has been muted for §c§l" + dayCount + " DAYS§f by §c" + pl.getName());
                }
            }

            PlayerPunishInfo muteInfo = new PlayerPunishInfo(0, target.getUniqueId().toString(),
                    pl.getUniqueId().toString(),
                    new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(dayCount)), true, PunishmentType.MUTE,
                    reason);
            PunishmentSql.insertNewPlayerPunishment(muteInfo);
        } catch (Exception e) {
            pl.sendMessage(Prefixes.ERROR + "Invalid amount of days.");
            return;
        }
    }

    @Override
    public String getUsage() {
        return "/mute <player> <days|permanent> <reason...>";
    }
}