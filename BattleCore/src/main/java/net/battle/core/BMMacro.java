package net.battle.core;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class BMMacro {
    
    private BMMacro() { }

    /**
     * CTS stands for "Component to String"
     */
    public static final PlainTextComponentSerializer CTS = PlainTextComponentSerializer.plainText();
    
    /**
     * LCTS stands for "Legacy Component to String"
     */
    public static final LegacyComponentSerializer LCTS = LegacyComponentSerializer.legacy('§');
    
}
