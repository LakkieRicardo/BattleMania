package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;
import net.battle.test.BMTestPlugin;

public class AllowCommand implements CommandBase {
    public String getLabel() {
        return "allow";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Toggle allow mode";
    }

    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.ownerPermission(executor)) {
            CommandHandler.sendPerms(executor);
            return;
        }
        if (args.length == 0) {
            if (BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE)) {
                BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE, false);
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage("§e§lALERT§8 > §fPlayer §c" + executor.getName() + "§f has disabled allow mode");
                }
            } else {
                BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_ALLOW_MODE, true);
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage("§e§lALERT§8 > §fPlayer §c" + executor.getName() + "§f has enabled allow mode");
                }
            }

            return;
        }
        if (args.length == 1) {
            OfflinePlayer t = CommandHandler.getOfflinePlayer(args[0]);
            if (t == null) {
                executor.sendMessage("§9§lCOMMAND§8 > §fInvalid player");
                return;
            }
            if (BMTestPlugin.ACTIVE_PLUGIN.getAllowed().contains(t.getUniqueId().toString())) {
                BMTestPlugin.ACTIVE_PLUGIN.removeAllowed(t.getUniqueId());
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage("§e§lALERT§8 > §fPlayer §c" + executor.getName() + "§f has removed §c" + t.getName()
                            + "§f from allow");
                }
            } else {
                BMTestPlugin.ACTIVE_PLUGIN.addAllowed(t.getUniqueId());
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage("§e§lALERT§8 > §fPlayer §c" + executor.getName() + "§f has added §c" + t.getName()
                            + "§f from allow");
                }
            }
            return;
        }
        CommandHandler.sendUsage(executor, this);
    }

    @Override
    public String getUsage() {
        return "/allow <player>";
    }
}