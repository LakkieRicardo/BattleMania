package net.battle.core.layouts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import net.battle.core.SettingsFiles;
import net.battle.core.layouts.navinv.NavigatorInvLayout;
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

    public static Optional<NavigatorInvLayout> createNavigatorFromId(String particlesLayoutId) {
        if (root == null) {
            throw new IllegalStateException("Layout JSON must be initialized");
        }

        for (int i = 0; i < root.size(); i++) {
            JSONObject member = (JSONObject) root.get(i);
            if (((String) member.get("id")).equalsIgnoreCase(particlesLayoutId)) {
                return Optional.of(new NavigatorInvLayout(member));
            }
        }
        return Optional.empty();
    }

    public static void initializeLayoutsFile() throws IOException, ParseException {
        root = (JSONArray) SettingsFiles.initializeJSONResource("/InventoryLayouts.json");
    }

    public static boolean isInvFromLayout(Inventory inv) {
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

}
