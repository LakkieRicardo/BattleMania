package net.battle.core.punish.listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.battle.core.punish.PunishManager;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.pod.PlayerPunishInfo;
import net.battle.core.sql.pod.PunishmentType;
import net.kyori.adventure.text.Component;

public class PlayerPunishmentListener implements Listener {
    
    @EventHandler
    public void onPlayerChateMute(AsyncChatEvent e) {
        Player pl = e.getPlayer();
        String uuid = pl.getUniqueId().toString();
        PunishManager.updatePlayerPunishments(uuid, PunishmentType.MUTE);
        List<PlayerPunishInfo> punishments = PunishmentSql.getPlayerActivePunishments(uuid, PunishmentType.MUTE);
        if (punishments == null || punishments.size() == 0) {
            return;
        }
        e.setCancelled(true);
        for (String s : PunishManager.getMuteMessages(punishments.get(0))) {
            pl.sendMessage(s);
        }
    }

    @EventHandler
    public void onPlayerBanJoin(AsyncPlayerPreLoginEvent e) {
        String uuid = e.getUniqueId().toString();
        PunishManager.updatePlayerPunishments(uuid, PunishmentType.BAN);
        List<PlayerPunishInfo> punishments = PunishmentSql.getPlayerActivePunishments(uuid, PunishmentType.BAN);
        if (punishments == null || punishments.size() == 0) {
            return;
        }
        
        Component fullMsgComp = null;
        for (String s : PunishManager.getBanMessage(punishments.get(0))) {
            if (fullMsgComp == null ){
                fullMsgComp = Component.text(s);
            } else {
                fullMsgComp = fullMsgComp.appendNewline().append(Component.text(s));
            }
        }

        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, fullMsgComp);
    }
}
