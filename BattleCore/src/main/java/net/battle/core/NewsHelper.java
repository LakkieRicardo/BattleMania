package net.battle.core;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public class NewsHelper {

    private NewsHelper() {
    }

    public static void DisplayNews(Player pl) {
        String titleStr = "§9NEWS §9§o" + BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("version");
        String subtitleStr = BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("news");
        pl.showTitle(Title.title(Component.text(titleStr), Component.text(subtitleStr)));
    }

}
