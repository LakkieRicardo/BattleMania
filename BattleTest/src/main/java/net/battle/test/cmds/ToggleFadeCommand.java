package net.battle.test.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.test.BMTestPlugin;
import net.kyori.adventure.text.Component;

public class ToggleFadeCommand implements CommandBase {

    @Override
    public String getUsage() {
        return "/fade <confirm|check>";
    }

    @Override
    public String getLabel() {
        return "fade";
    }

    @Override
    public String getDescription() {
        return "Toggles whether or not block fade events will occur";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "togglefade" };
    }

    @Override
    public void onCommandExecute(Player executor, String[] args) {
        if (!RankHandler.developerPermission(executor)) {
            CommandHandler.sendPerms(executor);
            return;
        }

        boolean currentValue = BMTestPlugin.ACTIVE_PLUGIN.getConfigBoolean(BMTestPlugin.CONFIG_FADE);

        if (args.length != 1) {
            executor.sendMessage(Prefixes.error + "No option specified. Type \"§c§oconfirm§c\" to actually change value, type \"§c§ocheck§c\" to check value.");
            return;
        }
        
        if (args[0].equals("check")) {
            executor.sendMessage(Prefixes.cmd + "Currently, allow block fade events is set to §c" + currentValue + "§f.");
        } else if (args[0].equals("confirm")) {
            Bukkit.broadcast(Component.text(Prefixes.alert + "Player §c" + executor.getName() + "§f has set allow block fade events to §c" + !currentValue + "§f."));
            BMTestPlugin.ACTIVE_PLUGIN.setConfigBoolean(BMTestPlugin.CONFIG_FADE, !currentValue);
        } else {
            CommandHandler.sendUsage(executor, this);
        }

     }
    
}
