package net.battle.core.command.cmds;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.battle.core.BMMacro;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;

public class HelpOpCommand implements CommandBase {
    public static Map<UUID, Long> cooldown = new HashMap<>();
    public static final long MS_DELAY = TimeUnit.SECONDS.toMillis(10L);

    public String getLabel() {
        return "helpop";
    }

    public String[] getAliases() {
        return new String[] { "a" };
    }

    @Override
    public String getDescription() {
        return "Get help from an operator";
    }

    public void onCommandExecute(Player pl, String[] args) {
        long nowMs = System.currentTimeMillis();
        if (cooldown.containsKey(pl.getUniqueId()) && cooldown.get(pl.getUniqueId()) < nowMs + MS_DELAY) {
            pl.sendMessage("§4§lERROR§8 > §cYou must wait 10 seconds in between uses of /helpop.");
            return;
        }
        if (args.length == 0) {
            CommandHandler.sendUsage(pl, this);
            return;
        }

        String msg = CommandHandler.getSpacedArgument(args, " ");

        pl.sendMessage("§e§lALERT§8 > §fMessage from §a" + BMMacro.CTS.serialize(pl.displayName()) + "§f: " + msg);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (pl == online) {
                continue;
            }
            if (RankHandler.helperPermission(pl)) {
                online.sendMessage(
                        "§e§lALERT§8 > §fMessage from §a" + BMMacro.CTS.serialize(pl.displayName()) + "§f: " + msg);
            }
        }

        cooldown.put(pl.getUniqueId(), nowMs);
    }

    @Override
    public String getUsage() {
        return "/helpop <msg>";
    }
}