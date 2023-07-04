package net.battle.core.layouts;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NavigatorClickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final NavigatorInventoryLayout layout;
    private final Player whoClicked;
    private final NavigatorClickType clickType;
    private final InventoryClickEvent clickEvent;
    private final INavigatorContentItem contentItem;
    private final int openPage;

    public NavigatorClickEvent(NavigatorInventoryLayout layout, Player whoClicked, NavigatorClickType clickType, InventoryClickEvent clickEvent,
            INavigatorContentItem contentItem, int openPage) {
        this.layout = layout;
        this.whoClicked = whoClicked;
        this.clickType = clickType;
        this.clickEvent = clickEvent;
        this.contentItem = contentItem;
        this.openPage = openPage;
    }

    public NavigatorInventoryLayout getLayout() {
        return layout;
    }

    public Player getWhoClicked() {
        return whoClicked;
    }

    public NavigatorClickType getClickType() {
        return clickType;
    }

    public InventoryClickEvent getClickEvent() {
        return clickEvent;
    }

    public INavigatorContentItem getContentItem() {
        return contentItem;
    }

    public int getOpenPage() {
        return openPage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
