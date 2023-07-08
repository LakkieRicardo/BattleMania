package net.battle.core.settings.inv;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import net.battle.core.handlers.Prefixes;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.plinv.PlayerInvClickEvent;
import net.battle.core.sql.impl.PlayerSettingsSql;

public class SettingListener implements Listener {

    private final SettingInvHandler handler;

    public SettingListener(SettingInvHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerInventoryLayoutClicked(PlayerInvClickEvent event) {
        event.getClickEvent().setCancelled(true);
        if (event.getButtonId() == null) {
            return;
        }

        Player player = (Player) event.getClickEvent().getWhoClicked();
        String invId = event.getLayout().getId();
        if (invId.equals("settings_home")) {
            handleHomeSettingsClick(event, player);
        } else if (invId.equals("settings_hub") || invId.equals("settings_game") || invId.equals("settings_public")) {
            handleSettingsMenuClick(event, player);
        }
    }

    private void handleHomeSettingsClick(PlayerInvClickEvent event, Player player) {
        if (event.getButtonId().equals("hub")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(event.getClickEvent().getInventory(), player, handler.getHubLayout());

        } else if (event.getButtonId().equals("game")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(event.getClickEvent().getInventory(), player, handler.getGameLayout());

        } else if (event.getButtonId().equals("public")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(event.getClickEvent().getInventory(), player, handler.getPublicLayout());

        }
    }

    private void handleSettingsMenuClick(PlayerInvClickEvent event, Player player) {
        Inventory inv = event.getClickEvent().getInventory();
        if (event.getButtonId().equals("back")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(inv, player, handler.getHomeLayout());
        } else {
            // The setting is changed to iron bars if it is disabled
            if (event.getClickEvent().getCurrentItem().getType() == Material.IRON_BARS) {
                return;
            }
            String settingId = event.getButtonId();
            boolean currentValue = PlayerSettingsSql.getSetting(event.getTargetPlayer().getUniqueId().toString(), settingId);
            PlayerSettingsSql.updateSetting(event.getTargetPlayer().getUniqueId().toString(), settingId, !currentValue);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            player.sendMessage(Prefixes.UPDATE + "Updated setting §c" + settingId + "§f to §c" + !currentValue + "§f.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            event.getLayout().updateInventoryMeta(inv, player, InvLayout.getMetaFromInv(inv));
        }
    }

    // @EventHandler
    // public void onInventoryClick(InventoryClickEvent e) {
    // if (e == null) {
    // return;
    // }
    // if (e.getInventory() == null) {
    // return;
    // }
    // if (e.getCurrentItem() == null) {
    // return;
    // }

    // Player pl = (Player) e.getWhoClicked();
    // InventoryView inv = e.getView();
    // ItemStack clicked = e.getCurrentItem();
    // Material m = clicked.getType();

    // String invTitle = BMTextConvert.CTS.serialize(inv.title());

    // if (invTitle.startsWith("Stats: ")) {
    // String targetName = invTitle.substring("Stats: ".length());
    // String targetUUID = Bukkit.getOfflinePlayer(targetName).getUniqueId().toString();
    // if (m == Material.PLAYER_HEAD) {
    // pl.closeInventory();
    // pl.openInventory(SettingInvHandler.getPublicSettings(targetUUID, targetName));
    // pl.sendMessage(Prefixes.COMMAND + "Opened public settings");
    // return;
    // }
    // if (m == Material.REPEATER) {
    // pl.closeInventory();
    // pl.openInventory(SettingInvHandler.getGameSettings(targetUUID, targetName));
    // pl.sendMessage(Prefixes.COMMAND + "Opened game settings");
    // return;
    // }
    // if (m == Material.PAPER) {
    // pl.sendMessage(Prefixes.COMMAND + "Opened hub settings");
    // pl.openInventory(getHubSettingsInventory(pl, targetUUID, targetName));
    // return;
    // }
    // } else if (invTitle.startsWith("Public Stats: ")) {
    // String targetUsername = invTitle.substring("Public Stats: ".length());
    // OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetUsername);
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("public.showswears"), false)) {
    // if (PlayerInfoSql.getPlayerInfo(pl).ingameHours() < 5.0F && !RankHandler.ownerPermission(pl)) {
    // if (pl.getName().equals(targetUsername)) {
    // pl.sendMessage(Prefixes.ERROR + "You cannot your swear setting yet");
    // }
    // return;
    // }
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "public.showswears", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("public.partyrequests"), false))
    // {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "public.partyrequests", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("public.privatemsgs"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "public.privatemsgs", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("public.playerchat"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "public.playerchat", pl, targetOffline);
    // return;
    // }
    // } else if (invTitle.startsWith("Game Stats: ")) {
    // String targetUsername = invTitle.replaceFirst("Game Stats: ", "");
    // OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetUsername);
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("game.leaveondeath"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "game.leaveondeath", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("game.particles"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "game.particles", pl, targetOffline);
    // return;
    // }
    // } else if (invTitle.startsWith("Hub Stats: ")) {
    // String targetUsername = invTitle.substring("Hub Stats: ".length());
    // OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetUsername);
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("hub.showplayers"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "hub.showplayers", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("hub.playerlaunch"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "hub.playerlaunch", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("hub.forcefield"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "hub.forcefield", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("hub.antivelocity"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "hub.antivelocity", pl, targetOffline);
    // return;
    // }
    // if (InventoryUtils.compareItemNames(clicked, SettingInvHandler.getSettingDisplayName("hub.fastsprint"), false)) {
    // SettingInvHandler.handlePlayerUpdatedSettingGUI(e, "hub.fastsprint", pl, targetOffline);
    // return;
    // }
    // }
    // }

    // public Inventory getHubSettingsInventory(Player pl) {
    // if (RankHandler.operatorPermission(pl)) {
    // return SettingInvHandler.getOperatorHubSettings(pl.getUniqueId().toString(), pl.getName());
    // }
    // if (RankHandler.moderatorPermission(pl)) {
    // return SettingInvHandler.getModeratorHubSettings(pl.getUniqueId().toString(), pl.getName());
    // }
    // return SettingInvHandler.getHubSettings(pl.getUniqueId().toString(), pl.getName());
    // }

    // public Inventory getHubSettingsInventory(Player pl, String targetUUID, String targetName) {
    // if (RankHandler.operatorPermission(pl)) {
    // return SettingInvHandler.getOperatorHubSettings(targetUUID, targetName);
    // }
    // if (RankHandler.moderatorPermission(pl)) {
    // return SettingInvHandler.getModeratorHubSettings(targetUUID, targetName);
    // }
    // return SettingInvHandler.getHubSettings(targetUUID, targetName);
    // }

    // @EventHandler
    // public void onPlayerInteract(PlayerInteractEvent e) {
    // Player pl = e.getPlayer();
    // ItemStack item = pl.getInventory().getItemInMainHand();

    // if (item.getType() == Material.REDSTONE_TORCH
    // && InventoryUtils.getItemName(item).equalsIgnoreCase("§aSettings")) {
    // if (pl.isSneaking()) {
    // return;
    // }
    // pl.openInventory(SettingInvHandler.getSettingsInventory(pl.getName()));
    // pl.sendMessage(Prefixes.COMMAND + "Opened settings");
    // e.setCancelled(true);
    // }
    // }
}