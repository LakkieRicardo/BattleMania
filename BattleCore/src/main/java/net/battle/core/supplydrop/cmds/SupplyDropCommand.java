package net.battle.core.supplydrop.cmds;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.supplydrop.listeners.SupplyDropHandler;

public class SupplyDropCommand implements CommandBase {
    public String getLabel() {
        return "supplydrop";
    }

    public String[] getAliases() {
        return new String[] { "sd", "sdrop" };
    }

    @Override
    public String getDescription() {
        return "Open supply drops inventory";
    }

    public void onCommandExecute(Player pl, String[] args) {
        pl.openInventory(SupplyDropHandler.inventory);
    }

    @Override
    public String getUsage() {
        return "/supplydrop";
    }
}