package net.battle.core;

import java.util.Collection;

import net.kyori.adventure.text.Component;

public class ComponentHelper {
    
    private ComponentHelper() {}

    public static boolean CollectionContainsString(Collection<Component> coll, String str) {
        for (Component comp : coll) {
            if (BMMacro.CTS.serialize(comp).contains(str)) {
                return true;
            }
        }
        return false;
    }

}
