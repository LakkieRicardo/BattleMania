package net.battle.core.layouts.plinv;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerInvClickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerInvLayout layout;
    private final InventoryClickEvent clickEvent;
    private final OfflinePlayer targetPlayer;
    private final String buttonId;

    public PlayerInvClickEvent(PlayerInvLayout layout, InventoryClickEvent clickEvent, OfflinePlayer targetPlayer, String buttonId) {
        this.layout = layout;
        this.clickEvent = clickEvent;
        this.targetPlayer = targetPlayer;
        this.buttonId = buttonId;
    }

    public PlayerInvLayout getLayout() {
        return layout;
    }

    public InventoryClickEvent getClickEvent() {
        return clickEvent;
    }

    public OfflinePlayer getTargetPlayer() {
        return targetPlayer;
    }

    public String getButtonId() {
        return buttonId;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
