package net.battle.core.command.cmds;

import java.util.List;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.battle.core.BMMacro;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;

public class HologramCommand implements CommandBase {
    public String getLabel() {
        return "hologram";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Spawn a hologram";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.operatorPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (args.length == 0) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        String text = CommandHandler.getSpacedArgument(args, " ");

        if (text.equalsIgnoreCase("kill")) {
            List<Entity> nearby = pl.getNearbyEntities(2.0D, 2.0D, 2.0D);
            for (Entity en : nearby) {
                if (en instanceof ArmorStand && en.isCustomNameVisible()) {
                    ArmorStand stand = (ArmorStand) en;
                    pl.sendMessage("§9§lCOMMAND§8 > §fKilled " + BMMacro.CTS.serialize(stand.customName()));
                    stand.setHealth(0.0D);
                }
            }
            return;
        }
    }

    @Override
    public String getUsage() {
        return "/hologram <kill/name>";
    }
    
}