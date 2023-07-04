package net.battle.core.command;

/**
 * Interface to get only metadata about a command
 * 
 * This is used by <code>CommandListCommand/code> to search through all commands.
 */
public interface ICommandInfo {

    /**
     * @return Usage without any additional formatting
     */
    String getUsage();

    String getLabel();

    String getDescription();

    String[] getAliases();

}
