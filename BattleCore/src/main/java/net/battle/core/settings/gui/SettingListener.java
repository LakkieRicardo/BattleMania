package net.battle.core.settings.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMTextConvert;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.settings.SettingHandler;
import net.battle.core.sql.impl.PlayerInfoSql;

public class SettingListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e == null) {
            return;
        }
        if (e.getInventory() == null) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }

        Player pl = (Player) e.getWhoClicked();
        InventoryView inv = e.getView();
        ItemStack clicked = e.getCurrentItem();
        Material m = clicked.getType();

        String invTitle = BMTextConvert.CTS.serialize(inv.title());

        if (invTitle.startsWith("Stats: ")) {
            String targetName = invTitle.substring("Stats: ".length());
            String targetUUID = Bukkit.getOfflinePlayer(targetName).getUniqueId().toString();
            if (m == Material.PLAYER_HEAD) {
                pl.closeInventory();
                pl.openInventory(SettingHandler.getPublicSettings(targetUUID, targetName));
                pl.sendMessage(Prefixes.COMMAND + "Opened public settings");
                return;
            }
            if (m == Material.REPEATER) {
                pl.closeInventory();
                pl.openInventory(SettingHandler.getGameSettings(targetUUID, targetName));
                pl.sendMessage(Prefixes.COMMAND + "Opened game settings");
                return;
            }
            if (m == Material.PAPER) {
                pl.sendMessage(Prefixes.COMMAND + "Opened hub settings");
                pl.openInventory(getHubSettingsInventory(pl, targetUUID, targetName));
                return;
            }
        } else if (invTitle.startsWith("Public Stats: ")) {
            String targetUsername = invTitle.substring("Public Stats: ".length());
            OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetUsername);
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("public.showswears"), false)) {
                if (PlayerInfoSql.getPlayerInfo(pl).ingameHours() < 5.0F && !RankHandler.ownerPermission(pl)) {
                    if (pl.getName().equals(targetUsername)) {
                        pl.sendMessage(Prefixes.ERROR + "You cannot your swear setting yet");
                    }
                    return;
                }
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "public.showswears", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("public.partyrequests"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "public.partyrequests", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("public.privatemsgs"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "public.privatemsgs", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("public.playerchat"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "public.playerchat", pl, targetOffline);
                return;
            }
        } else if (invTitle.startsWith("Game Stats: ")) {
            String targetUsername = invTitle.replaceFirst("Game Stats: ", "");
            OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetUsername);
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("game.leaveondeath"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "game.leaveondeath", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("game.particles"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "game.particles", pl, targetOffline);
                return;
            }
        } else if (invTitle.startsWith("Hub Stats: ")) {
            String targetUsername = invTitle.substring("Hub Stats: ".length());
            OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetUsername);
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("hub.showplayers"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "hub.showplayers", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("hub.playerlaunch"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "hub.playerlaunch", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("hub.forcefield"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "hub.forcefield", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("hub.antivelocity"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "hub.antivelocity", pl, targetOffline);
                return;
            }
            if (InventoryUtils.compareItemNames(clicked, SettingHandler.getSettingDisplayName("hub.fastsprint"), false)) {
                SettingHandler.handlePlayerUpdatedSettingGUI(e, "hub.fastsprint", pl, targetOffline);
                return;
            }
        }
    }

    public Inventory getHubSettingsInventory(Player pl) {
        if (RankHandler.operatorPermission(pl)) {
            return SettingHandler.getOperatorHubSettings(pl.getUniqueId().toString(), pl.getName());
        }
        if (RankHandler.moderatorPermission(pl)) {
            return SettingHandler.getModeratorHubSettings(pl.getUniqueId().toString(), pl.getName());
        }
        return SettingHandler.getHubSettings(pl.getUniqueId().toString(), pl.getName());
    }

    public Inventory getHubSettingsInventory(Player pl, String targetUUID, String targetName) {
        if (RankHandler.operatorPermission(pl)) {
            return SettingHandler.getOperatorHubSettings(targetUUID, targetName);
        }
        if (RankHandler.moderatorPermission(pl)) {
            return SettingHandler.getModeratorHubSettings(targetUUID, targetName);
        }
        return SettingHandler.getHubSettings(targetUUID, targetName);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        ItemStack item = pl.getInventory().getItemInMainHand();

        if (item.getType() == Material.REDSTONE_TORCH
                && InventoryUtils.getItemName(item).equalsIgnoreCase("§aSettings")) {
            if (pl.isSneaking()) {
                return;
            }
            pl.openInventory(SettingHandler.getSettingsInventory(pl.getName()));
            pl.sendMessage(Prefixes.COMMAND + "Opened settings");
            e.setCancelled(true);
        }
    }
}