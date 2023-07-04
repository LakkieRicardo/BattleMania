package net.battle.core.command.cmds;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TitleCommand implements CommandBase {
    public String getLabel() {
        return "title";
    }

    public String[] getAliases() {
        return new String[] { "announce", "sendtitle" };
    }

    @Override
    public String getDescription() {
        return "Send title to all players in this server";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.moderatorPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (args.length == 0) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        String textFull = CommandHandler.getSpacedArgument(args, " ");

        if (!textFull.contains(":")) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        String[] titles = textFull.split(":");
        String title = titles[0].replaceAll("&", "§");
        String subtitle = titles[1].replaceAll("&", "§");

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.showTitle(Title.title(Component.text(title), Component.text(subtitle)));
        }
    }

    @Override
    public String getUsage() {
        return "/title <title:subtitle>";
    }
}