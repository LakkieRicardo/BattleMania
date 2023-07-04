package net.battle.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.battle.core.BMMacro;
import net.battle.core.handlers.Rank;
import net.battle.core.handlers.RankHandler;
import net.battle.core.handlers.SwearHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.impl.PlayerSettingsSql;
import net.battle.core.sql.pod.PlayerInfo;
import net.kyori.adventure.text.Component;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        Player pl = e.getPlayer();
        PlayerInfo info = PlayerInfoSql.getPlayerInfo((OfflinePlayer) pl);
        Rank r = RankHandler.getRankFromName(info.getSqlRank());
        String message;
        if (RankHandler.getPlayerRank(pl) == Rank.OWNER) {
            message = BMMacro.CTS.serialize(e.message()).replaceAll("%", "%%").replaceAll("&", "§");
        } else {
            message = BMMacro.CTS.serialize(e.message()).replaceAll("%", "%%");
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (!PlayerSettingsSql.getSetting(all.getUniqueId().toString(), "public.playerchat") && all != pl) {
                e.viewers().remove(all);
                return;
            }
        }

        e.renderer((source, sourceDisplayName, messageComponent, viewer) -> {
            String filteredMessage = message;
            if (PlayerSettingsSql.getSetting(source.getUniqueId().toString(), "public.showswears")) {
                filteredMessage = SwearHandler.processString(SwearHandler.PROCESS_MODE_STAR, "ssa-1", message);
            } else {
                filteredMessage = SwearHandler.processString(SwearHandler.PROCESS_MODE_UNDERLINE, "ssa-1", message);
            }

            return Component.text(String.format("§8(§7%d§8) %s §a%s§7: §f%s", info.getLevel(), r.getGameName(),
                    pl.getName(), filteredMessage).trim().replaceAll("  ", " "));
        });
    }
}