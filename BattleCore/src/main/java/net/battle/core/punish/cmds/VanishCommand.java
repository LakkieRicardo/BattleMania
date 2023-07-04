package net.battle.core.punish.cmds;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.handlers.RankHandler;
import net.battle.core.handlers.TempHandler;

public class VanishCommand implements CommandBase {
    public String getLabel() {
        return "vanish";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Toggle a player's vanish";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.helperPermission(executor)) {
            executor.sendMessage("§4§lERROR§8 > §cNot enough permission");
            return;
        }

        TempHandler.handleTogglePlayerVanish(executor);
    }

    @Override
    public String getUsage() {
        return "/vanish";
    }
}