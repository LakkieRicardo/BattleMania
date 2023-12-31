package net.battle.core.layouts.navinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.battle.core.handlers.BMLogger;
import net.battle.core.layouts.LayoutDefinitionType;
import net.battle.core.layouts.LayoutHolder;

public class NavigatorInvListener implements Listener {

    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (!event.isLeftClick() || event.getSlot() < 0 || event.getSlot() >= inv.getSize()) {
            // Not a valid click
            return;
        }
        if (event.getClickedInventory() != event.getInventory() || !(inv.getHolder() instanceof LayoutHolder holder)
                || !(holder.getLayout() instanceof NavigatorInvLayout navLayout)) {
            return;
        }
        var data = (NavigatorInvData) holder.getData();
        int openPage = data.page();

        var definition = navLayout.getItemDefines().get(navLayout.getLayout().charAt(event.getSlot()));
        var clickedType = LayoutDefinitionType.valueOf((String) definition.get("type"));
        var contentList = NavigatorInvLayout.getContentList(inv);

        INavigatorContentItem contentItem = null;
        NavigatorClickType clickType = null;
        switch (clickedType) {
        case NAVIGATOR_CONTENT:
            int contentIdx = navLayout.getContentIndex(openPage, event.getSlot());
            if (contentIdx >= contentList.size()) {
                clickType = NavigatorClickType.INVALID_CLICK;
                break;
            }
            contentItem = contentList.get(contentIdx);
            if (contentItem == null) {
                clickType = NavigatorClickType.INVALID_CLICK;
                break;
            }
            clickType = NavigatorClickType.CONTENT_CLICK;
            break;
        case NAVIGATOR_NEXT:
            if (openPage < navLayout.getPageCount(inv, contentList) - 1) {
                clickType = NavigatorClickType.NEXT_CLICK;
            } else {
                clickType = NavigatorClickType.INVALID_CLICK;
            }
            break;
        case NAVIGATOR_PREVIOUS:
            if (openPage > 0) {
                clickType = NavigatorClickType.PREVIOUS_CLICK;
            } else {
                clickType = NavigatorClickType.INVALID_CLICK;
            }
            break;
        case PROP:
            clickType = NavigatorClickType.PROP_CLICK;
            break;
        default:
            BMLogger.warning("Found invalid item type in navigator inventory: " + clickedType.name());
            break;
        }

        Bukkit.getPluginManager().callEvent(new NavigatorClickEvent(navLayout, (Player) event.getWhoClicked(), clickType, event, contentItem, contentList, openPage));
    }

}
