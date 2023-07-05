package net.battle.core.punish.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;

public class OperatorCommand implements CommandBase {
    public String getLabel() {
        return "op";
    }

    public String[] getAliases() {
        return new String[] { "toggleop" };
    }

    @Override
    public String getDescription() {
        return "Give a player operator";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.ownerPermission(executor)) {
            executor.sendMessage(Prefixes.ERROR + "Not enough permission");
            return;
        }
        if (args.length != 2) {
            CommandHandler.sendUsage(executor, this);
            return;
        }
        if (args[0].equalsIgnoreCase("add")) {
            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
            if (t == null) {
                executor.sendMessage(Prefixes.ERROR + "Invalid player");
                return;
            }
            t.setOp(true);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (RankHandler.ownerPermission(online)) {
                    online.sendMessage(Prefixes.UPDATE + "Player §c" + t.getName() + "§f is now op");
                }
            }

            return;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
            if (t == null) {
                executor.sendMessage(Prefixes.ERROR + "Invalid player");
                return;
            }
            t.setOp(false);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (RankHandler.ownerPermission(online)) {
                    online.sendMessage(Prefixes.UPDATE + "Player §c" + t.getName() + "§f is no longer op");
                }
            }
            return;
        }

        if (args[0].equalsIgnoreCase("check")) {
            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
            if (t == null) {
                executor.sendMessage(Prefixes.ERROR + "Invalid player");
                return;
            }
            if (t.isOp()) {
                executor.sendMessage(Prefixes.COMMAND + "Player §c" + t.getName() + " is§f an op.");
            } else {
                executor.sendMessage(Prefixes.COMMAND + "Player §c" + t.getName() + "§f is §cnot§f an op.");
            }
            return;
        }
        CommandHandler.sendUsage(executor, this);
    }

    @Override
    public String getUsage() {
        return "/op <add,check,remove> <player>";
    }
}