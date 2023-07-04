package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;

public class ShutdownCommand implements CommandBase {
    public static boolean shutdown = false;

    public String getLabel() {
        return "shutdown";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Toggle shutdown mode, only allow owners in the server";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.ownerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (shutdown) {
            shutdown = false;
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage("§9§lCOMMAND§8 > §fShutdown mode was set to §c" + shutdown + "§f by §c" + pl.getName());
            }
            return;
        }
        shutdown = true;
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage("§9§lCOMMAND§8 > §fShutdown mode was set to §c" + shutdown + "§f by §c" + pl.getName());
            if (!RankHandler.ownerPermission(all)) {
                all.sendMessage("§9§lCOMMAND§8 > §fYou are being sent to §cLobby§f because of shutdown mode");
                ProxyHandler.sendAllToServer("Lobby");
            }
        }
    }

    @Override
    public String getUsage() {
        return "/shutdown";
    }
}