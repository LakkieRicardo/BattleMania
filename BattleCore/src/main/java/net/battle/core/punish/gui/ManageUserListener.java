package net.battle.core.punish.gui;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.battle.core.BMCorePlugin;
import net.battle.core.BMTextConvert;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.handlers.StringUtility;
import net.battle.core.handlers.TempHandler;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.plinv.PlayerInvData;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.punish.PunishManager;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.records.PlayerPunishInfo;
import net.battle.core.sql.records.PunishType;

public class ManageUserListener implements Listener {
    public static final Map<String, String> REASONS = new HashMap<>();
    public static final Map<String, Boolean> PERMANENT = new HashMap<>();

    @EventHandler
    public void onPlayerInventoryEvent(InventoryClickEvent e) {
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
        String reason = REASONS.get(pl.getName());

        if (BMTextConvert.CTS.serialize(inv.title()).startsWith("Manage")) {

            e.setCancelled(true);
            ItemStack skull = inv.getItem(4);
            String targetName = ((SkullMeta) skull.getItemMeta()).getOwningPlayer().getName();
            OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetName);

            if (m == Material.WOODEN_AXE) {
                if (e.getClick() == ClickType.MIDDLE) {
                    PlayerPunishInfo warningInfo = new PlayerPunishInfo(0, targetOffline.getUniqueId().toString(), pl.getUniqueId().toString(), null, true, PunishType.WARN, reason);
                    PunishmentSql.insertNewPlayerPunishment(warningInfo);
                    String msg = Prefixes.PUNISH + "The user §c" + targetName + "§f has been warned by §c" + pl.getName();
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (RankHandler.helperPermission(all)) {
                            all.sendMessage(msg);
                        }
                    }
                } else {
                    pl.sendMessage(Prefixes.COMMAND + "Opened punish inventory");
                    pl.closeInventory();
                    pl.openInventory(ManageUserHandler.getPunish(targetName, pl));
                }

                return;
            }
            if (m == Material.BOOK) {
                pl.sendMessage(Prefixes.COMMAND + "Opened punishment list inventory");
                pl.closeInventory();
                pl.openInventory(ManageUserHandler.getPunishList(targetName, targetOffline.getUniqueId().toString()));
                return;
            }
            if (m == Material.COMPASS) {
                pl.sendMessage(Prefixes.COMMAND + "Opened staff tools");
                pl.closeInventory();
                pl.openInventory(ManageUserHandler.getStaffTools(targetName));
                return;
            }
            if (m == Material.COMPARATOR) {
                if (!RankHandler.operatorPermission(pl)) {
                    CommandHandler.sendPerms(pl);
                    return;
                }
                pl.closeInventory();
                pl.openInventory(InvLayout.initializeInventory(BMCorePlugin.ACTIVE_PLUGIN.settingsHandler.getHomeLayout(), new PlayerInvData(targetOffline), pl).getInventory());
                return;
            }
            if (m == Material.PISTON) {
                pl.sendMessage(Prefixes.COMMAND + "Opened move to a server");
                pl.closeInventory();
                pl.openInventory(ManageUserHandler.getMoveInventory1(targetName));
                return;
            }
            return;
        }
        if (BMTextConvert.CTS.serialize(inv.title()).equalsIgnoreCase("Move Player")) {

            e.setCancelled(true);
            ItemStack skull = inv.getItem(4);
            String t = ((SkullMeta) skull.getItemMeta()).getOwningPlayer().getName();

            Player targetPlayer = CommandHandler.getPlayer(t);

            if (m == Material.RED_BED) {
                pl.closeInventory();
                pl.openInventory(ManageUserHandler.getMain(pl, targetPlayer));
            }

            if (m == Material.CRAFTING_TABLE) {
                if (targetPlayer == null) {
                    pl.sendMessage(Prefixes.ERROR + "This player must be online to send to another server!");
                    return;
                }
                pl.closeInventory();
                ProxyHandler.sendToServer(Arrays.asList(targetPlayer.getUniqueId()), "Lobby");
                pl.sendMessage(Prefixes.COMMAND + "You have sent §c" + t + "§f to §cLobby");

                return;
            }

            if (m == Material.COBBLESTONE) {
                if (targetPlayer == null) {
                    pl.sendMessage(Prefixes.ERROR + "This player must be online to send to another server!");
                    return;
                }
                pl.closeInventory();
                ProxyHandler.sendToServer(Arrays.asList(targetPlayer.getUniqueId()), "BW");
                pl.sendMessage(Prefixes.COMMAND + "You have sent §c" + t + "§f to §cLobby");

                return;
            }
            if (m == Material.FIRE_CHARGE) {
                if (targetPlayer == null) {
                    pl.sendMessage(Prefixes.ERROR + "This player must be online to send to another server!");
                    return;
                }
                pl.closeInventory();
                pl.sendMessage(Prefixes.ERROR + "TankDefender is still W.I.P.");

                return;
            }
        }

        if (BMTextConvert.CTS.serialize(inv.title()).equalsIgnoreCase("Punishments")) {

            e.setCancelled(true);
            ItemStack skull = inv.getItem(4);
            String t = ((SkullMeta) skull.getItemMeta()).getOwningPlayer().getName();
            OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(t);

            if (m == Material.RED_BED) {
                pl.closeInventory();
                pl.openInventory(ManageUserHandler.getMain(pl, targetOffline));
                return;
            }

            if (m == Material.REDSTONE_BLOCK) {
                if (InventoryUtils.loreContainsString(clicked.getItemMeta(), "§cActivity: §7Active")) {
                    if (!PunishManager.handleDisablePunishment(pl, targetOffline, PunishType.BAN, e.getCurrentItem())) {
                        BMLogger.severe("Failed to disable ban for " + targetOffline.getName());
                        pl.sendMessage(Prefixes.PUNISH + "The system failed to disable the ban.");
                        return;
                    }
                    pl.sendMessage(Prefixes.PUNISH + "The user §c" + t + "§f has been unbanned.");
                    pl.closeInventory();
                    return;
                }
                pl.sendMessage(Prefixes.ERROR + "You cannot deactive this punishment as it is not active");
                return;
            }

            if (m == Material.BARRIER) {
                if (InventoryUtils.loreContainsString(clicked.getItemMeta(), "§cActivity: §7Active")) {
                    if (!PunishManager.handleDisablePunishment(pl, targetOffline, PunishType.MUTE, e.getCurrentItem())) {
                        BMLogger.severe("Failed to disable mute for " + targetOffline.getName());
                        pl.sendMessage(Prefixes.PUNISH + "The system failed to disable the mute.");
                        return;
                    }
                    pl.sendMessage(Prefixes.PUNISH + "The user §c" + t + "§f has been unmuted.");
                    if (!pl.getUniqueId().equals(targetOffline.getUniqueId()) && targetOffline.isOnline()) {
                        Player targetOnline = Bukkit.getPlayer(targetOffline.getUniqueId());
                        targetOnline.sendMessage(Prefixes.PUNISH + "You have been unmuted by §c" + pl.getName() + "§f.");
                    }
                    pl.closeInventory();
                    return;
                }
                pl.sendMessage(Prefixes.ERROR + "You cannot deactive this punishment as it is not active");
                return;
            }
            return;
        }

        if (BMTextConvert.CTS.serialize(inv.title()).equalsIgnoreCase("Punish")) {
            e.setCancelled(true);
            ItemStack skull = inv.getItem(4);
            String targetName = ((SkullMeta) skull.getItemMeta()).getOwningPlayer().getName();
            OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetName);

            if (m == Material.RED_BED) {
                pl.closeInventory();
                pl.openInventory(ManageUserHandler.getMain(pl, targetOffline));
                return;
            }

            if (m == Material.PAPER) {
                PlayerPunishInfo warningInfo = new PlayerPunishInfo(0, targetOffline.getUniqueId().toString(), pl.getUniqueId().toString(), null, true, PunishType.WARN, reason);
                PunishmentSql.insertNewPlayerPunishment(warningInfo);
                String msg = Prefixes.PUNISH + "The user §c" + targetName + "§f has been warned by §c" + pl.getName();
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (RankHandler.helperPermission(all)) {
                        all.sendMessage(msg);
                    }
                }
            }

            if (m == Material.BARRIER) {
                PlayerPunishInfo muteInfo = new PlayerPunishInfo(0, targetOffline.getUniqueId().toString(), pl.getUniqueId().toString(), null, true, PunishType.MUTE, reason);
                PunishmentSql.insertNewPlayerPunishment(muteInfo);
                String msg = Prefixes.PUNISH + "The user §c" + targetName + "§f has been muted for §c§lPERMANENT§f by §c" + pl.getName();
                for (Player foo : Bukkit.getOnlinePlayers()) {
                    if (RankHandler.helperPermission(foo)) {
                        foo.sendMessage(msg);
                    }
                }
            }

            if (m == Material.REDSTONE_BLOCK) {
                PlayerPunishInfo banInfo = new PlayerPunishInfo(0, targetOffline.getUniqueId().toString(), pl.getUniqueId().toString(), null, true, PunishType.BAN, reason);
                PunishmentSql.insertNewPlayerPunishment(banInfo);
                String msg = Prefixes.PUNISH + "The user §c" + targetName + "§f has been banned for §c§lPERMANENT§f by §c" + pl.getName();
                for (Player foo : Bukkit.getOnlinePlayers()) {
                    if (RankHandler.helperPermission(foo)) {
                        foo.sendMessage(msg);
                    }
                }
            }

            if (m == Material.GREEN_CONCRETE || m == Material.RED_CONCRETE || m == Material.YELLOW_CONCRETE) {
                if (InventoryUtils.getItemLore(clicked) == null) {
                    return;
                }
                if (InventoryUtils.getItemLore(clicked).size() == 0) {
                    return;
                }
                List<String> itemLore = InventoryUtils.getItemLore(clicked);
                String lore = StringUtility.assemble(itemLore, 0, itemLore.size(), "%br%");
                PunishType type = null;
                if (lore.contains("Mute")) {
                    type = PunishType.MUTE;
                }
                if (lore.contains("Ban")) {
                    type = PunishType.BAN;
                }
                if (type == null) {
                    return;
                }
                int dayCount = -1;

                if (lore.contains("1 Day")) {
                    dayCount = 1;
                }
                if (lore.contains("7 Days")) {
                    dayCount = 7;
                }
                if (lore.contains("1 Month")) {
                    dayCount = 30;
                }
                if (lore.contains("5 Days")) {
                    dayCount = 5;
                }
                if (lore.contains("2 Months")) {
                    dayCount = 60;
                }

                if (type == PunishType.BAN) {
                    PlayerPunishInfo banInfo = new PlayerPunishInfo(0, targetOffline.getUniqueId().toString(), pl.getUniqueId().toString(), new Date(System.currentTimeMillis()
                            + TimeUnit.DAYS.toMillis(dayCount)), true, PunishType.BAN, reason);
                    PunishmentSql.insertNewPlayerPunishment(banInfo);
                    ProxyHandler.kickPlayer(pl, targetOffline.getUniqueId().toString(), String.join("\n", PunishManager.getBanMessage(banInfo)));
                    String msg = Prefixes.PUNISH + "The user §c" + targetName + "§f has been banned for §c§l" + dayCount + " DAYS§f by §c" + pl.getName();
                    for (Player foo : Bukkit.getOnlinePlayers()) {
                        if (RankHandler.helperPermission(foo)) {
                            foo.sendMessage(msg);
                        }
                    }
                    pl.closeInventory();
                    return;
                }
                if (type == PunishType.MUTE) {
                    PlayerPunishInfo muteInfo = new PlayerPunishInfo(0, targetOffline.getUniqueId().toString(), pl.getUniqueId().toString(), new Date(System.currentTimeMillis()
                            + TimeUnit.DAYS.toMillis(dayCount)), true, PunishType.MUTE, reason);
                    PunishmentSql.insertNewPlayerPunishment(muteInfo);
                    if (targetOffline.isOnline()) {
                        Player player = Bukkit.getPlayer(targetOffline.getUniqueId());
                        for (String str : PunishManager.getMuteMessages(muteInfo)) {
                            player.sendMessage(str);
                        }
                    }
                    String msg = Prefixes.PUNISH + "The user §c" + targetName + "§f has been muted for §c§l" + dayCount + " DAYS§f by §c" + pl.getName();
                    for (Player foo : Bukkit.getOnlinePlayers()) {
                        if (RankHandler.helperPermission(foo)) {
                            foo.sendMessage(msg);
                        }
                    }
                    pl.closeInventory();

                    return;
                }
                if (dayCount == -1) {
                    return;
                }
            }

            return;
        }

        if (BMTextConvert.CTS.serialize(inv.title()).equalsIgnoreCase("Staff Tools")) {
            e.setCancelled(true);
            ItemStack skull = inv.getItem(4);
            String targetName = ((SkullMeta) skull.getItemMeta()).getOwningPlayer().getName();
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

            if (m == Material.RED_BED) {
                pl.closeInventory();
                pl.openInventory(ManageUserHandler.getMain(pl, target));

                return;
            }
            if (m == Material.COMPASS) {
                if (!ProxyHandler.isPlayerOnline(targetName)) {
                    pl.sendMessage(Prefixes.ALERT + "§c" + targetName + "§f is offline");
                    return;
                }
                pl.sendMessage(Prefixes.ALERT + "§c" + targetName + "§f is on server §c" + ProxyHandler.getPlayerServer(targetName));
                return;
            }
            if (m == Material.ICE) {
                Player targetOnline = Bukkit.getPlayerExact(targetName);
                if (targetOnline == null) {
                    TempHandler.handleToggleOfflinePlayerFrozen(pl, target);
                    return;
                }
                TempHandler.handleTogglePlayerFrozen(pl, targetOnline);
                return;
            }
            if (m == Material.GLASS) {
                if (!ProxyHandler.isPlayerOnline(targetName)) {
                    pl.sendMessage(Prefixes.ALERT + "§c" + targetName + "§f is offline");
                    return;
                }
                TempHandler.handleTogglePlayerVanish(pl);
                String targetServer = ProxyHandler.getPlayerServer(targetName);
                pl.sendMessage(Prefixes.ALERT + "Sending you to server §c" + targetServer + "§f in vanish mode");
                pl.closeInventory();
                ProxyHandler.sendToServer(Arrays.asList(pl.getUniqueId()), targetServer);
                return;
            }
        }
    }
}