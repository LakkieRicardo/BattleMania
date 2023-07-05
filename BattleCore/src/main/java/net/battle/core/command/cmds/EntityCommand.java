package net.battle.core.command.cmds;

import java.util.regex.Pattern;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.kyori.adventure.text.Component;

public class EntityCommand implements CommandBase {
    public String getLabel() {
        return "entity";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Spawns one entity";
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

        if (args[0].equalsIgnoreCase("list")) {
            String[] entityNameArray = new String[EntityType.values().length];
            for (int i = 0; i < entityNameArray.length; i++) {
                entityNameArray[i] = EntityType.values()[i].name().toLowerCase();
            }
            String entityList = String.join(", ", entityNameArray);

            if (args.length > 1) {
                String searchTerm = CommandHandler.getSpacedArgument(args, " ", 1);
                entityList = entityList.replaceAll("(?i)" + Pattern.quote(searchTerm), "§c$0§7");
            }

            pl.sendMessage("§9§lCOMMAND§8 > §7All Entities: §7" + entityList);
            return;
        }
        EntityType type = getEntityType(args[0]);
        if (type == null || type == EntityType.PLAYER) {
            pl.sendMessage(Prefixes.ERROR + "Invalid entity type");
            CommandHandler.sendUsage(pl, this);
            return;
        }

        Entity entity = pl.getWorld().spawnEntity(pl.getLocation(), type, SpawnReason.COMMAND);

        if (args.length > 1) {
            String name = CommandHandler.getSpacedArgument(args, " ", 1).replaceAll("&", "§");
            entity.customName(Component.text(name));
        }
    }

    public static EntityType getEntityType(String name) {
        for (int i = 0; i < EntityType.values().length; i++) {
            EntityType type = EntityType.values()[i];
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }

    @Override
    public String getUsage() {
        return "/entity <type|list> [name...|search term...]";
    }
}