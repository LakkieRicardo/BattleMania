package net.battle.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HeaderFooterHandler {
    public static void updateHeaderFooter() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            updateHeaderFooter(all);
        }
    }

    public static void updateHeaderFooter(Player pl) {
        pl.sendPlayerListHeader(Component.text(BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("servertitle") + " §c" + BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("instancetitle")));
        pl.sendPlayerListFooter(Component.text(BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("serverip"))
                .color(NamedTextColor.RED).append(Component.text(" - ").color(NamedTextColor.GRAY))
                .append(Component.text(BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("websiteurl"))
                        .color(NamedTextColor.RED)));
    }
}