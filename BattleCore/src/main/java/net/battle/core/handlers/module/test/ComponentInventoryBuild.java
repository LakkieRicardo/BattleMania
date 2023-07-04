package net.battle.core.handlers.module.test;

import net.battle.core.handlers.module.ComponentInventory;
import net.battle.core.handlers.module.ComponentType;
import net.battle.core.handlers.module.Module;

public class ComponentInventoryBuild extends ComponentInventory {
    public ComponentInventoryBuild(Module parent) {
        super(ComponentType.INVENTORY, parent, "build", 27);
    }
}