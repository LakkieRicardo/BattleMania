package net.battle.test.cmds;

import org.bukkit.entity.Player;

import net.battle.core.NewsHelper;
import net.battle.core.command.CommandBase;

public class NewsCommand implements CommandBase {
    public String getLabel() {
        return "news";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Display news on your screen";
    }

    public void onCommandExecute(Player pl, String[] args) {
        NewsHelper.DisplayNews(pl);
    }

    @Override
    public String getUsage() {
        return "/news";
    }
}