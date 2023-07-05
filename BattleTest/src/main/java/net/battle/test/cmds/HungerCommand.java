package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.test.BMTestPlugin;
import net.kyori.adventure.text.Component;

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
            Bukkit.broadcast(Component.text(Prefixes.COMMAND + "§c" + pl.getName() + "§f has disabled hunger"));
        } else {
            BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_HUNGER, true);
            Bukkit.broadcast(Component.text(Prefixes.COMMAND + "§c" + pl.getName() + "§f has enabled hunger"));
        }
    }

    @Override
    public String getUsage() {
        return "/hunger";
    }
}