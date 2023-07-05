package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.test.BMTestPlugin;
import net.kyori.adventure.text.Component;

public class LockdownCommand implements CommandBase {
    public String getLabel() {
        return "lockdown";
    }

    public String[] getAliases() {
        String[] aliases = { "ld", "lock" };
        return aliases;
    }

    @Override
    public String getDescription() {
        return "Toggle lockdown mode";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.operatorPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_LOCKDOWN)) {
            BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_LOCKDOWN, false);
            Bukkit.broadcast(Component.text(Prefixes.ALERT + "Lockdown mode has been §cdisabled §fby " + pl.getName()));
        } else {
            BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_LOCKDOWN, true);
            Bukkit.broadcast(Component.text(Prefixes.ALERT + "Lockdown mode has been §cenabled §fby " + pl.getName()));
            Bukkit.broadcast(Component.text(Prefixes.ALERT + "Lockdown mode means that all EXP.MOD and lower cannot join."));
        }
    }

    @Override
    public String getUsage() {
        return "/lockdown";
    }
}