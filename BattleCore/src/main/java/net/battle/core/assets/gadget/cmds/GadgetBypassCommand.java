package net.battle.core.assets.gadget.cmds;

import org.bukkit.entity.Player;

import net.battle.core.assets.gadget.Gadget;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;

public class GadgetBypassCommand implements CommandBase {
    public String getLabel() {
        return "gadgetbypass";
    }

    public String[] getAliases() {
        return new String[] { "gb", "gbypass" };
    }

    @Override
    public String getDescription() {
        return "Toggle your gagdet cooldown bypass";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.ownerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (Gadget.BYPASS.contains(pl.getName())) {
            Gadget.BYPASS.remove(pl.getName());
            pl.sendMessage(Prefixes.COMMAND + "You §cdisabled §fcooldown bypass");
            return;
        }
        Gadget.BYPASS.add(pl.getName());
        pl.sendMessage(Prefixes.COMMAND + "You §cenabled §fcooldown bypass");
    }

    @Override
    public String getUsage() {
        return "/gadgetbypass";
    }
}