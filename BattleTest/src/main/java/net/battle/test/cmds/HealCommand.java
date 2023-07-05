package net.battle.test.cmds;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;

public class HealCommand implements CommandBase {

    private Map<UUID, Long> lastTime = new HashMap<>();
    private static final long DELAY_MILLIS = TimeUnit.SECONDS.toMillis(10L);

    public String getLabel() {
        return "heal";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Heal yourself";
    }

    public void onCommandExecute(Player executor, String[] args) {
        long currentTimeMs = System.currentTimeMillis();
        if (this.lastTime.containsKey(executor.getUniqueId())) {
            long delta = currentTimeMs - this.lastTime.get(executor.getUniqueId());
            if (delta <= DELAY_MILLIS && !RankHandler.developerPermission(executor)) {
                executor.sendMessage(Prefixes.ERROR + "You must wait 10 seconds before using this command again");
                return;
            }
        }
        executor.setHealth(20.0D);
        executor.setFoodLevel(20);
        executor.sendMessage(Prefixes.UPDATE + "You healed yourself");
        if (!RankHandler.ownerPermission(executor)) {
            executor.sendMessage(Prefixes.COMMAND + "You must wait §c10§f seconds before using this command again");
            this.lastTime.put(executor.getUniqueId(), currentTimeMs);
        }
    }

    @Override
    public String getUsage() {
        return "/heal";
    }
}