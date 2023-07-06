package net.battle.core.layouts;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IInvLayoutEffect {
    
    void applyEffect(Inventory inv, Player viewer, InvLayout layout, Object meta);

}
