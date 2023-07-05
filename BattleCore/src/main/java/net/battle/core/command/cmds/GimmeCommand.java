package net.battle.core.command.cmds;

import java.util.ArrayList;
import java.util.List;

import net.battle.core.BMTextConvert;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.RankHandler;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GimmeCommand implements CommandBase {
    public String getLabel() {
        return "gimme";
    }

    public String[] getAliases() {
        return new String[] { "i", "give" };
    }

    @Override
    public String getDescription() {
        return "Give a player an item with metadata";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.developerPermission(pl)) {
            CommandHandler.sendPerms(pl);

            return;
        }
        List<Player> targets = new ArrayList<>();
        targets.add(pl);

        if (args.length < 2) {
            CommandHandler.sendUsage(pl, this);
            return;
        }

        String ts = args[0];

        if (ts.contains(",")) {
            String[] players = ts.split(",");
            byte b;
            int i;
            String[] arrayOfString1;
            for (i = (arrayOfString1 = players).length, b = 0; b < i;) {
                String s = arrayOfString1[b];
                Player t = CommandHandler.getPlayer(s);
                if (t == null) {
                    pl.sendMessage("§4§lERROR§8 > §cPlayer " + s + " is invalid");
                    return;
                }
                targets.add(t);
                b++;
            }

        } else {
            if (ts.equalsIgnoreCase("all")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    targets.add(all);
                }
            }
            if (ts.equalsIgnoreCase("me")) {
                targets.add(pl);
            }
            if (targets.size() == 0) {
                Player t = CommandHandler.getPlayer(ts);
                if (t == null) {
                    pl.sendMessage("§4§lERROR§8 > §cPlayer " + ts + " is invalid");
                    return;
                }
                targets.add(t);
            }
            if (ts.equalsIgnoreCase("help")) {
                CommandHandler.sendUsage(pl, this);
                return;
            }
        }

        Material type = null;

        for (int i = 0; i < Material.values().length; i++) {
            Material m = Material.values()[i];
            if (m.name().equalsIgnoreCase(args[1]) || m.name().replaceAll("_", " ").equalsIgnoreCase(args[1])) {
                type = m;
            }
        }

        if (type == null) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid material");
            return;
        }

        ItemStack item = new ItemStack(type);

        if (args.length == 2) {
            for (Player all : targets) {
                all.getInventory().addItem(new ItemStack[] { item });
                all.sendMessage("§9§lCOMMAND§8 > §fYou §fgave yourself §c" + item.getAmount() + " "
                        + ((item.getItemMeta().displayName() != null) ? BMTextConvert.CTS.serialize(item.getItemMeta().displayName())
                                : type.name().toLowerCase().replaceAll("_", " ")));
            }

            return;
        }
        int amount = -1;
        List<FullEnchantment> enchants = new ArrayList<>();
        String finalName = "NO_NAME";
        List<Component> lore = new ArrayList<>();

        if (args.length > 2) {

            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception e) {
                pl.sendMessage("§4§lERROR§8 > §cInvalid amount");

                return;
            }
        }
        if (args.length > 3) {
            if (!args[3].equalsIgnoreCase("none")) {
                String arg = args[3];
                if (arg.contains(",")) {
                    String[] stringArray = arg.split(",");
                    for (int i = 0; i < stringArray.length; i++) {
                        String s = stringArray[i];
                        if (parseEnchant(s) == null) {
                            pl.sendMessage("§4§lERROR§8 > §cInvalid enchant: " + s);
                            pl.sendMessage("§9§lCOMMAND§8 > §fAll enchantments: §c" + GenerateAllEnchantsString());
                            return;
                        }
                        enchants.add(parseEnchant(s));
                    }

                } else {
                    if (parseEnchant(arg) == null) {
                        pl.sendMessage("§4§lERROR§8 > §cInvalid enchant: " + arg);
                        pl.sendMessage("§9§lCOMMAND§8 > §f§fAll enchantments: " + GenerateAllEnchantsString());
                        return;
                    }
                    enchants.add(parseEnchant(arg));
                }
            }
        }

        if (args.length > 4 && !args[4].equalsIgnoreCase("none")) {
            finalName = ChatColor.translateAlternateColorCodes('&', args[4].replaceAll("_", " "));
        }

        if (args.length > 5 && !args[5].equalsIgnoreCase("none")) {
            String rawLore = args[5];
            if (rawLore.contains(",")) {
                String[] lores = rawLore.split(",");
                for (int j = 0; j < lores.length; j++) {
                    lore.add(Component.text(lores[j].replaceAll("_", " ").replaceAll("&", "§")));
                }

            } else {
                lore.add(Component.text(rawLore.replaceAll("_", " ").replaceAll("&", "§")));
            }
        }

        if (amount != -1) {
            item.setAmount(amount);
        }

        for (FullEnchantment enchant : enchants) {
            item.addUnsafeEnchantment(enchant.enchantment, enchant.level);
        }

        if (!finalName.equalsIgnoreCase("NO_NAME")) {
            InventoryUtils.renameItem(item, finalName);
        }

        InventoryUtils.setItemLore(item, lore);

        for (Player all : targets) {
            all.getInventory().addItem(new ItemStack[] { item });
            all.sendMessage("§9§lCOMMAND§8 > §fYou §fgave yourself §c" + item.getAmount() + " "
                    + ((item.getItemMeta().displayName() != null) ? BMTextConvert.CTS.serialize(item.getItemMeta().displayName())
                            : type.name().toLowerCase().replaceAll("_", " ")));
        }
    }

    private FullEnchantment parseEnchant(String enchant) {
        int level;
        String[] components = enchant.split(":");

        Enchantment ench = getEnchantment(components[0]);
        if (ench == null) {
            return null;
        }

        try {
            level = Integer.parseInt(components[1]);
        } catch (Exception e) {
            return null;
        }

        FullEnchantment fullEnch = new FullEnchantment();
        fullEnch.enchantment = ench;
        fullEnch.level = level;

        return fullEnch;
    }

    private Enchantment getEnchantment(String enchant) {
        return Enchantment.getByKey(NamespacedKey.fromString(enchant));
    }

    public static String GenerateAllEnchantsString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Enchantment.values().length; i++) {
            if (i != 0) {
                result.append(", ");
            }

        }

        return result.toString();
    }

    private class FullEnchantment {
        public int level;
        public Enchantment enchantment;

        private FullEnchantment() {
        }
    }

    @Override
    public String getUsage() {
        return "/i <players> <material> [amount] [enchants] [name] [lore]";
    }
}