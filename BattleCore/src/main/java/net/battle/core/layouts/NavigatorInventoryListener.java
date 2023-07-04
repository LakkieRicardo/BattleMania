package net.battle.core.layouts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

public class NavigatorInventoryListener implements Listener {
    
    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (!event.isLeftClick() || event.getSlot() < 0 || event.getSlot() >= inv.getSize()) {
            // Not a valid click
            return;
        }
        if (!NavigatorInventoryLayout.isInventoryNavigator(inv) || !NavigatorInventoryLayout.doesInvHavePageNumber(inv) || event.getClickedInventory() != event.getInventory()) {
            return;
        }
        NavigatorInventoryLayout layout = NavigatorInventoryLayout.getLayoutFromInv(inv);
        int openPage = NavigatorInventoryLayout.getPageNumFromInv(inv);
        JSONObject definition = layout.getLayoutDefinitions().get(layout.getLayout().charAt(event.getSlot()));
        LayoutDefinitionType clickedType = LayoutDefinitionType.valueOf((String)definition.get("type"));
        
        INavigatorContentItem contentItem = null;
        NavigatorClickType clickType = null;
        switch (clickedType) {
            case NAVIGATOR_CONTENT:
                int contentIdx = layout.getContentIndex(openPage, event.getSlot());
                if (contentIdx >= layout.getContentList().size()) {
                    clickType = NavigatorClickType.INVALID_CLICK;
                    break;
                }
                contentItem = layout.getContentList().get(contentIdx);
                if (contentItem == null) {
                    clickType = NavigatorClickType.INVALID_CLICK;
                    break;
                }
                clickType = NavigatorClickType.CONTENT_CLICK;
                break;
            case NAVIGATOR_NEXT:
                if (openPage < layout.getPageCount() - 1) {
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
        }

        Bukkit.getPluginManager().callEvent(new NavigatorClickEvent(layout, (Player)event.getWhoClicked(), clickType, event, contentItem, openPage));
    }

    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent event) {
        NavigatorInventoryLayout.clearInventoryFromMaps(event.getInventory());
    }

}
