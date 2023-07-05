package net.battle.core.command.cmds;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;
import net.battle.core.handlers.RankHandler;

public class GamemodeCommand implements CommandBase {
    public String getLabel() {
        return "gm";
    }

    public String[] getAliases() {
        return new String[] { "gamemode" };
    }

    @Override
    public String getDescription() {
        return "Change your gamemode";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.developerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (args.length == 0) {
            GameMode gm = pl.getGameMode();
            if (gm != GameMode.CREATIVE) {
                pl.setGameMode(GameMode.CREATIVE);
                pl.sendMessage(Prefixes.COMMAND + "You are now in §7creative §fmode.");
                return;
            }
            pl.setGameMode(GameMode.SURVIVAL);
            pl.sendMessage(Prefixes.COMMAND + "You are now in §7survival §fmode.");

            return;
        }
        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                CommandHandler.sendUsage(pl, this);
                return;
            }
            String gamemode = args[0];

            if (gamemode.equalsIgnoreCase("s") || gamemode.equalsIgnoreCase("0")) {
                GameMode target = GameMode.SURVIVAL;
                pl.setGameMode(target);
                pl.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");

                return;
            }
            if (gamemode.equalsIgnoreCase("c") || gamemode.equalsIgnoreCase("1")) {
                GameMode target = GameMode.CREATIVE;
                pl.setGameMode(target);
                pl.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");

                return;
            }
            if (gamemode.equalsIgnoreCase("a") || gamemode.equalsIgnoreCase("2")) {
                GameMode target = GameMode.ADVENTURE;
                pl.setGameMode(target);
                pl.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");

                return;
            }
            if (gamemode.equalsIgnoreCase("sp") || gamemode.equalsIgnoreCase("3")) {
                GameMode target = GameMode.SPECTATOR;
                pl.setGameMode(target);
                pl.sendMessage(Prefixes.COMMAND + "You are now in §7" + target.name().toLowerCase() + "§f mode.");

                return;
            }
            Player t = CommandHandler.getPlayer(args[0]);
            if (t == null) {
                pl.sendMessage(Prefixes.ERROR + "The player is invalid.");

                return;
            }
            GameMode gm = t.getGameMode();
            if (gm != GameMode.CREATIVE) {
                t.setGameMode(GameMode.CREATIVE);
                if (t != pl) {
                    t.sendMessage(Prefixes.COMMAND + "You are now in §7creative §fmode.");
                }
                pl.sendMessage(Prefixes.COMMAND + "You have set §7" + t.getName() + "§f's gamemode to §7creative§f.");
                return;
            }
            t.setGameMode(GameMode.SURVIVAL);
            if (t != pl) {
                t.sendMessage(Prefixes.COMMAND + "You are now in survival mode.");
            }
            pl.sendMessage(Prefixes.COMMAND + "You have set §7" + t.getName() + "§f's gamemode to §7survival§f.");

            return;
        }

        if (args.length == 2) {

            String gamemode = args[0];

            Player t = CommandHandler.getPlayer(args[1]);

            if (t == null) {
                pl.sendMessage(Prefixes.ERROR + "The player is invalid.");

                return;
            }
            if (gamemode.equalsIgnoreCase("s") || gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("0")) {
                GameMode target = GameMode.SURVIVAL;
                t.setGameMode(target);
            }

            if (gamemode.equalsIgnoreCase("c") || gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("1")) {
                GameMode target = GameMode.CREATIVE;
                t.setGameMode(target);
            }

            if (gamemode.equalsIgnoreCase("a") || gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("2")) {
                GameMode target = GameMode.ADVENTURE;
                t.setGameMode(target);
            }

            if (gamemode.equalsIgnoreCase("sp") || gamemode.equalsIgnoreCase("spectator") || gamemode.equalsIgnoreCase("3")) {
                GameMode target = GameMode.SPECTATOR;
                t.setGameMode(target);
            }

            if (t != pl) {
                t.sendMessage(Prefixes.COMMAND + "Your gamemode has been updated to §7" + t.getGameMode().name().toLowerCase());
            }
            pl.sendMessage(Prefixes.COMMAND + "You have changed §7" + t.getName() + "§f's gamemode to §7" + t.getGameMode().name().toLowerCase());

            return;
        }
        CommandHandler.sendUsage(pl, this);
    }

    public static boolean isPlayerGameMode(Player pl) {
        return pl.getGameMode().equals(GameMode.CREATIVE);
    }

    @Override
    public String getUsage() {
        return "/gm [s,c,a,sp|pl] [<pl>]";
    }
}