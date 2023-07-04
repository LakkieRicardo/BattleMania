package net.battle.core.command.cmds;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;

public class NullCommand implements CommandBase {
    public String getLabel() {
        return "null";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Does ??";
    }

    public void onCommandExecute(Player pl, String[] args) {
        pl.sendMessage("§9§lCOMMAND§8 > §fI wonder what you thought this command did....");
    }

    @Override
    public String getUsage() {
        return "/null";
    }
}