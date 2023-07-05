package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.kyori.adventure.text.Component;

public class ClearDropsCommand implements CommandBase {
    public String getLabel() {
        return "cleardrops";
    }

    public String[] getAliases() {
        String[] aliases = { "cdrops" };
        return aliases;
    }

    @Override
    public String getDescription() {
        return "Clear all item drops";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.ownerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        int counter = 0;
        for (Entity e : pl.getWorld().getEntities()) {
            if (e instanceof Item) {
                Item item = (Item) e;
                item.remove();
                counter++;
            }
        }

        Bukkit.broadcast(Component.text(Prefixes.COMMAND + "§c" + pl.getName() + "§f cleared all items removing a total of §c" + counter + "§f items"));
    }

    @Override
    public String getUsage() {
        return "/cleardrops";
    }
}