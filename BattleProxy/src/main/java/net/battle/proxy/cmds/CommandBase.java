package net.battle.proxy.cmds;

import com.velocitypowered.api.command.CommandManager;

import net.battle.proxy.BMProxyPlugin;

public abstract class CommandBase {
    
    protected final BMProxyPlugin plugin;

    public CommandBase(BMProxyPlugin plugin) {
        this.plugin = plugin;
    }

    // Information required to send to individual servers running /cmdlist
    public abstract String getLabel();
    
    public abstract String[] getAliases();

    public abstract String getUsage();

    public abstract String getDescription();

    public abstract void create(CommandManager commandManager);

}
