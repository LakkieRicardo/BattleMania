package net.battle.test.cmds;

import java.util.Arrays;

import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.proxy.ProxyHandler;

public class LobbyCommand implements CommandBase {
    public String getLabel() {
        return "lobby";
    }

    public String[] getAliases() {
        String[] aliases = { "hub", "home" };
        return aliases;
    }

    @Override
    public String getDescription() {
        return "Go to lobby server";
    }

    public void onCommandExecute(Player pl, String[] args) {
        ProxyHandler.sendToServer(Arrays.asList(pl.getUniqueId()), "Lobby");
    }

    @Override
    public String getUsage() {
        return "/lobby";
    }
}
