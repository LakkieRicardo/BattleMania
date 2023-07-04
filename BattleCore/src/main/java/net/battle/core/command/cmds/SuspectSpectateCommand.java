package net.battle.core.command.cmds;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;

public class SuspectSpectateCommand implements CommandBase {
    public static Map<String, Location> lastLocation = new HashMap<>();
    public static Map<String, GameMode> lastGamemode = new HashMap<>();

    public String getLabel() {
        return "susspect";
    }

    public String[] getAliases() {
        return new String[] { "ss", "suspectspectate" };
    }

    @Override
    public String getDescription() {
        return "Spectate a player";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.helperPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (args.length != 1) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        Player t = CommandHandler.getPlayer(args[0]);

        if (t == null) {
            pl.sendMessage("§4§lERROR§8 > §cInvalid player");

            return;
        }
        lastLocation.put(pl.getName(), pl.getLocation());
        lastGamemode.put(pl.getName(), pl.getGameMode());

        pl.setGameMode(GameMode.SPECTATOR);
        pl.teleport(t.getLocation());
    }

    @Override
    public String getUsage() {
        return "/susspect <player>";
    }
}