package net.battle.test.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import net.battle.core.handlers.Rank;
import net.battle.core.handlers.RankHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.test.BMTestPlugin;
import net.kyori.adventure.text.Component;

public class WhitelistListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        Rank r = RankHandler.getRankFromSQLName(PlayerInfoSql.getPlayerInfo(e.getUniqueId().toString()).sqlRank());
        if (r == Rank.OPERATOR || r == Rank.DEVELOPER || r == Rank.OWNER) {
            return;
        }
        if (BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE)  && !BMTestPlugin.ACTIVE_PLUGIN.getAllowed().contains(e.getUniqueId().toString())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("§cThis server is currently in allow mode and you are not whitelisted."));
            return;
        }
    }
}
