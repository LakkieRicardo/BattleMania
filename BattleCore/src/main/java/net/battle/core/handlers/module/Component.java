package net.battle.core.handlers.module;

public abstract class Component {
    private final ComponentType type;
    private final Module parent;

    public Component(ComponentType type, Module parent) {
        this.type = type;
        this.parent = parent;
    }

    public ComponentType getType() {
        return this.type;
    }

    public Module getParent() {
        return this.parent;
    }

    public abstract void register();
}