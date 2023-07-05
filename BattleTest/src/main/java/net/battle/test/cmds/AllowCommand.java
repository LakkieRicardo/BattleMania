package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.test.BMTestPlugin;
import net.kyori.adventure.text.Component;

public class AllowCommand implements CommandBase {
    public String getLabel() {
        return "allow";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Toggle allow mode";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.ownerPermission(executor)) {
            CommandHandler.sendPerms(executor);
            return;
        }
        if (args.length == 0) {
            if (BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE)) {
                BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE, false);
                Bukkit.broadcast(Component.text(Prefixes.ALERT + "Player §c" + executor.getName() + "§f has disabled allow mode"));
            } else {
                BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE, true);
                Bukkit.broadcast(Component.text(Prefixes.ALERT + "Player §c" + executor.getName() + "§f has enabled allow mode"));
            }

            return;
        }
        if (args.length == 1) {
            OfflinePlayer t = CommandHandler.getOfflinePlayer(args[0]);
            if (t == null) {
                executor.sendMessage(Prefixes.COMMAND + "Invalid player");
                return;
            }
            if (BMTestPlugin.ACTIVE_PLUGIN.getAllowed().contains(t.getUniqueId().toString())) {
                BMTestPlugin.ACTIVE_PLUGIN.removeAllowed(t.getUniqueId());
                Bukkit.broadcast(Component.text(Prefixes.ALERT + "Player §c" + executor.getName() + "§f has removed §c" + t.getName() + "§f from allow"));
            } else {
                BMTestPlugin.ACTIVE_PLUGIN.addAllowed(t.getUniqueId());
                Bukkit.broadcast(Component.text(Prefixes.ALERT + "Player §c" + executor.getName() + "§f has added §c" + t.getName() + "§f from allow"));
            }
            return;
        }
        CommandHandler.sendUsage(executor, this);
    }

    @Override
    public String getUsage() {
        return "/allow <player>";
    }
}