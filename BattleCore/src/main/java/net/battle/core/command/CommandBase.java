package net.battle.core.command;

import org.bukkit.entity.Player;

public interface CommandBase extends ICommandInfo {
    
    void onCommandExecute(Player executor, String[] args);
    
}