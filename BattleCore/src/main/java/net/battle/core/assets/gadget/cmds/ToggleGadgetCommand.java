package net.battle.core.assets.gadget.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.command.CommandBase;
import net.battle.core.handlers.RankHandler;
import net.battle.core.sql.impl.PlayerInfoSql;

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
            pl.sendMessage("§4§lERROR§8 > §cNot enough permission for this");

            return;
        }
        if (BMCorePlugin.ACTIVE_PLUGIN.areGadgetsAllowed()) {
            BMCorePlugin.ACTIVE_PLUGIN.setGadgetsAllowed(false);
            for (Gadget g : Gadget.getAllGadgets()) {
                for (int i = 0; i < g.getUsers().size(); i++) {
                    g.unselectGadget(g.getUsers().get(i));
                }
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(
                        "§e§lALERT§8 > §f"
                                + RankHandler.getRankFromSQLName(PlayerInfoSql.getPlayerInfo(pl).getSqlRank())
                                        .getGameName()
                                + " §f"
                                + pl.getName() + " §fhas §cdisabled§f gadgets");
            }
        } else {
            BMCorePlugin.ACTIVE_PLUGIN.setGadgetsAllowed(true);
            for (Player all : Bukkit.getOnlinePlayers())
                all.sendMessage(
                        "§e§lALERT§8 > §f"
                                + RankHandler.getRankFromSQLName(PlayerInfoSql.getPlayerInfo(pl).getSqlRank())
                                        .getGameName()
                                + " §f"
                                + pl.getName() + " §fhas §cenabled§f gadgets");
        }
    }

    @Override
    public String getUsage() {
        return "/togglegadget";
    }
}