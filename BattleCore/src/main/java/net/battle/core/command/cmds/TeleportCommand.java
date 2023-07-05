package net.battle.core.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;

public class TeleportCommand implements CommandBase {
    public String getLabel() {
        return "tp";
    }

    public String[] getAliases() {
        return new String[] { "teleport" };
    }

    @Override
    public String getDescription() {
        return "Teleport to a certain player/all players or to another player/you";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.expModPermission(executor)) {
            executor.sendMessage(Prefixes.ERROR + "Not enough permission");
            return;
        }
        if (args.length == 2) {
            if (!RankHandler.moderatorPermission(executor)) {
                executor.sendMessage(Prefixes.ERROR + "Not enough permission");
                return;
            }
            if (args.length < 1) {
                CommandHandler.sendUsage(executor, this);
                return;
            }
            if (args.length == 1) {
                Player t = CommandHandler.getPlayer(args[0]);
                if (t == null) {
                    executor.sendMessage(Prefixes.ERROR + "Invalid player");
                    return;
                }
                executor.teleport((Entity) t);
                executor.sendMessage(Prefixes.COMMAND + "Teleported to §c" + t.getName());
                return;
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("here")) {
                    if (args[0].equalsIgnoreCase("all")) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player != executor) {
                                player.teleport((Entity) executor);
                                player.sendMessage(Prefixes.COMMAND + "Player §c" + executor.getName()
                                        + "§f has teleported you to them");
                            }
                        }
                        executor.sendMessage(Prefixes.COMMAND + "You teleported §ceveryone§f to you");
                        return;
                    }
                    Player t = CommandHandler.getPlayer(args[0]);
                    if (t == null) {
                        executor.sendMessage(Prefixes.ERROR + "Invalid player");
                        return;
                    }
                    t.teleport((Entity) executor);
                    t.sendMessage(Prefixes.COMMAND + "Player §c" + executor.getName() + "§f has teleported you to them");
                    executor.sendMessage(Prefixes.COMMAND + "Teleported §c" + t.getName() + "§f to you");
                    return;
                }
                Player from = CommandHandler.getPlayer(args[0]);
                Player to = CommandHandler.getPlayer(args[1]);
                if (from == null || to == null) {
                    executor.sendMessage(Prefixes.ERROR + "Invalid player");
                    return;
                }
                from.teleport((Entity) to);
                executor.sendMessage(
                        Prefixes.COMMAND + "You teleported §c" + from.getName() + "§f to §c" + to.getName());
                if (from != executor) {
                    from.sendMessage(Prefixes.COMMAND + "You have been teleported to §c" + to.getName() + "§f by §c"
                            + executor.getName());
                }
                if (to != executor) {
                    to.sendMessage(Prefixes.COMMAND + "Player §c" + from.getName()
                            + "§f has been teleported to you by §c" + executor.getName());
                }

                return;
            }
            CommandHandler.sendUsage(executor, this);
            return;
        }
        if (args.length < 1) {
            CommandHandler.sendUsage(executor, this);
            return;
        }
        if (args.length == 1) {
            Player t = CommandHandler.getPlayer(args[0]);
            if (t == null) {
                executor.sendMessage(Prefixes.ERROR + "Invalid player");
                return;
            }
            executor.teleport((Entity) t);
            executor.sendMessage(Prefixes.COMMAND + "Teleported to §c" + t.getName());
            return;
        }
        CommandHandler.sendUsage(executor, this);
    }

    @Override
    public String getUsage() {
        return "/tp <player|all> [player|here]";
    }
}