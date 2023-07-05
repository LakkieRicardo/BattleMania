package net.battle.core.assets.cmds;

import org.bukkit.entity.Player;

import net.battle.core.assets.AssetHandler;
import net.battle.core.command.CommandBase;
import net.battle.core.handlers.Prefixes;

public class AssetOpenerCommand implements CommandBase {
    public String getLabel() {
        return "asset";
    }

    public String[] getAliases() {
        return new String[] { "assets" };
    }

    @Override
    public String getDescription() {
        return "Open assets inventory";
    }

    public void onCommandExecute(Player pl, String[] args) {
        pl.openInventory(AssetHandler.getAssetInventory(pl));
        pl.sendMessage(Prefixes.COMMAND + "Opened §cAssets§f inventory");
    }

    @Override
    public String getUsage() {
        return "/asset";
    }
}