package net.battle.core.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.kyori.adventure.text.Component;

public class HeadCommand implements CommandBase {
    public String getLabel() {
        return "head";
    }

    public String[] getAliases() {
        return new String[] { "skull" };
    }

    @Override
    public String getDescription() {
        return "Give yourself a player head";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.developerPermission(executor)) {
            executor.sendMessage("§4§lERROR§8 > §cNot enough permission");
            return;
        }

        if (args.length == 0) {
            CommandHandler.sendUsage(executor, this);
            return;
        }
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (args.length == 1) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(args[0]));
            item.setItemMeta((ItemMeta) meta);
        }
        if (args.length > 1) {
            meta.displayName(Component.text(CommandHandler.getSpacedArgument(args, " ", 1).replaceAll("&", "§")));
            item.setItemMeta((ItemMeta) meta);
        }
        executor.getInventory().addItem(new ItemStack[] { item });
        executor.sendMessage("§9§lCOMMAND§8 > §fGave yourself §c" + meta.getOwningPlayer().getName() + "§f's skull (§c"
                + meta.getOwningPlayer().getUniqueId() + "§f)");
    }

    @Override
    public String getUsage() {
        return "/givehead <owner> [name...]";
    }
}