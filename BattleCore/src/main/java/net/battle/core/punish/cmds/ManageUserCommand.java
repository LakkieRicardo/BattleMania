package net.battle.core.punish.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.punish.gui.ManageUserHandler;
import net.battle.core.punish.gui.ManageUserListener;

public class ManageUserCommand implements CommandBase {
    public String getLabel() {
        return "mu";
    }

    public String[] getAliases() {
        return new String[] { "manageuser", "muser" };
    }

    @Override
    public String getDescription() {
        return "Open manage user inventory";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.helperPermission(executor)) {
            executor.sendMessage("§4§lERROR§8 > §cNot enough permission");

            return;
        }
        if (args.length < 2) {
            CommandHandler.sendUsage(executor, this);
            return;
        }
        if (!ProxyHandler.hasPlayerPlayedBefore(args[0])) {
            executor.sendMessage("§4§lERROR§8 > §cInvalid player");
            return;
        }

        String reason = CommandHandler.getSpacedArgument(args, " ", 1);

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        executor.sendMessage("§9§lCOMMAND§8 > §fYou are now managing §c" + target);
        BMLogger.info("Opening manager user for " + target);
        executor.openInventory(ManageUserHandler.getMain(executor, target));
        ManageUserListener.REASONS.put(executor.getName(), reason);
    }

    @Override
    public String getUsage() {
        return "/mu <player> <reason...>";
    }
}