package net.battle.core.command.cmds;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.handlers.Prefixes;

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
        pl.sendMessage(Prefixes.COMMAND + "I wonder what you thought this command did....");
    }

    @Override
    public String getUsage() {
        return "/null";
    }
}