package net.battle.core.layouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.battle.core.handlers.InventoryUtils;
import net.kyori.adventure.text.Component;

/**
 * Represents a single JSON layout entry in the InventoryLayouts.json file. This can be linked to multiple open inventories.
 */
public class NavigatorInventoryLayout {

    // This is the best way I've come up with to store metadata about a certain inventory using the Bukkit API
    private static final Map<Inventory, NavigatorInventoryLayout> LAYOUT_MAP = new HashMap<>();
    private static final Map<Inventory, Integer> PAGE_MAP = new HashMap<>();

    private final String layoutId;
    private final String title;
    private final String layout;
    private final Map<Character, JSONObject> definitions;

    private List<INavigatorContentItem> contentList = null;
    private boolean hasCountedContentSlots = false;
    private int contentSlotCount = 0;

    public NavigatorInventoryLayout(JSONObject layoutJSON) {
        this.layoutId = (String)layoutJSON.get("id");
        this.title = (String)layoutJSON.get("title");
        JSONArray layoutRows = (JSONArray)layoutJSON.get("layout_rows");
        StringBuilder layoutBuilder = new StringBuilder();
        for (int i = 0; i < layoutRows.size(); i++) {
            layoutBuilder.append((String)layoutRows.get(i));
        }
        this.layout = layoutBuilder.toString();

        if (layout.length() % 9 != 0) {
            throw new IllegalArgumentException("Layout size must be divisible by 9");
        }

        JSONObject definesObject = (JSONObject)layoutJSON.get("definitions");
        this.definitions = new HashMap<>();
        for (Object key : definesObject.keySet()) {
            JSONObject definition = (JSONObject)definesObject.get(key);
            char c = ((String)key).charAt(0);
            definitions.put(c, definition);
        }
    }

    public String getId() {
        return layoutId;
    }

    public void setContentList(List<INavigatorContentItem> content) {
        this.contentList = content;
    }

    public List<INavigatorContentItem> getContentList() {
        return contentList;
    }

    public Map<Character, JSONObject> getLayoutDefinitions() {
        return definitions;
    }

    public String getLayout() {
        return layout;
    }

    public int getSlotsOfContent() {
        if (hasCountedContentSlots) {
            return contentSlotCount;
        }
        for (char c : layout.toCharArray()) {
            JSONObject cDefinition = definitions.get(c);
            if (((String) cDefinition.get("type")).equals(LayoutDefinitionType.NAVIGATOR_CONTENT.name())) {
                contentSlotCount++;
            }
        }
        hasCountedContentSlots = true;
        return contentSlotCount;
    }

    public int getPageCount() {
        return (int)Math.ceil((float)contentList.size() / getSlotsOfContent());
    }

    private ItemStack createItemFromJSON(JSONObject definition) {
        ItemStack item = new ItemStack(Material.valueOf((String) definition.get("material")));
        if (definition.containsKey("name")) {
            InventoryUtils.renameItem(item, (String) definition.get("name"));
        }
        if (definition.containsKey("lore")) {
            JSONArray loreLines = (JSONArray) definition.get("lore");
            List<Component> newLore = new ArrayList<>();
            for (int loreIdx = 0; loreIdx < loreLines.size(); loreIdx++) {
                newLore.add(Component.text((String) loreLines.get(loreIdx)));
            }
            InventoryUtils.setItemLore(item, newLore);
        }
        
        return item;
    }

    public Inventory createPage(int page) {
        Inventory inventory = Bukkit.createInventory(null, layout.length(), Component.text(title));
        updatePage(inventory, page);
        return inventory;
    }

    /**
     * Updates the contents of the current inventory including the LAYOUT_MAP and PAGE_MAP. This means that the layout will register the newly updated page with the correct number.
     * @param inventory The inventory to update
     * @param page The new page to switch to
     */
    public void updatePage(Inventory inventory, int page) {
        if (page < 0 || page >= getPageCount()) {
            throw new IllegalArgumentException("Attempted to access page " + page + " which is not accessible");
        }
        int contentPlaced = 0;
        for (int i = 0; i < layout.length(); i++) {
            char idxChar = layout.charAt(i);
            JSONObject idxDefinition = definitions.get(idxChar);
            if (idxDefinition == null) {
                throw new RuntimeException("Found character that has no definition in the layout (" + idxChar + ")");
            }
            switch (LayoutDefinitionType.valueOf((String) idxDefinition.get("type"))) {
                case PROP:
                    inventory.setItem(i, createItemFromJSON(idxDefinition));
                    break;
                case NAVIGATOR_NEXT:
                    if (page < getPageCount() - 1) {
                        inventory.setItem(i, createItemFromJSON((JSONObject)idxDefinition.get("active")));
                    } else {
                        inventory.setItem(i, createItemFromJSON((JSONObject)idxDefinition.get("default")));
                    }
                    break;
                case NAVIGATOR_PREVIOUS:
                    if (page > 0) {
                        inventory.setItem(i, createItemFromJSON((JSONObject)idxDefinition.get("active")));
                    } else {
                        inventory.setItem(i, createItemFromJSON((JSONObject)idxDefinition.get("default")));
                    }
                    break;
                case NAVIGATOR_CONTENT:
                    int contentIdx = page * getSlotsOfContent() + contentPlaced++;
                    ItemStack content;
                    if (contentIdx >= contentList.size() || contentList.get(contentIdx) == null) {
                        content = createItemFromJSON((JSONObject)idxDefinition.get("default"));
                    } else {
                        content = contentList.get(contentIdx).getItem();
                    }
                    inventory.setItem(i, content);
                    break;
            }
        }

        LAYOUT_MAP.put(inventory, this);
        PAGE_MAP.put(inventory, page);
    }

    /**
     * Returns the theoretical index of the content clicked. This may return a number that is greater than the size of the content list
     * @param openPage The index of the page currently open
     * @param slot The inventory slot clicked
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
            LayoutDefinitionType type = LayoutDefinitionType.valueOf((String)definitions.get(layout.charAt(i)).get("type"));
            if (type == LayoutDefinitionType.NAVIGATOR_CONTENT) {
                contentIdx++;
            }
        }

        return contentIdx;
    }

    /**
     * Calculates the content slot a certain content item needs to go into.
     * @param openPage Currently open page
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
             if (((String)definitions.get(layout.charAt(invSlotCounter)).get("type")).equals("NAVIGATOR_CONTENT")) {
                if (contentSlotCounter++ == contentIdx) {
                    return invSlotCounter;
                }
             }
        }

        return -1;
    }

    public static boolean isInventoryNavigator(Inventory inv) {
        return LAYOUT_MAP.containsKey(inv);
    }

    public static NavigatorInventoryLayout getLayoutFromInv(Inventory inv) {
        return LAYOUT_MAP.get(inv);
    }

    public static void closeAllInventories() {
        for (Inventory inv : LAYOUT_MAP.keySet()) {
            inv.close();
        }
        LAYOUT_MAP.clear();
        PAGE_MAP.clear();
    }

    public static boolean doesInvHavePageNumber(Inventory inv) {
        return PAGE_MAP.containsKey(inv);
    }

    public static int getPageNumFromInv(Inventory inv) {
        return PAGE_MAP.get(inv);
    }

    public static void clearInventoryFromMaps(Inventory inv) {
        if (PAGE_MAP.containsKey(inv)) {
            PAGE_MAP.remove(inv);
        }
        if (LAYOUT_MAP.containsKey(inv)) {
            LAYOUT_MAP.remove(inv);
        }
    }

}
