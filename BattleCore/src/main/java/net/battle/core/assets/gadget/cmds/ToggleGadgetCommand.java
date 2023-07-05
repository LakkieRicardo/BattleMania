package net.battle.core.assets.gadget.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.command.CommandBase;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.kyori.adventure.text.Component;

public class ToggleGadgetCommand implements CommandBase {
    public String getLabel() {
        return "togglegadget";
    }

    public String[] getAliases() {
        return new String[] { "tgs", "tg", "togglegadgets" };
    }

    @Override
    public String getDescription() {
        return "Toggle whether gadgets are enabled";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.expModPermission(pl)) {
            pl.sendMessage(Prefixes.ERROR + "Not enough permission for this");

            return;
        }
        if (BMCorePlugin.ACTIVE_PLUGIN.areGadgetsAllowed()) {
            BMCorePlugin.ACTIVE_PLUGIN.setGadgetsAllowed(false);
            for (Gadget g : Gadget.getAllGadgets()) {
                for (int i = 0; i < g.getUsers().size(); i++) {
                    g.unselectGadget(g.getUsers().get(i));
                }
            }
            Bukkit.broadcast(Component.text(Prefixes.ALERT + "" + RankHandler.getRankFromSQLName(PlayerInfoSql.getPlayerInfo(pl).sqlRank()).getGameName()
                    + " §f" + pl.getName() + " §fhas §cdisabled§f gadgets"));
        } else {
            BMCorePlugin.ACTIVE_PLUGIN.setGadgetsAllowed(true);
            Bukkit.broadcast(Component.text(Prefixes.ALERT + "" + RankHandler.getRankFromSQLName(PlayerInfoSql.getPlayerInfo(pl).sqlRank()).getGameName()
                    + " §f" + pl.getName() + " §fhas §cenabled§f gadgets"));
        }
    }

    @Override
    public String getUsage() {
        return "/togglegadget";
    }
}