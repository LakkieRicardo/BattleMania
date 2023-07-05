package net.battle.core.punish.gui;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMTextConvert;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.pod.PlayerPunishInfo;
import net.battle.core.sql.pod.PunishmentType;
import net.kyori.adventure.text.Component;

public class ReportInventory implements Listener {

    public void openInventory(Player pl) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Report"));
        int count = Bukkit.getOnlinePlayers().size() - 1;
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all == pl) {
                continue;
            }
            ItemStack skull = InventoryUtils.getPlayerSkull(all.getName());
            skull = InventoryUtils.renameItem(skull, "§cClick to Report!");
            skull = InventoryUtils.setItemLore(skull,
                    Arrays.asList(new Component[] { Component.text("§7" + all.getName()) }));
            inv.addItem(new ItemStack[] { skull });
        }
        int remaining = 54 - count;
        for (int i = 0; i < remaining; i++) {
            inv.setItem(i + count, InventoryUtils.BLANK_ITEM);
        }
        pl.openInventory(inv);
    }

    public void openAnvilInventory(Player pl, String whoToReport) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, Component.text("Report " + whoToReport));
        inv.setItem(0, InventoryUtils.renameItem(new ItemStack(Material.PAPER), "§rReason for report"));
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent e) {
        Player pl = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        InventoryView inv = e.getView();
        String invTitle = BMTextConvert.CTS.serialize(inv.title());

        if (invTitle.equalsIgnoreCase("Report")) {
            if (InventoryUtils.isItemSimilarTo(item, InventoryUtils.BLANK_ITEM)) {
                return;
            }

            for (Player clicked : Bukkit.getOnlinePlayers()) {
                if (InventoryUtils.getItemLore(item).contains("§7" + clicked.getName())) {

                    pl.closeInventory();
                    openAnvilInventory(pl, clicked.getName());
                }
            }

            return;
        }

        if (invTitle.startsWith("Report ") && e.getSlotType() == InventoryType.SlotType.RESULT) {
            String reportedName = invTitle.substring("Report ".length());
            String reportedUUID = Bukkit.getOfflinePlayer(reportedName).getUniqueId().toString();
            String reporterName = pl.getName();
            String message = InventoryUtils.getItemName(item);
            pl.sendMessage(Prefixes.PUNISH + "You have reported §c" + reportedName + " §ffor: " + message);
            PlayerPunishInfo reportInfo = new PlayerPunishInfo(0, reportedUUID, pl.getUniqueId().toString(), null, true,
                    PunishmentType.REPORT, message);
            PunishmentSql.insertNewPlayerPunishment(reportInfo);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online == pl || RankHandler.helperPermission(online)) {
                    online.sendMessage(
                            Prefixes.ALERT + "" + reporterName + " has reported " + reportedName + " for: " + message);
                }
            }
        }
    }
}