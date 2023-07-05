package net.battle.core.punish.cmds;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Rank;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.punish.PunishManager;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.pod.PlayerPunishInfo;
import net.battle.core.sql.pod.PunishmentType;

public class BanCommand implements CommandBase {
    public String getLabel() {
        return "ban";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Ban a player with a reason";
    }

    public void onCommandExecute(Player pl, String[] args) {
        int dayCount;
        boolean permanent;
        if (!RankHandler.helperPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }

        if (args.length < 4) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        String reason = CommandHandler.getSpacedArgument(args, " ", 3);

        try {
            permanent = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid permanent ban value.");
            return;
        }
        try {
            dayCount = Integer.parseInt(args[0]);
        } catch (Exception e) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid amount of days.");
            return;
        }
        if (dayCount < 1) {
            pl.sendMessage("§4§lERROR§8 > §cThe ban must last for at least 1 day.");
            return;
        }

        if (!ProxyHandler.hasPlayerPlayedBefore(args[2])) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid player");
            return;
        }
        String targetUUID = Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString();

        if (!RankHandler.developerPermission(pl)) {
            if (RankHandler.getPlayerRank(pl) == Rank.HELPER && dayCount > 5) {
                pl.sendMessage("§4§lERROR§8 > §cHelpers can only ban for up to 5 days.");
                return;
            }
            if (RankHandler.getPlayerRank(pl) == Rank.MODERATOR && dayCount > 7) {
                pl.sendMessage("§4§lERROR§8 > §cModerators can only ban for up to 7 days");
                return;
            }
            if (RankHandler.getPlayerRank(pl) == Rank.EXPMOD && dayCount > 62) {
                pl.sendMessage("§4§lERROR§8 > §cExperienced moderators can only ban for up to 2 months(62 days)");

                return;
            }
        }
        Rank tr = RankHandler.getRankFromSQLName(targetUUID);
        Rank pr = RankHandler.getPlayerRank(pl);

        if (pr != Rank.OWNER && (tr == Rank.OPERATOR || tr == Rank.OWNER)) {
            pl.sendMessage("§4§lERROR§8 > §cYou cannot ban owners or operators");

            return;
        }

        PlayerPunishInfo banInfo = new PlayerPunishInfo(
            0, // Won't be inserted when using the insertNewPlayerPunishment function
            targetUUID,
            pl.getUniqueId().toString(),
            permanent ? null : new Date(TimeUnit.DAYS.convert(dayCount, TimeUnit.MILLISECONDS) + System.currentTimeMillis()),
            true,
            PunishmentType.BAN,
            reason
        );
        PunishmentSql.insertNewPlayerPunishment(banInfo);

        ProxyHandler.kickPlayer(pl, targetUUID, String.join("\n", PunishManager.getBanMessage(banInfo)));

        for (Player foo : Bukkit.getOnlinePlayers()) {
            if (RankHandler.helperPermission(foo))
                foo.sendMessage("§a§lPUNISH§8 > §fThe user §c" + args[2] + "§f has been banned for "
                        + (permanent ? "§c§lPERMANENT" : ("§c§l" + dayCount + " DAYS")) + "§f by §c" + pl.getName());
        }
    }

    @Override
    public String getUsage() {
        return "/ban <days> <permanent(true/false)> <player> <reason...>";
    }
}