package net.battle.core.command.cmds;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.kyori.adventure.text.Component;

public class SpawnEntityCommand implements CommandBase {
    public String getLabel() {
        return "sentity";
    }

    public String[] getAliases() {
        return new String[] { "spawnentity", "se" };
    }

    @Override
    public String getDescription() {
        return "Spawn a variable amount of entities with a certain name where you are looking";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.developerPermission(executor)) {
            executor.sendMessage("§4§lERROR§8 > §cNot enough permission");
            return;
        }

        int amount;
        if (args.length < 2) {
            CommandHandler.sendUsage(executor, this);
            return;
        }
        EntityType type = EntityCommand.getEntityType(args[0]);
        if (type == null) {
            executor.sendMessage("§4§lERROR§8 > §cInvalid entity type");
            executor.sendMessage("§e§lALERT§8 > §fUse /entity list to display entities");

            return;
        }
        try {
            amount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            executor.sendMessage("§4§lERROR§8 > §cInvalid amount");
            return;
        }
        if (!RankHandler.ownerPermission(executor) && amount > 10) {
            executor.sendMessage("§4§lERROR§8 > §cYou cannot spawn more than 10 entities at a time");
            return;
        }
        if (amount > 200) {
            executor.sendMessage("§4§lERROR§8 > §cYou cannot spawn more than 200 entities at a time");
            return;
        }
        String name = null;
        if (args.length > 2) {
            name = "";
            for (int j = 2; j < args.length; j++) {
                if (j == 2) {
                    name = name + args[j];
                } else {
                    name = name + " " + args[j];
                }
            }
            name = name.replaceAll("&", "§");
        }

        if (type == EntityType.PLAYER) {
            executor.sendMessage("§4§lERROR§8 > §cYou cannot spawn players");
            return;
        }
        Location target = executor.getTargetBlock(null, 50).getLocation();
        target.add(0.0D, 1.0D, 0.0D);
        for (int i = 0; i < amount; i++) {
            Entity spawn = target.getWorld().spawnEntity(target, type);
            if (name != null) {
                spawn.customName(Component.text(name));
                spawn.setCustomNameVisible(true);
            }
        }

        executor.sendMessage("§9§lCOMMAND§8 > §fYou spawned §c" + amount + "§f of §c" + type.name());
    }

    @Override
    public String getUsage() {
        return "/sentity <type> <amount> [name...]";
    }
}