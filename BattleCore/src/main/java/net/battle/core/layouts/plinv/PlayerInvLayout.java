package net.battle.core.layouts.plinv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.battle.core.handlers.InventoryUtils;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutDefinitionType;
import net.battle.core.layouts.LayoutHolder;
import net.kyori.adventure.text.Component;

/**
 * This is an inventory layout which is generated with a specific target player. It uses the PlayerInvData as its data
 * object, and will not work with any other data type. The target player in the data object can be used in conjunction
 * with the <code>TARGET_PLAYER_HEAD</code> item definition type to create a skull head with that target player. <br/>
 * <br/>
 * This layout can be used in conjunction with the PlayInvClickEvent in order to listen for button clicks. In addition,
 * in each item definition a <code>button_id</code> property can be specified which allows you to define a fixed value
 * to identify which button was pressed in the inventory while not relying on the name or location.
 */
public class PlayerInvLayout extends InvLayout {

    public PlayerInvLayout(JSONObject layoutJSON) {
        super(layoutJSON);
    }

    public String replaceKeywords(String text, Player viewer, PlayerInvData data) {
        var mappings = new HashMap<String, String>();
        mappings.put("<Target.Username>", data.target().getName());
        mappings.put("<Target.UUID>", data.target().getUniqueId().toString());
        mappings.put("<Viewer.Username>", viewer.getName());
        mappings.put("<Viewer.UUID>", viewer.getUniqueId().toString());
        for (String keyword : mappings.keySet()) {
            text = text.replaceAll(keyword, mappings.get(keyword));
        }
        return text;
    }

    @Override
    protected void doUpdateInventory(Inventory inventory, Player viewer, LayoutHolder holder) {
        if (!(holder.getData() instanceof PlayerInvData data)) {
            throw new IllegalArgumentException("The data of a PlayerInvLayout inventory must be PlayerInvData!");
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
                ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                if (idxDefinition.containsKey("name")) {
                    String newName = replaceKeywords(((String) idxDefinition.get("name")).replaceAll("&", "§"), viewer, data);
                    InventoryUtils.renameItem(item, newName);
                }
                if (idxDefinition.containsKey("lore")) {
                    JSONArray loreLines = (JSONArray) idxDefinition.get("lore");
                    List<Component> newLore = new ArrayList<>();
                    for (int loreIdx = 0; loreIdx < loreLines.size(); loreIdx++) {
                        newLore.add(Component.text(replaceKeywords(((String) loreLines.get(loreIdx)).replaceAll("&", "§"), viewer, data)));
                    }
                    InventoryUtils.setItemLore(item, newLore);
                }
                SkullMeta skull = (SkullMeta) item.getItemMeta();
                skull.setOwningPlayer(data.target());
                item.setItemMeta(skull);
                inventory.setItem(i, item);
                break;
            default:
                break;
            }
        }
    }

}
