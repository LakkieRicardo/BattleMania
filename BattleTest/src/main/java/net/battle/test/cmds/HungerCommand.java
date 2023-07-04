package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.battle.test.BMTestPlugin;

public class HungerCommand implements CommandBase {
    public String getLabel() {
        return "hunger";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Toggle hunger";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.operatorPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_HUNGER)) {
            BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_HUNGER, false);
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage("§9§lCOMMAND§8 > §f§c" + pl.getName() + "§f has disabled hunger");
            }
        } else {
            BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_HUNGER, true);
            for (Player all : Bukkit.getOnlinePlayers())
                all.sendMessage("§9§lCOMMAND§8 > §f§c" + pl.getName() + "§f has enabled hunger");
        }
    }

    @Override
    public String getUsage() {
        return "/hunger";
    }
}