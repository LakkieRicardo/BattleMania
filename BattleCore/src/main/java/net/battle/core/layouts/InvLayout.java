package net.battle.core.layouts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
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

    // This is the best way I've come up with to store metadata about a certain inventory using the Bukkit API
    private static final Map<Inventory, InvLayout> LAYOUT_MAP = new HashMap<>();
    private static final Map<Inventory, Object> INVENTORY_META_MAP = new HashMap<>();

    private static JSONArray root = null;

    protected final String layoutId;
    protected final String title;
    protected final String layout;

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

    /**
     * Creates a new inventory based off this layout. The viewer is only used as additional information to generate the
     * inventory, not to open the inventory.
     * 
     * @param viewer The player who is opening this inventory.
     * @param meta   The meta object - could be any type (ex: NavigatorInvMeta)
     * @return An inventory that is filled based on the layout and meta
     */
    public Inventory createInventory(Player viewer, Object meta) {
        Inventory inventory = Bukkit.createInventory(null, layout.length(), Component.text(title.replaceAll("&", "§")));
        updateInventoryMeta(inventory, viewer, meta);
        LAYOUT_MAP.put(inventory, this);
        return inventory;
    }

    protected ItemStack createItemFromJSON(JSONObject definition) {
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

    /**
     * This is the function that will be overriden by the specific layout type to handle whatever additional inventory
     * metadata and item definitions there may be.
     * 
     * @param inventory The inventory to update
     * @param viewer    The player who is viewing the inventory
     * @param meta      The updated metadata for this inventory
     */
    protected abstract void doUpdateInventoryMeta(Inventory inventory, Player viewer, Object meta);

    /**
     * Updates an inventory's metadata by overwriting all of its items.
     * 
     * @param inventory The inventory to update
     * @param viewer    The player who is viewing the inventory
     * @param meta      The updated metadata for this inventory
     */
    public void updateInventoryMeta(Inventory inventory, Player viewer, Object meta) {
        doUpdateInventoryMeta(inventory, viewer, meta);
        for (IInvLayoutEffect effect : effects) {
            effect.applyEffect(inventory, viewer, this, meta);
        }
        INVENTORY_META_MAP.put(inventory, meta);
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

    public static boolean IsInvLayoutGenerated(Inventory inv) {
        return LAYOUT_MAP.containsKey(inv);
    }

    public static InvLayout getLayoutFromInv(Inventory inv) {
        return LAYOUT_MAP.get(inv);
    }

    public static void closeAllInventories() {
        for (Inventory inv : LAYOUT_MAP.keySet()) {
            inv.close();
        }
        LAYOUT_MAP.clear();
        INVENTORY_META_MAP.clear();
    }

    public static boolean doesInvHaveMeta(Inventory inv) {
        return INVENTORY_META_MAP.containsKey(inv);
    }

    public static Object getMetaFromInv(Inventory inv) {
        return INVENTORY_META_MAP.get(inv);
    }

    public static void clearInventoryFromMaps(Inventory inv) {
        if (INVENTORY_META_MAP.containsKey(inv)) {
            INVENTORY_META_MAP.remove(inv);
        }
        if (LAYOUT_MAP.containsKey(inv)) {
            LAYOUT_MAP.remove(inv);
        }
    }

    /**
     * Moves an inventory from one layout to another. There are, however, some pre-conditions required and will throw an
     * exception if not met: <br/>
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
     */
    public static void transferInventoryLayout(Inventory inv, Player viewer, InvLayout newLayout) {
        Validate.isTrue(LAYOUT_MAP.containsKey(inv));
        InvLayout oldLayout = LAYOUT_MAP.get(inv);
        Validate.isTrue(oldLayout.getLayout().length() == newLayout.getLayout().length());
        Validate.isTrue(!oldLayout.getId().equals(newLayout.getId()));
        if (!oldLayout.getTitle().equals(newLayout.getTitle())) {
            BMLogger.warning("Attempting to transfer mismatching layout titles(old: \"" + oldLayout.getTitle() + "\", new: \"" + newLayout.getTitle()
                    + "\"). The transfer will succeed but the title cannot be updated.");
        }
        INVENTORY_META_MAP.remove(inv);
        LAYOUT_MAP.remove(inv);
        newLayout.updateInventoryMeta(inv, viewer, newLayout);
    }

}
