package net.battle.core.command.cmds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;

public class EvacuateCommand implements CommandBase {
    public String getLabel() {
        return "evac";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Removes all/non-staff players from this server";
    }

    public void onCommandExecute(Player pl, String[] args) {
        boolean keepStaff;
        if (!RankHandler.ownerPermission(pl)) {
            CommandHandler.sendPerms(pl);

            return;
        }
        if (args.length != 2) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        String server = args[0];

        try {
            keepStaff = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            pl.sendMessage("§4§lERROR§8 > §cMust be true or false");
            return;
        }
        broadcastMessage("§e§lALERT§8 > §fServer is evacuated.");
        List<UUID> playersToSend = new ArrayList<>();
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (keepStaff) {
                if (!RankHandler.expModPermission(all)) {
                    playersToSend.add(all.getUniqueId());
                }
                continue;
            }
            playersToSend.add(all.getUniqueId());
        }
        ProxyHandler.sendToServer(playersToSend, server);
    }

    private void broadcastMessage(String s) {
        for (Player pl : Bukkit.getOnlinePlayers())
            pl.sendMessage(s);
    }

    @Override
    public String getUsage() {
        return "/evac <server> <keep staff>";
    }
}