package net.battle.core.command.cmds;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.command.ICommandInfo;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;

public class CommandListCommand implements CommandBase {
    public String getLabel() {
        return "cmdlist";
    }

    public String[] getAliases() {
        return new String[] { "commandlist" };
    }

    @Override
    public String getDescription() {
        return "Display and filter command descriptions";
    }

    private List<String> getDisplayableCommands() {
        List<String> result = new ArrayList<>(
                CommandHandler.getAllCommands().size() + ProxyHandler.PROXY_COMMANDS.size());

        for (ICommandInfo cmdInfo : CommandHandler.getAllCmdsWithProxyCmds()) {
            String[] aliasArray = cmdInfo.getAliases();
            if (aliasArray != null) {
                StringBuilder aliases = new StringBuilder();
                for (String alias : aliasArray) {
                    aliases.append(", ");
                    aliases.append(alias);
                }
                result.add("§7" + cmdInfo.getLabel() + " (" + aliases.substring(2) + "): " + cmdInfo.getDescription());
            } else {
                result.add("§7" + cmdInfo.getLabel() + ": " + cmdInfo.getDescription());
            }
        }

        return result;
    }

    private void executeCommandProper(Player pl, String[] args) {
        List<String> commands = getDisplayableCommands();
        if (args.length > 0) {
            String searchTerm = CommandHandler.getSpacedArgument(args, " ");
            List<String> filteredCommands = new ArrayList<>();
            for (String command : commands) {
                if (command.toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredCommands.add(command.replaceAll("(?i)" + Pattern.quote(searchTerm), "§c$0§7"));
                }
            }
            commands = filteredCommands;
        }
        pl.sendMessage(Prefixes.COMMAND + "Commands§7 (" + commands.size() + ")§f:");
        for (String command : commands) {
            pl.sendMessage(command);
        }
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.developerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }

        if (ProxyHandler.PROXY_COMMANDS.size() == 0) {
            pl.sendMessage(Prefixes.COMMAND + "Querying for commands...");            
            ProxyHandler.queryForProxyCommands();
            Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, () -> {
                executeCommandProper(pl, args);
            }, 20L);
        } else {
            executeCommandProper(pl, args);
        }
    }

    public String getUsage() {
        return "/cmdlist [filter...]";
    }
}