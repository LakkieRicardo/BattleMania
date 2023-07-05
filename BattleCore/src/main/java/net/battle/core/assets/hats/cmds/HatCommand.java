package net.battle.core.assets.hats.cmds;

import org.bukkit.entity.Player;

import net.battle.core.assets.hats.Hat;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.StringUtility;

public class HatCommand implements CommandBase {
    public String getLabel() {
        return "hat";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Equip or unequip your hat";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (args.length != 1) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            String result = StringUtility.assemble(Hat.names, ", ");
            pl.sendMessage(Prefixes.COMMAND + "Hat names: " + result);
            return;
        }
        if (args[0].equalsIgnoreCase("disable")) {
            Hat.dequip(pl);
            return;
        }
        for (Hat hat : Hat.hats) {
            if (hat.getName().equalsIgnoreCase(args[0])) {
                hat.equip(pl);
                return;
            }
        }
        pl.sendMessage(Prefixes.ERROR + "Found no hat with the name §c" + args[0]);
    }

    @Override
    public String getUsage() {
        return "/hat <list,hat,disable>";
    }
}