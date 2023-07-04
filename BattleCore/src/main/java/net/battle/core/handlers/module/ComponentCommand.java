package net.battle.core.handlers.module;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;

public abstract class ComponentCommand extends Component implements CommandBase {
    public ComponentCommand(ComponentType type, Module parent) {
        super(type, parent);
    }

    public void register() {
        CommandHandler.registerCommand(this);
    }
}