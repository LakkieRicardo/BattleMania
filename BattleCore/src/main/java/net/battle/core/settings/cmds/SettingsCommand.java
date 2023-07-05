package net.battle.core.settings.cmds;

import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;
import net.battle.core.settings.SettingHandler;
import net.battle.core.sql.impl.PlayerSettingsSql;

public class SettingsCommand implements CommandBase {
    public String getLabel() {
        return "settings";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Change a player's setting or list all settings";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("list")) {
                CommandHandler.sendUsage(pl, this);
                return;
            }
            pl.sendMessage(Prefixes.COMMAND + "Setting dislay name (ID): default value");
            for (String setting : BMCorePlugin.ACTIVE_PLUGIN.getSettingsStringList("settingList")) {
                String displayName;
                if (BMCorePlugin.ACTIVE_PLUGIN.getSettingsContains("settingDisplayNames." + setting)) {
                    displayName = BMCorePlugin.ACTIVE_PLUGIN.getSettingsString("settingDisplayNames." + setting);
                } else {
                    displayName = "<missing>";
                }
                String defaultValue;
                if (BMCorePlugin.ACTIVE_PLUGIN.getSettingsContains("settingDefaults." + setting)) {
                    defaultValue = Boolean
                            .toString(BMCorePlugin.ACTIVE_PLUGIN.getSettingsBoolean("settingDefaults." + setting));
                } else {
                    defaultValue = "<missing>";
                }
                pl.sendMessage(String.format("§9§lCOMMAND§8 > §7%s (%s): %s", displayName, setting, defaultValue));
            }
            return;
        }
        if (args.length == 2) {
            boolean value;
            if (!RankHandler.developerPermission(pl)) {
                CommandHandler.sendPerms(pl);
                return;
            }
            String setting = args[0];

            try {
                value = Boolean.parseBoolean(args[1]);
            } catch (Exception e) {
                CommandHandler.sendUsage(pl, this);
                return;
            }

            PlayerSettingsSql.updateSetting(pl.getUniqueId().toString(), setting, value);
            pl.sendMessage(Prefixes.COMMAND + "Updated setting §c" + setting + "§f to §c" + value);
            return;
        }
        pl.openInventory(SettingHandler.getSettingsInventory(pl.getName()));
        pl.sendMessage(Prefixes.COMMAND + "Opened settings");
    }

    @Override
    public String getUsage() {
        return "/settings <setting/list> [value(true/false)]";
    }
}