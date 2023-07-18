package net.battle.core.layouts.navinv;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class NavigatorClickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final NavigatorInvLayout layout;
    private final Player whoClicked;
    private final NavigatorClickType clickType;
    private final InventoryClickEvent clickEvent;
    private final INavigatorContentItem contentItem;
    private final List<INavigatorContentItem> contentList;
    private final int openPage;

    public NavigatorClickEvent(NavigatorInvLayout layout, Player whoClicked, NavigatorClickType clickType, InventoryClickEvent clickEvent,
            INavigatorContentItem contentItem, List<INavigatorContentItem> contentList, int openPage) {
        this.layout = layout;
        this.whoClicked = whoClicked;
        this.clickType = clickType;
        this.clickEvent = clickEvent;
        this.contentItem = contentItem;
        this.contentList = contentList;
        this.openPage = openPage;
    }

    public NavigatorInvLayout getLayout() {
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

    public List<INavigatorContentItem> getContentList() {
        return contentList;
    }

    public int getOpenPage() {
        return openPage;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
