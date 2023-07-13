package net.battle.core.settings.inv;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import net.battle.core.handlers.Prefixes;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutHolder;
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
        Inventory inv = event.getClickEvent().getInventory();
        // The old data can be reused because the target is not changing
        Object oldData = ((LayoutHolder) inv.getHolder()).getData();
        if (event.getButtonId().equals("hub")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(inv, player, handler.getHubLayout(), oldData);

        } else if (event.getButtonId().equals("game")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(inv, player, handler.getGameLayout(), oldData);

        } else if (event.getButtonId().equals("public")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(inv, player, handler.getPublicLayout(), oldData);

        }
    }

    private void handleSettingsMenuClick(PlayerInvClickEvent event, Player player) {
        Inventory inv = event.getClickEvent().getInventory();
        Object oldData = ((LayoutHolder) inv.getHolder()).getData();
        if (event.getButtonId().equals("back")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.transferInventoryLayout(inv, player, handler.getHomeLayout(), oldData);
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
            event.getLayout().updateInventory(inv, player);
        }
    }
}