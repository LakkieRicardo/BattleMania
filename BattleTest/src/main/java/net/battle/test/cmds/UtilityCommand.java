package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.listeners.JoinLeaveListener;

public class UtilityCommand implements CommandBase {
    public String getLabel() {
        return "utils";
    }

    public String[] getAliases() {
        return new String[] { "util", "utility" };
    }

    @Override
    public String getDescription() {
        return "Miscellaneous commands";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.developerPermission(executor)) {
            CommandHandler.sendPerms(executor);
            return;
        }
        if (args.length != 2) {
            executor.sendMessage(Prefixes.ERROR + "Usage for inventories: /utils inv <anvil,craft,furnace,ender,open,enchant>");
            executor.sendMessage(Prefixes.ERROR + "Usage for gamemode: /utils gm <type>");
            executor.sendMessage(Prefixes.ERROR + "Usage for custom invs: /utils getinv <clear,build,hub>");
            return;
        }
        if (args[0].equalsIgnoreCase("inv")) {
            String type = args[1].toLowerCase();
            if (type.equalsIgnoreCase("anvil"))
                executor.openInventory(Bukkit.createInventory((InventoryHolder) executor, InventoryType.ANVIL));
            if (type.equalsIgnoreCase("craft"))
                executor.openInventory(Bukkit.createInventory((InventoryHolder) executor, InventoryType.CRAFTING));
            if (type.equalsIgnoreCase("furnace"))
                executor.openInventory(Bukkit.createInventory((InventoryHolder) executor, InventoryType.FURNACE));
            if (type.equalsIgnoreCase("ender"))
                executor.openInventory(Bukkit.createInventory((InventoryHolder) executor, InventoryType.ENDER_CHEST));
            if (type.equalsIgnoreCase("open"))
                executor.openInventory((Inventory) executor.getInventory());
            if (type.equalsIgnoreCase("enchant"))
                executor.openInventory(Bukkit.createInventory((InventoryHolder) executor, InventoryType.ENCHANTING));
            if (executor.getOpenInventory() == null) {
                executor.sendMessage(Prefixes.ERROR + "Invalid inventory");
            }
            return;
        }
        if (args[0].equalsIgnoreCase("gm")) {
            String gamemode = args[1];

            if (gamemode.equalsIgnoreCase("s") || gamemode.equalsIgnoreCase("0")) {
                GameMode target = GameMode.SURVIVAL;
                executor.setGameMode(target);
                executor.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");

                return;
            }
            if (gamemode.equalsIgnoreCase("c") || gamemode.equalsIgnoreCase("1")) {
                GameMode target = GameMode.CREATIVE;
                executor.setGameMode(target);
                executor.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");

                return;
            }
            if (gamemode.equalsIgnoreCase("a") || gamemode.equalsIgnoreCase("2")) {
                GameMode target = GameMode.ADVENTURE;
                executor.setGameMode(target);
                executor.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");

                return;
            }
            if (gamemode.equalsIgnoreCase("sp") || gamemode.equalsIgnoreCase("3")) {
                GameMode target = GameMode.SPECTATOR;
                executor.setGameMode(target);
                executor.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");
                return;
            }
            executor.sendMessage(Prefixes.ERROR + "Invalid gamemode");
            return;
        }
        if (args[0].equalsIgnoreCase("getinv")) {
            String type = args[1];
            if (type.equalsIgnoreCase("clear")) {
                executor.getInventory().clear();
                executor.sendMessage(Prefixes.COMMAND + "Got inventory clear");
                return;
            }
            if (type.equalsIgnoreCase("build")) {
                executor.getInventory().clear();
                executor.getInventory().setItem(0, InventoryUtils.renameItem(new ItemStack(Material.STONE, 1), "§aBuild Tools"));
                executor.getInventory().setItem(1, InventoryUtils.renameItem(new ItemStack(Material.BLAZE_ROD), "§aPosition 1"));
                executor.getInventory().setItem(2, InventoryUtils.renameItem(new ItemStack(Material.BLAZE_ROD), "§aPosition 2"));
                executor.getInventory().setItem(3, InventoryUtils.renameItem(new ItemStack(Material.COBBLESTONE, 1), "§aWorld Editing Tools"));
                executor.sendMessage(Prefixes.COMMAND + "Got inventory build");
                return;
            }
            if (type.equalsIgnoreCase("hub")) {
                JoinLeaveListener.resetInventory(executor);
                executor.sendMessage(Prefixes.COMMAND + "Got inventory hub");
                return;
            }
            executor.sendMessage(Prefixes.ERROR + "Invalid inventory");

            return;
        }
        executor.sendMessage(Prefixes.ERROR + "Invalid type");
    }

    @Override
    public String getUsage() {
        return "/utils <inv,gm,getinv> [type]";
    }
}