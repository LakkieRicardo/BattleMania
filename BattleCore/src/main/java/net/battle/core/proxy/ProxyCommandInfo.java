package net.battle.core.proxy;

import net.battle.core.command.ICommandInfo;

public class ProxyCommandInfo implements ICommandInfo {

    private final String label, usage, description;
    private final String[] aliases;

    public ProxyCommandInfo(String label, String usage, String description, String[] aliases) {
        this.label = label;
        this.usage = usage;
        this.description = description;
        this.aliases = aliases;
    }

    public String getLabel() {
        return label;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

}
