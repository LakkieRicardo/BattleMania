package net.battle.core.layouts.plinv;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

import net.battle.core.handlers.ItemStackBuilder;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutDefinitionType;

/**
 * This is an inventory layout which is generated with a specific target player. It uses the PlayerInvMeta as its meta
 * object, and will not work with any other meta type. The target player in the meta object can be used in conjunction
 * with the <code>TARGET_PLAYER_HEAD</code> item definition type to create a skull head with that target player. <br/>
 * <br/>
 * This layout can be used in conjunction with the PlayInvClickEvent in order to listen for button clicks. In addition,
 * in each item definition a <code>button_id</code> property can be specified which allows you to define a fixed value
 * to identify which button was pressed in the inventory while not relying on the name or location.
 */
public class PlayerInvLayout extends InvLayout {

    private final Map<Character, JSONObject> itemDefines = new HashMap<>();

    public PlayerInvLayout(JSONObject layoutJSON) {
        super(layoutJSON);
        JSONObject definesObject = (JSONObject) layoutJSON.get("definitions");
        for (Object key : definesObject.keySet()) {
            JSONObject definition = (JSONObject) definesObject.get(key);
            char c = ((String) key).charAt(0);
            itemDefines.put(c, definition);
        }
    }

    public Map<Character, JSONObject> getItemDefines() {
        return itemDefines;
    }

    @Override
    protected void doUpdateInventoryMeta(Inventory inventory, Player viewer, Object meta) {
        if (!(meta instanceof PlayerInvMeta plMeta)) {
            throw new IllegalArgumentException("Navigator page meta must be PlayerInvMeta!");
        }
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
            case TARGET_PLAYER_HEAD:
                inventory.setItem(i, new ItemStackBuilder(Material.PLAYER_HEAD).withSkullOwner(plMeta.target()).build());
                break;
            default:
                break;
            }
        }
    }

}
