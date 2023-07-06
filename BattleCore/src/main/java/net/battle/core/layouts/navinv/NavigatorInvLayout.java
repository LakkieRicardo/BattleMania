package net.battle.core.layouts.navinv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.battle.core.handlers.InventoryUtils;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutDefinitionType;
import net.kyori.adventure.text.Component;

/**
 * Represents a type of layout with a list of content items which allows for scrolling through pages.
 */
public class NavigatorInvLayout extends InvLayout {

    private final Map<Character, JSONObject> itemDefines;

    private List<INavigatorContentItem> contentList = null;
    private boolean hasCountedContentSlots = false;
    private int contentSlotCount = 0;

    public NavigatorInvLayout(JSONObject layoutJSON) {
        super(layoutJSON);
        JSONObject definesObject = (JSONObject) layoutJSON.get("definitions");
        this.itemDefines = new HashMap<>();
        for (Object key : definesObject.keySet()) {
            JSONObject definition = (JSONObject) definesObject.get(key);
            char c = ((String) key).charAt(0);
            itemDefines.put(c, definition);
        }
    }

    public void setContentList(List<INavigatorContentItem> content) {
        this.contentList = content;
    }

    public List<INavigatorContentItem> getContentList() {
        return contentList;
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

    public int getPageCount() {
        return (int) Math.ceil((float) contentList.size() / getSlotsOfContent());
    }

    private ItemStack createItemFromJSON(JSONObject definition) {
        ItemStack item = new ItemStack(Material.valueOf((String) definition.get("material")));
        if (definition.containsKey("name")) {
            InventoryUtils.renameItem(item, ((String) definition.get("name")).replaceAll("&", "§"));
        }
        if (definition.containsKey("lore")) {
            JSONArray loreLines = (JSONArray) definition.get("lore");
            List<Component> newLore = new ArrayList<>();
            for (int loreIdx = 0; loreIdx < loreLines.size(); loreIdx++) {
                newLore.add(Component.text(((String) loreLines.get(loreIdx)).replaceAll("&", "§")));
            }
            InventoryUtils.setItemLore(item, newLore);
        }

        return item;
    }

    public void doUpdateInventoryMeta(Inventory inventory, Player viewer, Object meta) {
        if (!(meta instanceof NavigatorInvMeta navMeta)) {
            throw new IllegalArgumentException("Navigator page meta must be NavigatorInvMeta!");
        }
        if (navMeta.page() < 0 || navMeta.page() >= getPageCount()) {
            throw new IllegalArgumentException("Attempted to access page " + navMeta.page() + " which is not accessible");
        }
        int contentPlaced = 0;
        for (int i = 0; i < layout.length(); i++) {
            char idxChar = layout.charAt(i);
            JSONObject idxDefinition = itemDefines.get(idxChar);
            if (idxDefinition == null) {
                throw new RuntimeException("Found character that has no definition in the layout (" + idxChar + ")");
            }
            switch (LayoutDefinitionType.valueOf((String) idxDefinition.get("type"))) {
            case PROP:
                inventory.setItem(i, createItemFromJSON(idxDefinition));
                break;
            case NAVIGATOR_NEXT:
                if (navMeta.page() < getPageCount() - 1) {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("active")));
                } else {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("default")));
                }
                break;
            case NAVIGATOR_PREVIOUS:
                if (navMeta.page() > 0) {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("active")));
                } else {
                    inventory.setItem(i, createItemFromJSON((JSONObject) idxDefinition.get("default")));
                }
                break;
            case NAVIGATOR_CONTENT:
                int contentIdx = navMeta.page() * getSlotsOfContent() + contentPlaced++;
                ItemStack content;
                if (contentIdx >= contentList.size() || contentList.get(contentIdx) == null) {
                    content = createItemFromJSON((JSONObject) idxDefinition.get("default"));
                } else {
                    content = contentList.get(contentIdx).getItem();
                }
                inventory.setItem(i, content);
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

}
