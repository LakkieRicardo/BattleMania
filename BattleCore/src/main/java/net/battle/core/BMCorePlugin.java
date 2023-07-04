package net.battle.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import net.battle.core.assets.AssetHandler;
import net.battle.core.assets.cmds.AssetOpenerCommand;
import net.battle.core.assets.gadget.cmds.GadgetBypassCommand;
import net.battle.core.assets.gadget.cmds.ToggleGadgetCommand;
import net.battle.core.assets.gadget.listeners.GadgetInventoryListener;
import net.battle.core.assets.hats.cmds.HatCommand;
import net.battle.core.assets.hats.cmds.HatInventoryCommand;
import net.battle.core.assets.hats.listeners.HatInventoryListener;
import net.battle.core.assets.hats.listeners.HatLeaveListener;
import net.battle.core.assets.listeners.AssetChestListener;
import net.battle.core.assets.listeners.AssetInventoryListener;
import net.battle.core.assets.particle.cmds.ParticleCommand;
import net.battle.core.assets.particle.cmds.ParticleInventoryCommand;
import net.battle.core.assets.particle.handlers.ParticleUtility;
import net.battle.core.assets.particle.impl.ParticleHalo;
import net.battle.core.assets.particle.impl.ParticleHex;
import net.battle.core.assets.particle.impl.ParticleJail;
import net.battle.core.assets.particle.listeners.ParticleInventoryListener;
import net.battle.core.command.CommandHandler;
import net.battle.core.command.cmds.CommandListCommand;
import net.battle.core.command.cmds.EffectCommand;
import net.battle.core.command.cmds.EntityCommand;
import net.battle.core.command.cmds.EvacuateCommand;
import net.battle.core.command.cmds.GamemodeCommand;
import net.battle.core.command.cmds.GetUUIDCommand;
import net.battle.core.command.cmds.GimmeCommand;
import net.battle.core.command.cmds.HeadCommand;
import net.battle.core.command.cmds.HologramCommand;
import net.battle.core.command.cmds.NullCommand;
import net.battle.core.command.cmds.SetIngotCommand;
import net.battle.core.command.cmds.SetLevelCommand;
import net.battle.core.command.cmds.SetRankCommand;
import net.battle.core.command.cmds.SetTokenCommand;
import net.battle.core.command.cmds.SpawnEntityCommand;
import net.battle.core.command.cmds.SuspectSpectateCommand;
import net.battle.core.command.cmds.TeleportCommand;
import net.battle.core.command.cmds.TitleCommand;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.RankHandler;
import net.battle.core.handlers.ScoreboardHandler;
import net.battle.core.handlers.SwearSearchAlgorithm;
import net.battle.core.handlers.TimeHandler;
import net.battle.core.layouts.InventoryLayouts;
import net.battle.core.layouts.NavigatorInventoryLayout;
import net.battle.core.layouts.NavigatorInventoryListener;
import net.battle.core.listeners.BlockBreakListener;
import net.battle.core.listeners.ChatListener;
import net.battle.core.listeners.JoinLeaveListener;
import net.battle.core.listeners.PlayerSneakListener;
import net.battle.core.listeners.PotionHandler;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.proxy.ProxyMessageListener;
import net.battle.core.punish.cmds.BanCommand;
import net.battle.core.punish.cmds.FreezeCommand;
import net.battle.core.punish.cmds.KickCommand;
import net.battle.core.punish.cmds.ManageUserCommand;
import net.battle.core.punish.cmds.MuteCommand;
import net.battle.core.punish.cmds.OperatorCommand;
import net.battle.core.punish.cmds.ReportCommand;
import net.battle.core.punish.cmds.VanishCommand;
import net.battle.core.punish.cmds.WarnCommand;
import net.battle.core.punish.gui.ManageUserListener;
import net.battle.core.punish.gui.ReportInventory;
import net.battle.core.punish.listeners.PlayerPunishmentListener;
import net.battle.core.punish.listeners.PlayerFreezeListener;
import net.battle.core.settings.cmds.SettingsCommand;
import net.battle.core.settings.gui.SettingListener;
import net.battle.core.sql.ConnectionSQL;
import net.battle.core.supplydrop.cmds.SupplyDropCommand;
import net.battle.core.supplydrop.listeners.SupplyDropHandler;

public class BMCorePlugin extends JavaPlugin {

    public static final String PROXY_CHANNEL_MESSAGES = "battlemania:messages";
    public static BMCorePlugin ACTIVE_PLUGIN = null;

    public ConnectionSQL sqlConn = null;
    public Random random;
    // TODO: Move this to Bungee
    private YamlConfiguration settingsFile = null;

    private boolean allowBlockBreak;
    private boolean allowGadgets;

    public void onEnable() {
        BMLogger.info("Initializing BM Core...");
        ACTIVE_PLUGIN = this;
        random = new Random();

        BMLogger.info("Initializing config.yml file...");
        getConfig().options().copyDefaults(true);
        saveConfig();

        BMLogger.info("Initializing InventoryLayouts.json file and listeners...");
        try {
            InventoryLayouts.initializeLayoutsFile();
            registerAndLogListener(new NavigatorInventoryListener());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        BMLogger.info("Initializing MySQL database connection...");
        sqlConn = new ConnectionSQL();
        String address = getConfig().getString("sql.address");
        try {
            sqlConn.openConnection(address, getConfig().getString("sql.password"));
        } catch (ClassNotFoundException | SQLException e) {
            BMLogger.severe("Failed to connect to MySQL database at \"" + address + "\", throwing exception...");
            throw new RuntimeException(e);
        }

        BMLogger.info("Creating player online time handler...");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ACTIVE_PLUGIN, TimeHandler::updateTime, 20 * 60 * 6,
                20 * 60 * 6); // 20 ticks, 60 seconds, 6 minutes

        BMLogger.info("Initializing CommandHandler");
        Bukkit.getPluginManager().registerEvents(new CommandHandler(), this);

        BMLogger.info("Initializing utility commands...");
        CommandHandler.registerCommand(new GamemodeCommand());
        CommandHandler.registerCommand(new SetRankCommand());
        CommandHandler.registerCommand(new SetLevelCommand());
        CommandHandler.registerCommand(new SetIngotCommand());
        CommandHandler.registerCommand(new SetTokenCommand());
        CommandHandler.registerCommand(new GetUUIDCommand());
        CommandHandler.registerCommand(new TitleCommand());
        CommandHandler.registerCommand(new EntityCommand());
        CommandHandler.registerCommand(new HologramCommand());
        CommandHandler.registerCommand(new EvacuateCommand());
        CommandHandler.registerCommand(new SupplyDropCommand());
        CommandHandler.registerCommand(new CommandListCommand());
        CommandHandler.registerCommand(new GimmeCommand());
        CommandHandler.registerCommand(new SuspectSpectateCommand());
        CommandHandler.registerCommand(new EffectCommand());
        CommandHandler.registerCommand(new HeadCommand());
        CommandHandler.registerCommand(new TeleportCommand());
        CommandHandler.registerCommand(new SpawnEntityCommand());
        CommandHandler.registerCommand(new NullCommand());

        BMLogger.info("Initializing punishment commands...");
        CommandHandler.registerCommand(new BanCommand());
        CommandHandler.registerCommand(new ManageUserCommand());
        CommandHandler.registerCommand(new MuteCommand());
        CommandHandler.registerCommand(new ReportCommand());
        CommandHandler.registerCommand(new WarnCommand());
        CommandHandler.registerCommand(new SettingsCommand());
        CommandHandler.registerCommand(new OperatorCommand());
        CommandHandler.registerCommand(new KickCommand());
        CommandHandler.registerCommand(new VanishCommand());
        CommandHandler.registerCommand(new FreezeCommand());

        BMLogger.info("Initializing asset commands...");
        CommandHandler.registerCommand(new AssetOpenerCommand());
        CommandHandler.registerCommand(new HatInventoryCommand());
        CommandHandler.registerCommand(new HatCommand());
        CommandHandler.registerCommand(new ParticleCommand());
        CommandHandler.registerCommand(new ParticleInventoryCommand());
        CommandHandler.registerCommand(new ToggleGadgetCommand());
        CommandHandler.registerCommand(new GadgetBypassCommand());

        BMLogger.info("Initializing utility listeners...");
        registerAndLogListener(new JoinLeaveListener());
        registerAndLogListener(new BlockBreakListener());
        registerAndLogListener(new ChatListener());
        registerAndLogListener(new SupplyDropHandler());
        registerAndLogListener(new PlayerSneakListener());
        registerAndLogListener(new PotionHandler());

        BMLogger.info("Initializing punishment listeners...");
        registerAndLogListener(new ManageUserListener());
        registerAndLogListener(new ReportInventory());
        registerAndLogListener(new PlayerPunishmentListener());
        registerAndLogListener(new SettingListener());
        registerAndLogListener(new PlayerFreezeListener());

        BMLogger.info("Initializing asset command listeners...");
        registerAndLogListener(new AssetChestListener());
        registerAndLogListener(new AssetInventoryListener());
        registerAndLogListener(new HatInventoryListener());
        registerAndLogListener(new ParticleUtility());
        registerAndLogListener(new ParticleInventoryListener());
        registerAndLogListener(new GadgetInventoryListener());
        registerAndLogListener(new HatLeaveListener());

        BMLogger.info("Initializing assets...");
        // Constructors will register them under the "particles" list
        new ParticleHalo();
        new ParticleHex();
        new ParticleJail();
        for (int i = 0; i < 100; i++) {
            new ParticleJail("jail" + i);
        }
        AssetHandler.init();

        BMLogger.info("Opening connections with proxy...");
        getServer().getMessenger().registerIncomingPluginChannel(this, PROXY_CHANNEL_MESSAGES, new ProxyMessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, PROXY_CHANNEL_MESSAGES);

        if (getServer().getOnlinePlayers().size() > 0) {
            BMLogger.info("Querying proxy for command list...");
            ProxyHandler.queryForProxyCommands();
        }

        BMLogger.info("Initializing settings file...");
        try {
            settingsFile = SettingsFiles.initializeSettingsFile(getConfig().getString("settingsfile"));
        } catch (IOException ex) {
            BMLogger.severe("Failed to load settings file, cannot initialize!");
        }
        BMLogger.info("Testing Settings File: servertitle=" + settingsFile.getString("servertitle"));

        BMLogger.info("Initializing scoreboard, tab, and swear search algorithm...");
        ScoreboardHandler.setupScoreboards();
        ScoreboardHandler.updateScoreboards();
        HeaderFooterHandler.updateHeaderFooter();
        SwearSearchAlgorithm.init();

        // Additional info
        BMLogger.info("All ranks: " + RankHandler.getAllRanks());

        allowGadgets = true;
    }

    @Override
    public void onDisable() {
        BMLogger.info("Closing all navigator inventories...");
        NavigatorInventoryLayout.closeAllInventories();
    }

    public void registerAndLogListener(Listener l) {
        BMLogger.info("Registering listener " + l.getClass().getName() + "...");
        Bukkit.getPluginManager().registerEvents(l, this);
    }

    public void setAllowBlockBreak(boolean value) {
        this.allowBlockBreak = value;
    }

    public boolean getAllowBlockBreak() {
        return allowBlockBreak;
    }

    public boolean areGadgetsAllowed() {
        return allowGadgets;
    }

    public void setGadgetsAllowed(boolean allowed) {
        allowGadgets = allowed;
    }

    public synchronized String getSettingsString(String key) {
        return settingsFile.getString(key);
    }

    public synchronized boolean getSettingsBoolean(String key) {
        return settingsFile.getBoolean(key);
    }

    public synchronized boolean getSettingsContains(String key) {
        return getSettingsContains(key);
    }

    public synchronized List<String> getSettingsStringList(String key) {
        return settingsFile.getStringList(key);
    }

    public synchronized void setSettingsValue(String key, Object value) {
        settingsFile.set(key, value);
    }

    public void sendPluginMessage(byte[] data) {
        getServer().sendPluginMessage(this, PROXY_CHANNEL_MESSAGES, data);
    }

}
