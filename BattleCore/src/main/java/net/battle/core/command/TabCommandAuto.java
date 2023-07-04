package net.battle.core.command;

public interface TabCommandAuto extends CommandTab {
    String getLabel();

    default String getCommandLabel() {
        return CommandHandler.getCommand(getLabel()).getLabel();
    }

    default String[] getCommandAliases() {
        return CommandHandler.getCommand(getLabel()).getAliases();
    }
}