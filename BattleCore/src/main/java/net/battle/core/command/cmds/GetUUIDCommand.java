package net.battle.core.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class GetUUIDCommand implements CommandBase {
    public String getLabel() {
        return "uuid";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Get a player's UUID";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (args.length != 1) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        String targetUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
        pl.sendMessage(Component.text("§9§lCOMMAND§8 > §f§c" + args[0] + "§f's UUID is §c")
                .append(Component.text(targetUUID).clickEvent(ClickEvent.copyToClipboard(targetUUID)))
                .append(Component.text(".").color(NamedTextColor.WHITE)));
    }

    public String getUsage() {
        return "/uuid <player";
    }
}