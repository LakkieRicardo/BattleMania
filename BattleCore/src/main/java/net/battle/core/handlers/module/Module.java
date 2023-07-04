package net.battle.core.handlers.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {
    private final String name;
    private final List<Component> components;

    public Module(String name, Component... components) {
        this.name = name;
        this.components = new ArrayList<>();
        this.components.addAll(Arrays.asList(components));
    }

    public abstract String getDisplayName();

    public abstract BMRunnable getRunnable();

    public void registerComponent(Component component) {
        this.components.add(component);
        component.register();
    }

    public String getName() {
        return this.name;
    }
}