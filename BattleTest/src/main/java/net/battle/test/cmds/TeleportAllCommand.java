package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;

public class TeleportAllCommand implements CommandBase {
    public String getLabel() {
        return "tpall";
    }

    public String[] getAliases() {
        return new String[] { "teleportall" };
    }

    @Override
    public String getDescription() {
        return "Teleport everyone to you or a player";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.ownerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (args.length == 0) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (pl != all) {
                    all.teleport((Entity) pl);
                    all.sendMessage(Prefixes.COMMAND + "You were teleported to §c" + pl.getName());
                }
            }
            pl.sendMessage(Prefixes.COMMAND + "You teleported everyone to you");

            return;
        }
        Player t = CommandHandler.getPlayer(args[0]);

        if (t == null) {
            pl.sendMessage(Prefixes.ERROR + "Invalid player");
            return;
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all != t) {
                all.teleport((Entity) t);
                all.sendMessage(
                        Prefixes.COMMAND + "You were teleported to §c" + t.getName() + "§f by §c" + pl.getName());
            }
        }

        t.sendMessage(Prefixes.COMMAND + "Everyone was teleported to you by §c" + pl.getName());
        pl.sendMessage(Prefixes.COMMAND + "You teleported everyone to §c" + t.getName());
    }

    @Override
    public String getUsage() {
        return "/tpall [target]";
    }
}