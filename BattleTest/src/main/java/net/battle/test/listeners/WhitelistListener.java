package net.battle.test.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import net.battle.core.handlers.Rank;
import net.battle.core.handlers.RankHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.test.BMTestPlugin;
import net.battle.test.cmds.ShutdownCommand;
import net.kyori.adventure.text.Component;

public class WhitelistListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (BMTestPlugin.ACTIVE_PLUGIN.getAllowed().contains(e.getUniqueId().toString())) {
            return;
        }
        if (BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE)) {
            return;
        }
        Rank r = RankHandler.getRankFromSQLName(PlayerInfoSql.getPlayerInfo(e.getUniqueId().toString()).getSqlRank());
        if (ShutdownCommand.shutdown && r != Rank.OWNER) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST,
                    Component.text("§cThis server is currently in shutdown mode."));
            return;
        }
        if (r == Rank.DEVELOPER || r == Rank.OPERATOR || r == Rank.OWNER) {
            return;
        }
        if (r != Rank.OWNER) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST,
                    Component.text("§cThis server is on shutdown, meaning only owners can join."));
            return;
        }
    }
}
