package net.battle.core.command.cmds;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.battle.core.listeners.PotionHandler;

public class EffectCommand implements CommandBase {
    public String getLabel() {
        return "effect";
    }

    public String[] getAliases() {
        return new String[] { "potion" };
    }

    @Override
    public String getDescription() {
        return "Adds a potion effect to a player/yourself";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.operatorPermission(pl)) {
            CommandHandler.sendPerms(pl);

            return;
        }
        if (args.length == 1) {
            if (RankHandler.ownerPermission(pl)) {
                if (CommandHandler.getPlayer(args[0]) == null) {
                    pl.sendMessage("§4§lERROR§8 > §cInvalid player");
                    return;
                }
                Player target = CommandHandler.getPlayer(args[0]);
                pl.openInventory(PotionHandler.getPotionTypeInventory(target.getName()));
                pl.sendMessage("§9§lCOMMAND§8 > §fYou opened effect inventory for §c" + target.getName());
                return;
            }
            CommandHandler.sendPerms(pl);

            return;
        }

        pl.openInventory(PotionHandler.getPotionTypeInventory(pl.getName()));
        pl.sendMessage("§9§lCOMMAND§8 > §fYou opened effect inventory for yourself");
    }

    @Override
    public String getUsage() {
        return "/effect [player]";
    }
}