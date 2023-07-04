package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.battle.test.BMTestPlugin;

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
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage("§e§lALERT§8 > §fLockdown mode has been §cdisabled §fby " + pl.getName());
            }
        } else {
            BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_LOCKDOWN, true);
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage("§e§lALERT§8 > §fLockdown mode has been §cenabled §fby " + pl.getName());
                all.sendMessage("§e§lALERT§8 > §fLockdown mode means that all EXP.MOD and lower cannot join.");
            }
        }
    }

    @Override
    public String getUsage() {
        return "/lockdown";
    }
}