package net.battle.core.command.cmds;

import java.util.Arrays;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.command.ICommandInfo;
import net.battle.core.handlers.Prefixes;

public class UsageCommand implements CommandBase {

    public String getUsage() {
        return "/usage <command>";
    }

    @Override
    public String getLabel() {
        return "usage";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Retrieves command usage info";
    }

    @Override
    public void onCommandExecute(Player executor, String[] args) {
        if (args.length != 1) {
            CommandHandler.sendUsage(executor, this);
            return;
        }

        String commandLabel = args[0];
        ICommandInfo commandInfo = CommandHandler.getExistingCommandInfo(Arrays.asList(commandLabel));
        if (commandInfo == null) {
            executor.sendMessage(Prefixes.error + "Unable to find command with label/alias §c§o" + commandLabel);
            return;
        }
        executor.sendMessage(Prefixes.cmd + "§7Usage: §f" + commandInfo.getUsage());
    }

}
