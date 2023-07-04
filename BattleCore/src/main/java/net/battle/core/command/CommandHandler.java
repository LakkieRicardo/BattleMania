package net.battle.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.RankHandler;
import net.battle.core.proxy.ProxyHandler;

/**
 * Utility class which handles various actions which commands may need access to, firing of commands, and command
 * registration.
 */
public class CommandHandler implements Listener {

    private static final List<CommandBase> COMMANDS = new ArrayList<>();

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String[] components;
        Player pl = e.getPlayer();
        String message = e.getMessage();

        if (message.contains(" ")) {
            components = message.split(" ");
        } else {
            components = new String[1];
            components[0] = message;
        }
        String[] args = new String[components.length - 1];
        for (int i = 1; i < components.length; i++) {
            args[i - 1] = components[i];
        }
        String label = components[0].replaceFirst("/", "");

        if ((label.toLowerCase().startsWith("bukkit:") || label.toLowerCase().startsWith("minecraft:") || label.equalsIgnoreCase("plugins")
                || label.equalsIgnoreCase("pl") || label.equalsIgnoreCase("me") || label.equalsIgnoreCase("say") || label.equalsIgnoreCase("about")
                || label.equalsIgnoreCase("version")) && !RankHandler.ownerPermission(pl)) {
            e.setCancelled(true);
            pl.sendMessage("§4§lERROR§8 > §cThis command is blocked by the server.");

            return;
        }
        for (CommandBase cmd : COMMANDS) {
            List<String> labels = new ArrayList<>();
            labels.add(cmd.getLabel());
            if (cmd.getAliases() != null) {
                for (String alias : cmd.getAliases()) {
                    labels.add(alias);
                }

            }
            for (String s : labels) {
                if (label.equalsIgnoreCase(s)) {
                    e.setCancelled(true);
                    cmd.onCommandExecute(pl, args);
                    return;
                }
            }
        }
    }

    public static List<ICommandInfo> getAllCmdsWithProxyCmds() {
        List<ICommandInfo> commandInfoList = new ArrayList<>(CommandHandler.getAllCommands().size() + ProxyHandler.PROXY_COMMANDS.size());

        commandInfoList.addAll(CommandHandler.getAllCommands());
        commandInfoList.addAll(ProxyHandler.PROXY_COMMANDS);
        return commandInfoList;
    }

    /**
     * Searches for an existing command using an ICommandInfo
     * 
     * @param labelList Search term
     * @return ICommandInfo if found, null if not
     */
    public static ICommandInfo getExistingCommandInfo(List<String> labels) {
        for (ICommandInfo existingInfo : getAllCmdsWithProxyCmds()) {
            List<String> existingLbls = new ArrayList<>(Arrays.asList(existingInfo.getLabel()));
            if (existingInfo.getAliases() != null) {
                existingLbls.addAll(Arrays.asList(existingInfo.getAliases()));
            }

            for (String existingLbl : existingLbls) {
                for (String newLbl : labels) {
                    if (existingLbl.equals(newLbl)) {
                        return existingInfo;
                    }
                }
            }
        }
        return null;
    }

    public static void registerCommand(CommandBase cmd) {
        Validate.notNull(cmd);

        List<String> labelsList = new ArrayList<>(Arrays.asList(cmd.getLabel()));
        if (cmd.getAliases() != null) {
            labelsList.addAll(Arrays.asList(cmd.getAliases()));
        }
        ICommandInfo oldCommand = getExistingCommandInfo(labelsList);

        if (oldCommand != null) {
            if (cmd.getAliases() == null) {
                BMLogger.severe("Unable to register command " + cmd.getLabel() + " because command label was already taken by command " + oldCommand.getLabel()
                        + "!");
            } else {
                BMLogger.severe("Unable to register command " + cmd.getLabel() + "(" + String.join(", ", cmd.getAliases())
                        + ") because command label was already taken by command " + oldCommand.getLabel() + "!");
            }
        }

        COMMANDS.add(cmd);
        BMLogger.info("Registered command " + cmd.getClass().getName() + " (label=" + cmd.getLabel() + ")");
    }

    public static CommandBase getCommand(String label) {
        for (CommandBase cmd : COMMANDS) {
            List<String> labels = new ArrayList<>();
            labels.add(cmd.getLabel());
            if (cmd.getAliases() != null) {
                for (String alias : cmd.getAliases()) {
                    labels.add(alias);
                }

            }
            for (String s : labels) {
                if (label.equalsIgnoreCase(s)) {
                    return cmd;
                }
            }
        }
        return null;
    }

    public static boolean unregisterCommand(CommandBase cmd) {
        boolean exists = COMMANDS.contains(cmd);
        if (exists) {
            COMMANDS.remove(cmd);
        }
        BMLogger.info("Unregistered command " + cmd.getLabel() + " returning " + exists);
        return exists;
    }

    public static Player getPlayer(String name) {
        return Bukkit.getPlayerExact(name);
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    public static boolean isPlayerOnline(String uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        return player == null ? false : player.isOnline();
    }

    public static List<CommandBase> getAllCommands() {
        return COMMANDS;
    }

    public static Player getPlayerUUID(String targetString) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getUniqueId().toString().equals(targetString)) {
                return pl;
            }
        }
        return null;
    }

    public static void sendUsage(Player pl, CommandBase cmd) {
        pl.sendMessage("§4§lERROR§8 > §cUsage: " + cmd.getUsage());
    }

    /**
     * Send message that says "not enough permissions"
     * 
     * @param pl Player to send to
     */
    public static void sendPerms(Player pl) {
        CommandHandler.sendPerms(pl);
    }

    public static String getSpacedArgument(String[] args, String delimiter, int startingIdx) {
        if (args == null) {
            throw new IllegalArgumentException("String[] args was null");
        }

        String[] cutArgs = Arrays.copyOfRange(args, startingIdx, args.length);
        return String.join(delimiter, cutArgs);
    }

    /**
     * Defaults startingIdx to 0
     */
    public static String getSpacedArgument(String[] args, String delimiter) {
        return String.join(delimiter, args);
    }
}