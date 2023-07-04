package net.battle.test.security.cmd;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.handlers.RankHandler;

public class SecurityCommand implements CommandBase {
    public String getLabel() {
        return "security";
    }

    public String[] getAliases() {
        return new String[] { "sc", "secure" };
    }

    @Override
    public String getDescription() {
        return "Add, remove, or view security cameras";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (RankHandler.ownerPermission(executor)) {
            if (args.length != 2) {
                executor.sendMessage("§4§lERROR§8 > §cUsage: /security <add,remove,view> <name>");
                return;
            }
            args[0].equalsIgnoreCase("add");
        } else if (RankHandler.operatorPermission(executor)) {
            if (args.length != 2) {
                executor.sendMessage("§4§lERROR§8 > §cUsage: /security view <name>");
                return;
            }
            if (!args[0].equalsIgnoreCase("view")) {
                executor.sendMessage("§4§lERROR§8 > §cUsage: /security view <name>");
                return;
            }
        }

    }

    @Override
    public String getUsage() {
        return "/security";
    }
}
