package net.battle.core.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabDefault implements CommandTab {
    public String getCommandLabel() {
        return null;
    }

    public String[] getCommandAliases() {
        return null;
    }

    public List<String> getOptionsOnArgument(int arg, String[] typedArgs, Player pl) {
        String last = typedArgs[arg - 1];
        List<String> players = new ArrayList<>();
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.getName().startsWith(last)) {
                players.add(all.getName());
            }
        }
        return players;
    }
}