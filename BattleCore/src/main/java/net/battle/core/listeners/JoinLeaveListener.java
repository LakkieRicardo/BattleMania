package net.battle.core.listeners;

import net.battle.core.HeaderFooterHandler;
import net.battle.core.NewsHelper;
import net.battle.core.assets.AssetHandler;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.handlers.ScoreboardHandler;
import net.battle.core.handlers.TempHandler;
import net.battle.core.settings.inv.SettingInvHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.records.PlayerInfo;
import net.kyori.adventure.text.Component;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();
        String playerUUID = pl.getUniqueId().toString();

        // Initialize player settings if needed
        List<String> missingPlayerSettings = SettingInvHandler.getPlayerMissingSettings(playerUUID);
        if (missingPlayerSettings.size() > 0) {
            SettingInvHandler.insertPlayerDefaultSettings(playerUUID, missingPlayerSettings);
        }

        // Initialize player info
        PlayerInfo info = PlayerInfoSql.getPlayerInfo(pl);
        if (info == null) {
            info = new PlayerInfo(pl.getUniqueId().toString(), "player", 0, 500, 100, 0.0F);
            PlayerInfoSql.updateOrInsertInfo(info);
        }

        // Initialize news, scoreboard, and tab list
        pl.playerListName(Component.text((RankHandler.getRankFromSQLName(info.sqlRank()).getGameName() + " §a" + pl.getName()).trim()));
        pl.setScoreboard(ScoreboardHandler.BLANK_BOARD);
        NewsHelper.DisplayNews(pl);
        HeaderFooterHandler.updateHeaderFooter(pl);
        ScoreboardHandler.setupScoreboard(pl);
        ScoreboardHandler.updateScoreboard(pl);
        TempHandler.handlePlayerJoin(pl);
        e.joinMessage(Component.text(Prefixes.JOIN + "" + pl.getName()));
        pl.setHealth(20.0D);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        TempHandler.handlePlayerLeave(player);
        player.setScoreboard(ScoreboardHandler.BLANK_BOARD);
        e.quitMessage(Component.text(Prefixes.QUIT + "" + player.getName()));
    }

    public static void resetInventory(Player pl) {
        ItemStack setting = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta0 = setting.getItemMeta();
        meta0.displayName(Component.text("§aSettings"));
        setting.setItemMeta(meta0);
        ItemStack punish = new ItemStack(Material.IRON_AXE);
        ItemMeta punishMeta = punish.getItemMeta();
        if (RankHandler.helperPermission(pl)) {
            punishMeta.displayName(Component.text("§aCheck Reports"));
        } else {
            punishMeta.displayName(Component.text("§aReport"));
        }
        punishMeta.setUnbreakable(true);
        punish.setItemMeta(punishMeta);
        pl.getInventory().clear();
        pl.getInventory().setItem(0, AssetHandler.getAssetItem());
        pl.getInventory().setItem(1, Gadget.NO_GADGET_ITEM);
        pl.getInventory().setItem(4, setting);
        pl.getInventory().setItem(8, punish);
        pl.updateInventory();
    }
}