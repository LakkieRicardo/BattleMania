package net.battle.core.punish.gui;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.RankHandler;
import net.battle.core.punish.PunishManager;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.pod.PlayerInfo;
import net.battle.core.sql.pod.PlayerPunishInfo;
import net.battle.core.sql.pod.PunishmentType;
import net.kyori.adventure.text.Component;

public class ManageUserHandler {

    public static final String PUNISH_ID_FIELD_PREFIX = "§cPunish ID: §7";

    public static Inventory getMain(Player user, OfflinePlayer target) {
        if (RankHandler.expModPermission(user)) {
            return getExpModMain(target.getName(), target.getUniqueId().toString());
        }
        if (RankHandler.moderatorPermission(user)) {
            return getModMain(target.getName(), target.getUniqueId().toString());
        }
        if (RankHandler.helperPermission(user)) {
            return getHelperMain(target.getName());
        }
        return null;
    }

    private static Inventory getHelperMain(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Manage " + name));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack punish = new ItemStack(Material.WOODEN_AXE);
        InventoryUtils.renameItem(punish, "§aPunish");
        InventoryUtils.setItemLore(punish, "§7Left Click to assign a punishment");
        InventoryUtils.addItemLore(punish, "§7Middle click to assign a warning");
        InventoryUtils.setItem(inv, 1, 2, punish);
        ItemStack check = new ItemStack(Material.BOOK);
        InventoryUtils.renameItem(check, "§aCheck Punishments");
        InventoryUtils.setItemLore(check, "§7Click to view punishments");
        InventoryUtils.setItem(inv, 4, 2, check);
        ItemStack staff = new ItemStack(Material.COMPASS);
        InventoryUtils.renameItem(staff, "§aStaff Tools");
        InventoryUtils.setItemLore(staff, "§7Click to view all Staff Tools");
        InventoryUtils.setItem(inv, 7, 2, staff);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    private static Inventory getModMain(String name, String uuid) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Manage " + name));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack punish = new ItemStack(Material.WOODEN_AXE);
        InventoryUtils.renameItem(punish, "§aPunish");
        InventoryUtils.setItemLore(punish, "§7Click to assign a punishment");
        InventoryUtils.setItem(inv, 1, 2, punish);
        ItemStack check = new ItemStack(Material.BOOK);
        InventoryUtils.renameItem(check, "§aCheck Punishments");
        InventoryUtils.setItemLore(check, "§7Click to view punishments");
        InventoryUtils.setItem(inv, 4, 2, check);
        ItemStack staff = new ItemStack(Material.COMPASS);
        InventoryUtils.renameItem(staff, "§aStaff Tools");
        InventoryUtils.setItemLore(staff, "§7Click to view all Staff Tools");
        InventoryUtils.setItem(inv, 7, 2, staff);
        ItemStack stats = new ItemStack(Material.CLOCK);
        InventoryUtils.renameItem(stats, "§aStats");
        PlayerInfo info = PlayerInfoSql.getPlayerInfo(uuid);
        InventoryUtils.setItemLore(stats, "§7§m----------------------------------------");
        InventoryUtils.setItemLore(stats, "§7Ingots: §c" + info.getIngot());
        InventoryUtils.addItemLore(stats, "§7Tokens: §c" + info.getToken());
        InventoryUtils.addItemLore(stats, "§7Online Time: §c" + info.getTime());
        InventoryUtils.addItemLore(stats, "§7UUID: §c" + uuid);
        InventoryUtils.addItemLore(stats, "§7Rank: §c" + RankHandler.getRankFromSQLName(info.getSqlRank()).getGameName());
        InventoryUtils.addItemLore(stats, "§7§m----------------------------------------");
        InventoryUtils.setItem(inv, 1, 4, stats);
        ItemStack settings = new ItemStack(Material.COMPARATOR);
        InventoryUtils.renameItem(settings, "§aSettings");
        InventoryUtils.addItemLore(settings, "§7Click to view or edit");
        InventoryUtils.addItemLore(settings, "§7the player's settings");
        InventoryUtils.setItem(inv, 4, 4, settings);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    private static Inventory getExpModMain(String name, String uuid) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Manage " + name));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack punish = new ItemStack(Material.WOODEN_AXE);
        InventoryUtils.renameItem(punish, "§aPunish");
        InventoryUtils.setItemLore(punish, "§7Click to assign a punishment");
        InventoryUtils.setItem(inv, 1, 2, punish);
        ItemStack check = new ItemStack(Material.BOOK);
        InventoryUtils.renameItem(check, "§aCheck Punishments");
        InventoryUtils.setItemLore(check, "§7Click to view punishments");
        InventoryUtils.setItem(inv, 4, 2, check);
        ItemStack staff = new ItemStack(Material.COMPASS);
        InventoryUtils.renameItem(staff, "§aStaff Tools");
        InventoryUtils.setItemLore(staff, "§7Click to view all Staff Tools");
        InventoryUtils.setItem(inv, 7, 2, staff);
        ItemStack stats = new ItemStack(Material.CLOCK);
        InventoryUtils.renameItem(stats, "§aStats");
        PlayerInfo info = PlayerInfoSql.getPlayerInfo(uuid);
        InventoryUtils.setItemLore(stats, "§7§m----------------------------------------");
        InventoryUtils.addItemLore(stats, "§7Ingots: §c" + info.getIngot());
        InventoryUtils.addItemLore(stats, "§7Tokens: §c" + info.getToken());
        InventoryUtils.addItemLore(stats, "§7Online Time: §c" + info.getTime() + " hours");
        InventoryUtils.addItemLore(stats, "§7UUID: §c" + uuid);
        InventoryUtils.addItemLore(stats, "§7Rank: §c" + RankHandler.getRankFromSQLName(info.getSqlRank()).getGameName());
        InventoryUtils.addItemLore(stats, "§7§m----------------------------------------");
        InventoryUtils.setItem(inv, 1, 4, stats);
        ItemStack settings = new ItemStack(Material.COMPARATOR);
        InventoryUtils.renameItem(settings, "§aSettings");
        InventoryUtils.setItemLore(settings, "§7Click to view or edit");
        InventoryUtils.addItemLore(settings, "§7tthe player's settings");
        InventoryUtils.setItem(inv, 4, 4, settings);
        ItemStack server = new ItemStack(Material.PISTON);
        InventoryUtils.renameItem(server, "§aMove to a Server");
        InventoryUtils.setItemLore(server, "§7Click to move player");
        InventoryUtils.addItemLore(server, "§7to a new server");
        InventoryUtils.setItem(inv, 7, 4, server);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getStaffTools(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Staff Tools"));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack vanish = new ItemStack(Material.GLASS);
        InventoryUtils.renameItem(vanish, "§aJoin Player's Server in Vanish");
        InventoryUtils.setItemLore(vanish, "§7Clicking this will put you in vanish and will teleport");
        InventoryUtils.addItemLore(vanish, "§7you to the player's server secretly");
        InventoryUtils.setItem(inv, 1, 2, vanish);
        ItemStack freeze = new ItemStack(Material.ICE);
        InventoryUtils.renameItem(freeze, "§aFreeze Player");
        InventoryUtils.setItemLore(freeze, "§7Click this to freeze the player");
        InventoryUtils.setItem(inv, 4, 2, freeze);
        ItemStack locate = new ItemStack(Material.COMPASS);
        InventoryUtils.renameItem(locate, "§aLocate Player");
        InventoryUtils.setItemLore(locate, "§7Click this to find the server the player is");
        InventoryUtils.addItemLore(locate, "§7currently on");
        InventoryUtils.setItem(inv, 7, 2, locate);
        ItemStack back = new ItemStack(Material.RED_BED);
        InventoryUtils.renameItem(back, "§8Back");
        InventoryUtils.setItem(inv, 4, 5, back);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getMoveInventory1(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Move Player"));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack bw = new ItemStack(Material.COBBLESTONE);
        InventoryUtils.renameItem(bw, "§aBunkerWars");
        InventoryUtils.setItemLore(bw, "§7Click to move to a BunkerWars server");
        InventoryUtils.setItem(inv, 1, 2, bw);
        ItemStack td = new ItemStack(Material.FIRE_CHARGE);
        InventoryUtils.renameItem(td, "§aTankDefender");
        InventoryUtils.setItemLore(td, "§7Click to move to a TankDefender server");
        InventoryUtils.setItem(inv, 4, 2, td);
        ItemStack lobby = new ItemStack(Material.CRAFTING_TABLE);
        InventoryUtils.renameItem(lobby, "§aLobby");
        InventoryUtils.setItemLore(lobby, "§7Click to move to a Lobby server");
        InventoryUtils.setItem(inv, 7, 2, lobby);
        ItemStack back = new ItemStack(Material.RED_BED);
        InventoryUtils.renameItem(back, "§8Back");
        InventoryUtils.setItem(inv, 4, 5, back);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getPunish(String name, Player user) {
        if (RankHandler.expModPermission(user)) {
            return getExpModPunish(name);
        }
        if (RankHandler.moderatorPermission(user)) {
            return getModeratorPunish(name);
        }
        if (RankHandler.helperPermission(user)) {
            return getHelperPunish(name);
        }
        return null;
    }

    public static Inventory getHelperPunish(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Punish"));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack chatPunish = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(chatPunish, "§aChat Punishments");
        InventoryUtils.setItemLore(chatPunish, "§7These punishments are only if the user");
        InventoryUtils.addItemLore(chatPunish, "§7is breaking any of our chat related rules");
        InventoryUtils.setItem(inv, 1, 1, chatPunish);
        ItemStack exploitPunish = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(exploitPunish, "§aExploit Punishments");
        InventoryUtils.setItemLore(exploitPunish, "§7These punishments are only if the user is");
        InventoryUtils.addItemLore(exploitPunish, "§7misusing a bug on purpose");
        InventoryUtils.setItem(inv, 4, 1, exploitPunish);
        ItemStack clientPunish = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(clientPunish, "§aClient Punishments");
        InventoryUtils.setItemLore(clientPunish, "§7These punishments are only if");
        InventoryUtils.addItemLore(clientPunish, "§7the user is using unapproved mods or hacks");
        InventoryUtils.setItem(inv, 7, 1, clientPunish);
        ItemStack back = new ItemStack(Material.RED_BED);
        InventoryUtils.renameItem(back, "§8Back");
        InventoryUtils.setItem(inv, 4, 5, back);
        ItemStack warn = new ItemStack(Material.PAPER);
        InventoryUtils.renameItem(warn, "§aWarning");
        InventoryUtils.setItemLore(warn, "§7If the player already has a warning for the same");
        InventoryUtils.addItemLore(warn, "§7reason then it will result in a larger punishment");
        InventoryUtils.setItem(inv, 0, 5, warn);
        ItemStack chat1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(chat1, "§aSeverity 1");
        InventoryUtils.setItemLore(chat1, "§7This is for when a user is using to many caps");
        InventoryUtils.addItemLore(chat1, "§7the max limit is 5 words EX: \"HI HI BATTLEMANIA HI HI\"");
        InventoryUtils.addItemLore(chat1, "§7-");
        InventoryUtils.addItemLore(chat1, "§6Note: §7If the user has not already been warned then warn first");
        InventoryUtils.addItemLore(chat1, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(chat1, "§7on the same day then use punishment");
        InventoryUtils.addItemLore(chat1, "§cMute Duration: §71 Day");
        InventoryUtils.setItem(inv, 1, 2, chat1);
        ItemStack exploit1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(exploit1, "§aSeverity 1");
        InventoryUtils.setItemLore(exploit1, "§7This is for when a user is abusing bugs");
        InventoryUtils.addItemLore(exploit1, "§7-");
        InventoryUtils.addItemLore(exploit1, "§6Note: §7If the user has not already been warned then warn first");
        InventoryUtils.addItemLore(exploit1, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(exploit1, "§7on the same day then use punishment");
        InventoryUtils.addItemLore(exploit1, "§cBan Duration: §75 Days");
        InventoryUtils.setItem(inv, 4, 2, exploit1);
        ItemStack client1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(client1, "§aSeverity 1");
        InventoryUtils.setItemLore(client1, "§7This is for when a user is saying they are going to hack");
        InventoryUtils.addItemLore(client1, "§7EX: \"I am going to hack with kill aura and kill everyone\"");
        InventoryUtils.addItemLore(client1, "§cBan Duration: §71 Day");
        InventoryUtils.setItem(inv, 7, 2, client1);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getModeratorPunish(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Punish"));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack chatPunish = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(chatPunish, "§aChat Punishments");
        InventoryUtils.setItemLore(chatPunish, "§7These punishments are only if the user");
        InventoryUtils.addItemLore(chatPunish, "§7is breaking any of our chat related rules");
        InventoryUtils.setItem(inv, 1, 1, chatPunish);
        ItemStack exploitPunish = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(exploitPunish, "§aExploit Punishments");
        InventoryUtils.setItemLore(exploitPunish, "§7These punishments are only if the user is");
        InventoryUtils.addItemLore(exploitPunish, "§7misusing a bug on purpose");
        InventoryUtils.setItem(inv, 4, 1, exploitPunish);
        ItemStack clientPunish = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(clientPunish, "§aClient Punishments");
        InventoryUtils.setItemLore(clientPunish, "§7These punishments are only if");
        InventoryUtils.addItemLore(clientPunish, "§7the user is using unapproved mods or hacks");
        InventoryUtils.setItem(inv, 7, 1, clientPunish);
        ItemStack back = new ItemStack(Material.RED_BED);
        InventoryUtils.renameItem(back, "§8Back");
        InventoryUtils.setItem(inv, 4, 5, back);
        ItemStack warn = new ItemStack(Material.PAPER);
        InventoryUtils.renameItem(warn, "§aWarning");
        InventoryUtils.setItemLore(warn, "§7If the player already has a warning for the same");
        InventoryUtils.addItemLore(warn, "§7reason then it will result in a larger punishment");
        InventoryUtils.setItem(inv, 0, 5, warn);
        ItemStack chat1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(chat1, "§aSeverity 1");
        InventoryUtils.setItemLore(chat1, "§7This is for when a user is using to many caps");
        InventoryUtils.addItemLore(chat1, "§7the max limit is 5 words EX: \"HI HI BATTLEMANIA HI HI\"");
        InventoryUtils.addItemLore(chat1, "§7-");
        InventoryUtils.addItemLore(chat1, "§6Note: §7If the user has not already been warned, then warn first");
        InventoryUtils.addItemLore(chat1, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(chat1, "§7on the same day then use this punishment");
        InventoryUtils.addItemLore(chat1, "§cMute Duration: §71 Day");
        InventoryUtils.setItem(inv, 1, 2, chat1);
        ItemStack exploit1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(exploit1, "§aSeverity 1");
        InventoryUtils.setItemLore(exploit1, "§7This is for when a user is abusing bugs");
        InventoryUtils.addItemLore(exploit1, "§7-");
        InventoryUtils.addItemLore(exploit1, "§6Note: §7If the user has not already been warned, then warn first");
        InventoryUtils.addItemLore(exploit1, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(exploit1, "§7on the same day then use this punishment");
        InventoryUtils.addItemLore(exploit1, "§cBan Duration: §75 Days");
        InventoryUtils.setItem(inv, 4, 2, exploit1);
        ItemStack client1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(client1, "§aSeverity 1");
        InventoryUtils.setItemLore(client1, "§7This is for when a user is saying they are going to hack");
        InventoryUtils.addItemLore(client1, "§7EX: \"I am going to hack with kill aura and kill everyone\"");
        InventoryUtils.addItemLore(client1, "§cBan Duration: §71 Day");
        InventoryUtils.setItem(inv, 7, 2, client1);
        ItemStack chat2 = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(chat2, "§aSeverity 2");
        InventoryUtils.setItemLore(chat2, "§7This is for when a user is being rude in chat or bypassing filter");
        InventoryUtils.addItemLore(chat2, "§7EX: \"You suck at this you dummy,\" \"Get a life,\" and \"FUACK YOU\"");
        InventoryUtils.addItemLore(chat2, "§7-");
        InventoryUtils.addItemLore(chat2, "§6Note: §7If the user has not already been warned, then warn first");
        InventoryUtils.addItemLore(chat2, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(chat2, "§7on the same day then use this punishment");
        InventoryUtils.addItemLore(chat2, "§cMute Duration: §77 Days");
        InventoryUtils.setItem(inv, 1, 3, chat2);
        ItemStack exploit2 = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(exploit2, "§aSeverity 2");
        InventoryUtils.setItemLore(exploit2, "§7This is for when a user is using non-combat hacks");
        InventoryUtils.addItemLore(exploit2, "§7EX: Fly hacks, speed hacks, durp hacks, etc.");
        InventoryUtils.addItemLore(exploit2, "§cBan Duration: §77 Days");
        InventoryUtils.setItem(inv, 7, 3, exploit2);
        ItemStack permBan = new ItemStack(Material.REDSTONE_BLOCK);
        InventoryUtils.renameItem(permBan, "§aPermanent Ban");
        InventoryUtils.setItemLore(permBan, "§7This is if the user has already been banned");
        InventoryUtils.addItemLore(permBan, "§7for 30 days");
        InventoryUtils.addItemLore(permBan, "§cWARNING: §7Please supply a detailed report for this punishment");
        InventoryUtils.addItemLore(permBan, "§cBan Duration: §7FOREVER");
        InventoryUtils.setItem(inv, 0, 0, permBan);
        ItemStack mutePerm = new ItemStack(Material.BARRIER);
        InventoryUtils.renameItem(mutePerm, "§aPermanent Mute");
        InventoryUtils.setItemLore(mutePerm, "§7This is if the user has already been muted");
        InventoryUtils.addItemLore(mutePerm, "§7For 1 Month");
        InventoryUtils.addItemLore(mutePerm, "§cWARNING: §7Please supply a detailed report for this punishment");
        InventoryUtils.addItemLore(mutePerm, "§cMute Duration: §7FOREVER");
        InventoryUtils.setItem(inv, 8, 0, mutePerm);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getExpModPunish(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Punish"));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack chatPunish = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(chatPunish, "§aChat Punishments");
        InventoryUtils.setItemLore(chatPunish, "§7These punishments are only if the user");
        InventoryUtils.addItemLore(chatPunish, "§7is breaking any of our chat related rules");
        InventoryUtils.setItem(inv, 1, 1, chatPunish);
        ItemStack exploitPunish = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(exploitPunish, "§aExploit Punishments");
        InventoryUtils.setItemLore(exploitPunish, "§7These punishments are only if the user is");
        InventoryUtils.addItemLore(exploitPunish, "§7misusing a bug on purpose");
        InventoryUtils.setItem(inv, 4, 1, exploitPunish);
        ItemStack clientPunish = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        InventoryUtils.renameItem(clientPunish, "§aClient Punishments");
        InventoryUtils.setItemLore(clientPunish, "§7These punishments are only if");
        InventoryUtils.addItemLore(clientPunish, "§7the user is using unapproved mods or hacks");
        InventoryUtils.setItem(inv, 7, 1, clientPunish);
        ItemStack back = new ItemStack(Material.RED_BED);
        InventoryUtils.renameItem(back, "§8Back");
        InventoryUtils.setItem(inv, 4, 5, back);
        ItemStack warn = new ItemStack(Material.PAPER);
        InventoryUtils.renameItem(warn, "§aWarning");
        InventoryUtils.setItemLore(warn, "§7If the player already has a warning for the same");
        InventoryUtils.addItemLore(warn, "§7reason then it will result in a larger punishment");
        InventoryUtils.setItem(inv, 0, 5, warn);
        ItemStack chat1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(chat1, "§aSeverity 1");
        InventoryUtils.setItemLore(chat1, "§7This is for when a user is using to many caps");
        InventoryUtils.addItemLore(chat1, "§7the max limit is 5 words EX: \"HI HI BATTLEMANIA HI HI\"");
        InventoryUtils.addItemLore(chat1, "§7-");
        InventoryUtils.addItemLore(chat1, "§6Note: §7If the user has not already been warned, then warn first");
        InventoryUtils.addItemLore(chat1, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(chat1, "§7on the same day then use this punishment");
        InventoryUtils.addItemLore(chat1, "§cMute Duration: §71 Day");
        InventoryUtils.setItem(inv, 1, 2, chat1);
        ItemStack exploit1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(exploit1, "§aSeverity 1");
        InventoryUtils.setItemLore(exploit1, "§7This is for when a user is abusing bugs");
        InventoryUtils.addItemLore(exploit1, "§7-");
        InventoryUtils.addItemLore(exploit1, "§6Note: §7If the user has not already been warned, then warn first");
        InventoryUtils.addItemLore(exploit1, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(exploit1, "§7on the same day then use this punishment");
        InventoryUtils.addItemLore(exploit1, "§cBan Duration: §75 Days");
        InventoryUtils.setItem(inv, 4, 2, exploit1);
        ItemStack client1 = new ItemStack(Material.YELLOW_CONCRETE);
        InventoryUtils.renameItem(client1, "§aSeverity 1");
        InventoryUtils.setItemLore(client1, "§7This is for when a user is saying they are going to hack");
        InventoryUtils.addItemLore(client1, "§7EX: \"I am going to hack with kill aura and kill everyone\"");
        InventoryUtils.addItemLore(client1, "§cBan Duration: §71 Day");
        InventoryUtils.setItem(inv, 7, 2, client1);
        ItemStack chat2 = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(chat2, "§aSeverity 2");
        InventoryUtils.setItemLore(chat2, "§7This is for when a user is being rude in chat or bypassing filter");
        InventoryUtils.addItemLore(chat2, "§7EX: \"You suck at this you dummy,\" \"Get a life,\" and \"FUACK YOU\"");
        InventoryUtils.addItemLore(chat2, "§7-");
        InventoryUtils.addItemLore(chat2, "§6Note: §7If the user has not already been warned, then warn first");
        InventoryUtils.addItemLore(chat2, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(chat2, "§7on the same day then use this punishment");
        InventoryUtils.addItemLore(chat2, "§cMute Duration: §77 Days");
        InventoryUtils.setItem(inv, 1, 3, chat2);
        ItemStack client2 = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(client2, "§aSeverity 2");
        InventoryUtils.setItemLore(client2, "§7This is for when a user is using non-combat hacks");
        InventoryUtils.addItemLore(client2, "§7EX: Fly hacks, speed hacks, durp hacks, etc.");
        InventoryUtils.addItemLore(client2, "§cBan Duration: §77 Days");
        InventoryUtils.setItem(inv, 7, 3, client2);
        ItemStack permBan = new ItemStack(Material.REDSTONE_BLOCK);
        InventoryUtils.renameItem(permBan, "§aPermanent Ban");
        InventoryUtils.setItemLore(permBan, "§7This is if the user has already been banned");
        InventoryUtils.addItemLore(permBan, "§7for 30 days");
        InventoryUtils.addItemLore(permBan, "§cWARNING: §7Please supply a detailed report for this punishment");
        InventoryUtils.addItemLore(permBan, "§cBan Duration: §7FOREVER");
        InventoryUtils.setItem(inv, 0, 0, permBan);
        ItemStack mutePerm = new ItemStack(Material.BARRIER);
        InventoryUtils.renameItem(mutePerm, "§aPermanent Mute");
        InventoryUtils.setItemLore(mutePerm, "§7This is if the user has already been muted");
        InventoryUtils.addItemLore(mutePerm, "§7For 1 Month");
        InventoryUtils.addItemLore(mutePerm, "§cWARNING: §7Please supply a detailed report for this punishment");
        InventoryUtils.addItemLore(mutePerm, "§cMute Duration: §7FOREVER");
        InventoryUtils.setItem(inv, 8, 0, mutePerm);
        ItemStack chat3 = new ItemStack(Material.RED_CONCRETE);
        InventoryUtils.renameItem(chat3, "§aSeverity 3");
        InventoryUtils.setItemLore(chat3, "§7This is for when a user is spamming advertisments or just plain spamming");
        InventoryUtils.addItemLore(chat3, "§7EX: \"JOIN: DUMB.SERVER.URL\" or \"HI\" 4 or more times");
        InventoryUtils.addItemLore(chat3, "§7-");
        InventoryUtils.addItemLore(chat3, "§6Note: §7If the user is not advertising and has not already been warned then warn first");
        InventoryUtils.addItemLore(chat3, "§7If the user has already been warned for the same reason");
        InventoryUtils.addItemLore(chat3, "§7on the same day then use this punishment");
        InventoryUtils.addItemLore(chat3, "§cMute Duration: §71 Month");
        InventoryUtils.setItem(inv, 1, 4, chat3);
        ItemStack exploit2 = new ItemStack(Material.GREEN_CONCRETE);
        InventoryUtils.renameItem(exploit2, "§aSeverity 2");
        InventoryUtils.setItemLore(exploit2, "§7This is for when a player expoits glitches or hacks and abuses leaked files");
        InventoryUtils.addItemLore(exploit2, "§7-");
        InventoryUtils.addItemLore(exploit2, "§6Note: §7If the player has not been punished for this before, punish them severity 1");
        InventoryUtils.addItemLore(exploit2, "§7If the player has hacked SQL information, do not give them severity 1");
        InventoryUtils.addItemLore(exploit2, "§cBan Duration: §72 Months");
        InventoryUtils.setItem(inv, 4, 3, exploit2);
        ItemStack client3 = new ItemStack(Material.RED_CONCRETE);
        InventoryUtils.renameItem(client3, "§aSeverity 3");
        InventoryUtils.setItemLore(client3, "§7This is for when a user is using combat hacks");
        InventoryUtils.addItemLore(client3, "§7EX: Kill aura, regen, reach, etc.");
        InventoryUtils.addItemLore(client3, "§cBan Duration: §71 Month");
        InventoryUtils.setItem(inv, 7, 4, client3);
        InventoryUtils.fillBlanks(inv);
        return inv;
    }

    public static Inventory getPunishList(String name, String uuid) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Punishments"));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        meta.setOwningPlayer(player);
        skull.setItemMeta((ItemMeta) meta);
        InventoryUtils.renameItem(skull, "§a" + name);
        InventoryUtils.setItem(inv, 4, 0, skull);
        ItemStack back = new ItemStack(Material.RED_BED);
        InventoryUtils.renameItem(back, "§8Back");
        InventoryUtils.setItem(inv, 4, 5, back);
        List<PlayerPunishInfo> warns = PunishmentSql.getPlayerPunishmentHistory(uuid, PunishmentType.WARN);
        List<PlayerPunishInfo> punishes = PunishmentSql.getPlayerPunishmentHistory(uuid);
        int counter = 18;
        for (PlayerPunishInfo punish : punishes) {
            if (counter > 43) {
                break;
            }

            ItemStack punishItem = null;
            if (punish.getType() == PunishmentType.REPORT || punish.getType() == PunishmentType.WARN) {
                continue;
            }
            punishItem = switch (punish.getType()) {
            case MUTE -> new ItemStack(Material.BARRIER);
            case BAN -> new ItemStack(Material.REDSTONE_BLOCK);
            default -> new ItemStack(Material.EGG); // Should not be reached
            };

            if (punish.getExpiration() == null) {
                InventoryUtils.renameItem(punishItem, "§aPermanent " + punish.getType().name());
            } else {
                InventoryUtils.renameItem(punishItem, "§aTemporary " + punish.getType().name());
            }

            InventoryUtils.setItemLore(punishItem, "§cType: §7" + punish.getType().name());
            if (punish.getExpiration() == null) {
                InventoryUtils.addItemLore(punishItem, "§c" + punish.getType().name() + "Duration: §7FOREVER");
            } else if (!punish.isActive()) {
                InventoryUtils.addItemLore(punishItem, "§c" + punish.getType().name() + "Duration: §7EXPIRED");
            } else {
                InventoryUtils.addItemLore(punishItem, "§c" + punish.getType().name() + "Expiration: §7"
                        + PunishManager.PUNISHMENT_DATE_FORMAT.format(punish.getExpiration()) + " at "
                        + PunishManager.PUNISHMENT_TIME_FORMAT.format(punish.getExpiration()));
            }

            if (punish.isActive()) {
                punishItem.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            }
            InventoryUtils.addItemLore(punishItem, "§7-");
            InventoryUtils.addItemLore(punishItem, "§cPunisher: §7" + Bukkit.getOfflinePlayer(UUID.fromString(punish.getPunisherUUID())).getName());
            InventoryUtils.addItemLore(punishItem, "§cPunish Reason: §7" + punish.getReason());
            InventoryUtils.addItemLore(punishItem, "§cActivity: §7" + (punish.isActive() ? "Active" : "Inactive"));
            InventoryUtils.addItemLore(punishItem, PUNISH_ID_FIELD_PREFIX + punish.getId());
            if (punish.isActive()) {
                InventoryUtils.addItemLore(punishItem, "§7-");
                InventoryUtils.addItemLore(punishItem, "§cClick me to de-activate punishment!");
            }
            inv.setItem(counter, punishItem);

            counter++;
        }
        for (PlayerPunishInfo warn : warns) {
            String[] components;
            if (counter > 43) {
                break;
            }
            ItemStack warnItem = new ItemStack(Material.PAPER);
            InventoryUtils.renameItem(warnItem, "§aWarning");
            InventoryUtils.setItemLore(warnItem, "§7If the player already has a warning for the same");
            InventoryUtils.addItemLore(warnItem, "§7reason then it will result in a larger punishment");
            InventoryUtils.addItemLore(warnItem, "§7-");
            InventoryUtils.addItemLore(warnItem, "§cPunisher: §7" + Bukkit.getOfflinePlayer(UUID.fromString(warn.getPunisherUUID())).getName());
            String reason = warn.getReason();

            if (reason.length() > 50) {
                components = reason.split("§", 50);
            } else {
                components = new String[] { reason };
            }
            InventoryUtils.addItemLore(warnItem, "§cPunish Reason: §7" + components[0]);
            if (components.length > 1) {
                for (int i = 1; i < components.length; i++) {
                    InventoryUtils.addItemLore(warnItem, "§7" + components[i]);
                }
            }
            inv.setItem(counter, warnItem);
            counter++;
        }
        InventoryUtils.fillBlanks(inv);
        return inv;
    }
}