package net.battle.core.assets.hats.cmds;

import org.bukkit.entity.Player;

import net.battle.core.assets.hats.gui.HatInventoryBuilder;
import net.battle.core.command.CommandBase;

public class HatInventoryCommand implements CommandBase {
    public String getLabel() {
        return "hatinv";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Open hat inventory";
    }

    public void onCommandExecute(Player pl, String[] args) {
        pl.openInventory(HatInventoryBuilder.getInventory());
        pl.sendMessage("§9§lCOMMAND§8 > §fOpened§c hat§f inventory");
    }

    @Override
    public String getUsage() {
        return "/hatinv";
    }
}