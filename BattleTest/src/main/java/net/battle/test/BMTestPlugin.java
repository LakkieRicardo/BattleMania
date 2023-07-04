package net.battle.test;

import net.battle.core.BMCorePlugin;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.RestartHandler;
import net.battle.test.cmds.AllowCommand;
import net.battle.test.cmds.ClearDropsCommand;
import net.battle.test.cmds.HealCommand;
import net.battle.test.cmds.HungerCommand;
import net.battle.test.cmds.LobbyCommand;
import net.battle.test.cmds.LockdownCommand;
import net.battle.test.cmds.NewsCommand;
import net.battle.test.cmds.ShutdownCommand;
import net.battle.test.cmds.TeleportAllCommand;
import net.battle.test.cmds.UtilityCommand;
import net.battle.test.cmds.WorldTPCommand;
import net.battle.test.listeners.BoothListener;
import net.battle.test.listeners.ChairListener;
import net.battle.test.listeners.FadeListener;
import net.battle.test.listeners.GoldAppleListener;
import net.battle.test.listeners.HungerListener;
import net.battle.test.listeners.WeatherListener;
import net.battle.test.listeners.WhitelistListener;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BMTestPlugin extends JavaPlugin {
    
    public static BMTestPlugin ACTIVE_PLUGIN;

    public static final String CONFIG_HUNGER = "hunger";
    public static final String CONFIG_PVP = "pvp";
    public static final String CONFIG_PVE = "pve";
    public static final String CONFIG_ALLOW_MODE = "allow";
    public static final String CONFIG_ALLOWED_LIST = "allowed";
    public static final String CONFIG_LOCKDOWN = "lockdown";

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        ACTIVE_PLUGIN = this;

        BMLogger.info("Creating test server commands...");
        CommandHandler.registerCommand(new NewsCommand());
        CommandHandler.registerCommand(new LobbyCommand());
        CommandHandler.registerCommand(new WorldTPCommand());
        CommandHandler.registerCommand(new HungerCommand());
        CommandHandler.registerCommand(new LockdownCommand());
        CommandHandler.registerCommand(new ClearDropsCommand());
        CommandHandler.registerCommand(new ShutdownCommand());
        CommandHandler.registerCommand(new UtilityCommand());
        CommandHandler.registerCommand(new HealCommand());
        CommandHandler.registerCommand(new AllowCommand());
        CommandHandler.registerCommand(new TeleportAllCommand());

        BMLogger.info("Creating test server listeners...");
        registerListener(new WhitelistListener());
        registerListener(new WeatherListener());
        registerListener(new HungerListener());
        registerListener(new BoothListener());
        registerListener(new ChairListener());
        registerListener(new FadeListener());
        registerListener(new GoldAppleListener());

        RestartHandler.startTimer();

        BMCorePlugin.ACTIVE_PLUGIN.setAllowBlockBreak(true);
    }

    public void onDisable() {
        saveConfig();
    }

    public static void registerListener(Listener l) {
        Bukkit.getPluginManager().registerEvents(l, (Plugin) ACTIVE_PLUGIN);
        BMLogger.info("Registered listener " + l.getClass().getName());
    }

    public static World getWorld(String name) {
        World w = Bukkit.getWorld(name);
        if (w == null) {
            WorldCreator creator = new WorldCreator(name);
            w = creator.createWorld();
        }
        return w;
    }
    
    public synchronized void setConfigBoolean(String key, boolean value) {
        getConfig().set(key, value);
    }

    public boolean getConfigBoolean(String key) {
        return getConfig().getBoolean(key);
    }

    public List<String> getAllowed() {
        return getConfig().getStringList(CONFIG_ALLOWED_LIST);
    }

    public void removeAllowed(UUID uuid) {
        List<String> allowed = getAllowed();
        allowed.remove(uuid.toString());
        getConfig().set(CONFIG_ALLOWED_LIST, allowed);
    }

    public void addAllowed(UUID uuid) {
        List<String> allowed = getAllowed();
        allowed.add(uuid.toString());
        getConfig().set(CONFIG_ALLOWED_LIST, allowed);
    }
}