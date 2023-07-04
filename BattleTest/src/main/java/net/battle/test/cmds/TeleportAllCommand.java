package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
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
                    all.sendMessage("§9§lCOMMAND§8 > §fYou were teleported to §c" + pl.getName());
                }
            }
            pl.sendMessage("§9§lCOMMAND§8 > §fYou teleported everyone to you");

            return;
        }
        Player t = CommandHandler.getPlayer(args[0]);

        if (t == null) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid player");
            return;
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all != t) {
                all.teleport((Entity) t);
                all.sendMessage(
                        "§9§lCOMMAND§8 > §fYou were teleported to §c" + t.getName() + "§f by §c" + pl.getName());
            }
        }

        t.sendMessage("§9§lCOMMAND§8 > §fEveryone was teleported to you by §c" + pl.getName());
        pl.sendMessage("§9§lCOMMAND§8 > §fYou teleported everyone to §c" + t.getName());
    }

    @Override
    public String getUsage() {
        return "/tpall [target]";
    }
}