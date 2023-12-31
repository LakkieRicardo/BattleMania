package net.battle.core.layouts.navinv;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import net.battle.core.handlers.BMLogger;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutDefinitionType;
import net.battle.core.layouts.LayoutHolder;

/**
 * Represents a type of layout with a list of content items which allows for scrolling through pages. <br/>
 * <br/>
 * In order to use a navigator layout, first define it in the InventoryLayouts.json. There is no special layout type,
 * the only special item type for this layout is NAVIGATOR_CONTENT, NAVIGATOR_PREVIOUS, and NAVIGATOR_NEXT. <br/>
 * <br/>
 * Once done, use the {@link InvLayout#getLayoutJSONFromId(String)} function and pass it to this constructor in order to
 * interpret the JSON at runtime. Then, when you are at the point you need the inventory you can call the
 * {@link #setContentList(List)} and define the inventory's content items. That way, when you call the
 * {@link #updateInventory(Inventory, Object)} function it will dynamically generate the inventory consisting of all the
 * content items you need. <br/>
 * <br/>
 * Once you have the inventory open, you will want to listen to the NavigatorClickEvent in order to check when a player
 * is trying to select one of your content items, if they are trying to navigate, etc. and play sounds, do certain
 * actions, etc. depending on your needs. <br/>
 * <br/>
 * You can also write a function to implement the interface IInvLayoutEffect to add an additional effect onto your
 * inventory. This can be used to render things that may change player-to-player, and is done exclusively after the
 * inventory has already been created.
 */
public class NavigatorInvLayout extends InvLayout {

    private boolean hasCountedContentSlots = false;
    private int contentSlotCount = 0;

    public NavigatorInvLayout(JSONObject layoutJSON) {
        super(layoutJSON);
    }

    public Map<Character, JSONObject> getItemDefinitions() {
        return itemDefines;
    }

    public int getSlotsOfContent() {
        if (hasCountedContentSlots) {
            return contentSlotCount;
        }
        for (char c : layout.toCharArray()) {
            JSONObject cDefinition = itemDefines.get(c);
            if (((String) cDefinition.get("type")).equals(LayoutDefinitionType.NAVIGATOR_CONTENT.name())) {
                contentSlotCount++;
            }
        }
        hasCountedContentSlots = true;
        return contentSlotCount;
    }

    public int getPageCount(Inventory inv) {
        return (int) Math.ceil((float) getContentList(inv).size() / getSlotsOfContent());
    }

    public int getPageCount(Inventory inv, List<INavigatorContentItem> contentList) {
        return (int) Math.ceil((float) contentList.size() / getSlotsOfContent());
    }

    @Override
    protected void doUpdateInventory(Inventory inventory, Player viewer, LayoutHolder holder) {
        if (!(holder.getData() instanceof NavigatorInvData data)) {
            throw new IllegalArgumentException("NavigatorInvLayout data must be of type NavigatorInvData!");
        }
        var contentList = getContentList(inventory);
        if (data.page() < 0 || data.page() >= getPageCount(inventory, contentList)) {
            throw new IllegalArgumentException("Attempted to access page " + data.page() + " which is not accessible");
        }
        int contentPlaced = 0;
        for (int i = 0; i < layout.length(); i++) {
            char idxChar = layout.charAt(i);
            JSONObject idxDefinition = itemDefines.get(idxChar);
            if (idxDefinition == null) {
                throw new RuntimeException("Found character that has no definition in the layout (" + idxChar + ")");
            }
            var definitionType = LayoutDefinitionType.valueOf((String) idxDefinition.get("type"));
            switch (definitionType) {
            case PROP:
                inventory.setItem(i, createItemFromJSON(idxDefinition));
                break;
            case NAVIGATOR_NEXT:
                if (data.page() < getPageCount(inventory, contentList) - 1) {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("active")));
                } else {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("default")));
                }
                break;
            case NAVIGATOR_PREVIOUS:
                if (data.page() > 0) {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("active")));
                } else {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("default")));
                }
                break;
            case NAVIGATOR_CONTENT:
                int contentIdx = data.page() * getSlotsOfContent() + contentPlaced++;
                ItemStack content;
                if (contentIdx >= contentList.size() || contentList.get(contentIdx) == null) {
                    content = createItemFromJSON((JSONObject) idxDefinition.get("default"));
                } else {
                    content = contentList.get(contentIdx).getItem();
                }
                inventory.setItem(i, content);
                break;
            default:
                BMLogger.warning("Navigator inventory layout has invalid definition type: " + definitionType.name());
                break;
            }
        }
    }

    /**
     * Returns the theoretical index of the content clicked. This may return a number that is greater than the size of the
     * content list
     * 
     * @param openPage The index of the page currently open
     * @param slot     The inventory slot clicked
     * @return The index by which to access the content after checking if it is out of bounds
     */
    public int getContentIndex(int openPage, int slot) {
        if (slot > layout.length()) {
            throw new IllegalArgumentException("Clicked slot cannot be greater than the size of the inventory layout");
        }
        int contentIdx = openPage * getSlotsOfContent();
        // We need to loop up to but not including the clicked slot to prevent off-by-one error
        // By doing this we're assuming that the clicked slot is a content slot, which is a fair assumption to make
        for (int i = 0; i < slot; i++) {
            LayoutDefinitionType type = LayoutDefinitionType.valueOf((String) itemDefines.get(layout.charAt(i)).get("type"));
            if (type == LayoutDefinitionType.NAVIGATOR_CONTENT) {
                contentIdx++;
            }
        }

        return contentIdx;
    }

    /**
     * Calculates the content slot a certain content item needs to go into.
     * 
     * @param openPage   Currently open page
     * @param contentIdx Content of the index in the contentList
     * @return The inventory slot this content item is in. -1 if it is out of bounds
     */
    public int getContentSlotIndex(int openPage, int contentIdx) {
        contentIdx -= openPage * getSlotsOfContent();
        if (contentIdx < 0 || contentIdx >= getSlotsOfContent()) {
            return -1;
        }
        int contentSlotCounter = 0;
        for (int invSlotCounter = 0; invSlotCounter < layout.length(); invSlotCounter++) {
            if (((String) itemDefines.get(layout.charAt(invSlotCounter)).get("type")).equals("NAVIGATOR_CONTENT")) {
                if (contentSlotCounter++ == contentIdx) {
                    return invSlotCounter;
                }
            }
        }

        return -1;
    }

    public static List<INavigatorContentItem> getContentList(Inventory inv) {
        var holder = InvLayout.getLayoutHolder(inv);
        if (!(holder.getLayout() instanceof NavigatorInvLayout)) {
            throw new IllegalArgumentException("Content list can only be retrieved from a NavigatorInvLayout inventory!");
        }
        NavigatorInvData data = (NavigatorInvData) holder.getData();
        return data.contentList();
    }

}
