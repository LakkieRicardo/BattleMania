package net.battle.core.layouts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import net.battle.core.SettingsFiles;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.InventoryUtils;
import net.kyori.adventure.text.Component;

/**
 * Represents any kind of layout which can be found in InventoryLayouts.json.
 */
public abstract class InvLayout {

    static final List<Inventory> LAYOUT_INVENTORIES = new ArrayList<>();

    private static JSONArray root = null;

    protected final String layoutId;
    protected final String title;
    protected final String layout;
    protected final Map<Character, JSONObject> itemDefines = new HashMap<>();

    protected final List<IInvLayoutEffect> effects = new ArrayList<>();

    public InvLayout(JSONObject layoutJSON) {
        this.layoutId = (String) layoutJSON.get("id");
        this.title = (String) layoutJSON.get("title");
        JSONArray layoutRows = (JSONArray) layoutJSON.get("layout_rows");
        StringBuilder layoutBuilder = new StringBuilder();
        for (int i = 0; i < layoutRows.size(); i++) {
            layoutBuilder.append((String) layoutRows.get(i));
        }
        this.layout = layoutBuilder.toString();

        if (layout.length() % 9 != 0) {
            throw new IllegalArgumentException("Layout size must be divisible by 9");
        }

        JSONObject definesObject = (JSONObject) layoutJSON.get("definitions");
        for (Object key : definesObject.keySet()) {
            JSONObject definition = (JSONObject) definesObject.get(key);
            char c = ((String) key).charAt(0);
            itemDefines.put(c, definition);
        }
    }

    public String getId() {
        return layoutId;
    }

    public String getLayout() {
        return layout;
    }

    public List<IInvLayoutEffect> getEffects() {
        return effects;
    }

    public String getTitle() {
        return title;
    }

    public Map<Character, JSONObject> getItemDefines() {
        return itemDefines;
    }

    protected ItemStack createItemFromJSON(JSONObject definition) {
        ItemStack item;
        if (definition.containsKey("material")) {
            item = new ItemStack(Material.valueOf((String) definition.get("material")));
        } else {
            item = new ItemStack(Material.STONE);
        }
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

    /**
     * This is the function that will be overriden by the specific layout type to handle whatever additional inventory
     * metadata and item definitions there may be.
     * 
     * @param inventory The inventory to update
     * @param viewer    The player who is viewing the inventory
     * @param holder    The layout information for this inventory
     */
    protected abstract void doUpdateInventory(Inventory inventory, Player viewer, LayoutHolder holder);

    /**
     * Updates inventory data using the current LayoutHolder's data object.
     * 
     * @param inventory The inventory whose contents to update
     * @param viewer    The player viewing this inventory
     */
    public void updateInventory(Inventory inventory, Player viewer) {
        if (!(inventory.getHolder() instanceof LayoutHolder holder)) {
            throw new IllegalArgumentException("Layout inventory holder must be of type LayoutHolder!");
        }
        if (inventory.getViewers().size() > 1) {
            throw new InventoryViewerStateException("More than 1 player is viewing a layout inventory! Each player must have their own layout inventory.");
        }
        doUpdateInventory(inventory, viewer, holder);
        for (var effect : effects) {
            effect.applyEffect(inventory, viewer);
        }
    }

    /**
     * Finds the proper JSON object to be fed into the InvLayout sub-class constructor.
     * 
     * @param particlesLayoutId The id of the layout in the JSON file
     * @return Optional for the JSON layout object
     */
    public static Optional<JSONObject> getLayoutJSONFromId(String particlesLayoutId) {
        if (root == null) {
            throw new IllegalStateException("Layout JSON must be initialized");
        }

        for (int i = 0; i < root.size(); i++) {
            JSONObject member = (JSONObject) root.get(i);
            if (((String) member.get("id")).equalsIgnoreCase(particlesLayoutId)) {
                return Optional.of(member);
            }
        }
        return Optional.empty();
    }

    public static void initializeLayoutsFile() throws IOException, ParseException {
        root = (JSONArray) SettingsFiles.initializeJSONResource("/InventoryLayouts.json");
    }

    public static boolean isInvLayoutGenerated(Inventory inv) {
        return inv.getHolder() instanceof LayoutHolder;
    }

    public static LayoutHolder getLayoutHolder(Inventory inv) {
        Validate.isTrue(isInvLayoutGenerated(inv));
        return (LayoutHolder) inv.getHolder();
    }

    public static InvLayout getLayoutFromInv(Inventory inv) {
        return getLayoutHolder(inv).getLayout();
    }

    public static void closeAllInventories() {
        for (Inventory inv : LAYOUT_INVENTORIES) {
            inv.close();
        }
        LAYOUT_INVENTORIES.clear();
    }

    public static void registerInventoryClosed(Inventory inv) {
        LAYOUT_INVENTORIES.remove(inv);
    }

    /**
     * Moves an inventory from one layout to another. The metadata object will be transferred directly. There are, however,
     * some pre-conditions required and will throw an exception if not met: <br/>
     * <ol>
     * <li>The inventory must be already registered into a layout</li>
     * <li>The new layout must be the same size as the old layout</li>
     * </ol>
     * <br/>
     * In addition to this, once the inventory is transferred the title of the inventory cannot be updated. A warning will
     * be printed if the titles are different.
     * 
     * @param inv       The inventory to transfer
     * @param viewer    The player who is viewing the inventory
     * @param newLayout The new layout to transfer to
     * @param newData   The new inventory data to update to
     */
    public static void transferInventoryLayout(Inventory inv, Player viewer, InvLayout newLayout, Object newData) {
        Validate.isTrue(LAYOUT_INVENTORIES.contains(inv));
        LayoutHolder holder = getLayoutHolder(inv);
        InvLayout oldLayout = holder.getLayout();
        Validate.isTrue(oldLayout.getLayout().length() == newLayout.getLayout().length());
        Validate.isTrue(!oldLayout.getId().equals(newLayout.getId()));
        if (!oldLayout.getTitle().equals(newLayout.getTitle())) {
            BMLogger.warning("Attempting to transfer mismatching layout titles(old: \"" + oldLayout.getTitle() + "\", new: \"" + newLayout.getTitle()
                    + "\"). The transfer will succeed but the title cannot be updated.");
        }
        holder.setLayout(newLayout);
        holder.setData(newData);
        newLayout.updateInventory(inv, viewer);
    }

    /**
     * Updates the data of an inventory. This will throw an IllegalArgumentException if the inventory does not have a holder
     * of type LayoutHolder. If there is an exception with the data type, it will be thrown when calling
     * {@link #updateInventory(Inventory, Player)}
     * 
     * @param inv  The inventory to update
     * @param data The new data to update to
     */
    public static void updateInventoryData(Inventory inv, Object data) {
        getLayoutHolder(inv).setData(data);
    }

    /**
     * Creates a holder and updates an inventory based n the data object.
     * 
     * @return The newly created LayoutHolder with prepared inventory
     */
    public static LayoutHolder initializeInventory(InvLayout layout, Object data, Player viewer) {
        LayoutHolder holder = new LayoutHolder(layout, data);
        layout.updateInventory(holder.getInventory(), viewer);
        return holder;
    }

}
