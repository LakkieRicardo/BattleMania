package net.battle.core.command.cmds;

import java.util.List;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.battle.core.BMTextConvert;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.kyori.adventure.text.Component;

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
            int counter = 0;
            for (Entity en : nearby) {
                if (en instanceof ArmorStand stand && stand.isCustomNameVisible() && stand.isInvisible()) {
                    pl.sendMessage(Prefixes.COMMAND + "Killed " + BMTextConvert.CTS.serialize(stand.customName()));
                    stand.setHealth(0.0D);
                    stand.setKiller(pl);
                    counter++;
                }
            }
            pl.sendMessage(Prefixes.COMMAND + "Killed a total of §c" + counter + "§f entities.");
            return;
        }

        ArmorStand stand = (ArmorStand) pl.getWorld().spawnEntity(pl.getLocation().add(0, 1000, 0), EntityType.ARMOR_STAND, SpawnReason.COMMAND);
        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setAI(false);
        stand.customName(Component.text(text.replaceAll("&", "§")));
        stand.setCustomNameVisible(true);
        stand.setInvulnerable(true);
        stand.setCollidable(false);
        stand.setCanMove(false);
        stand.teleport(pl.getLocation());
    }

    @Override
    public String getUsage() {
        return "/hologram <kill/name>";
    }

}