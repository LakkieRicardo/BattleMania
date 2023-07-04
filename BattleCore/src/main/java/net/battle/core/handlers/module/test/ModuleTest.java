package net.battle.core.handlers.module.test;

import net.battle.core.handlers.module.BMRunnable;
import net.battle.core.handlers.module.Module;

public class ModuleTest extends Module {
    public static ModuleTest instance;
    private ModuleTestRunnable runnable;

    public ModuleTest() {
        super("test", new net.battle.core.handlers.module.Component[0]);
        this.runnable = new ModuleTestRunnable();
        instance = this;
    }

    public String getDisplayName() {
        return "Test Module";
    }

    public BMRunnable getRunnable() {
        return this.runnable;
    }
}