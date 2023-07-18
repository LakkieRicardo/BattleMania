package net.battle.core.layouts.plinv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.RankHandler;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutDefinitionType;
import net.battle.core.layouts.LayoutHolder;
import net.battle.core.proxy.ProxyHandler;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.records.PlayerInfo;
import net.kyori.adventure.text.Component;

/**
 * This is an inventory layout which is generated with a specific target player. It uses the PlayerInvData as its data
 * object, and will not work with any other data type. The target player in the data object can be used in conjunction
 * with a <code>PLAYER_HEAD</code> definition type and <code>$Target</code> as the <code>owner</code> attribute to
 * create a skull head with that target player. <br/>
 * <br/>
 * This layout can be used in conjunction with the <code>PlayInvClickEvent</code> in order to listen for button clicks.
 * In addition, in each item definition a <code>button_id</code> property can be specified which allows you to define a
 * fixed value to identify which button was pressed in the inventory while not relying on the name or location.
 */
public class PlayerInvLayout extends InvLayout {

    public PlayerInvLayout(JSONObject layoutJSON) {
        super(layoutJSON);
    }

    /**
     * Adds <code>UUID</code>, <code>Username</code>, <code>Online.YesNo</code>, <code>Online.TrueFalse</code>,
     * <code>Ingots</code>, <code>Tokens</code>, <code>Rank.DisplayName</code>, <code>Rank.SQLName</code>,
     * <code>Server</code> to the mappings. The format it follows is: <br/>
     * <br/>
     * <code>&lt;PlayerKey.Username&gt;</code>
     * 
     * @param mappings  Mappings table to insert into
     * @param playerKey The key by which to access this player's properties
     * @param player    The player from which to get their info
     */
    private void addPlayerMappings(Map<String, String> mappings, String playerKey, OfflinePlayer player) {
        PlayerInfo playerInfo = PlayerInfoSql.getPlayerInfo(player);
        mappings.put("<%s.UUID>", player.getUniqueId().toString());
        mappings.put(String.format("<%s.Username>", playerKey), player.getName());
        mappings.put(String.format("<%s.Online.YesNo>", playerKey), ProxyHandler.isPlayerOnline(player.getName()) ? "Yes" : "No");
        mappings.put(String.format("<%s.Online.TrueFalse>", playerKey), Boolean.toString(ProxyHandler.isPlayerOnline(player.getName())));
        mappings.put(String.format("<%s.Ingots>", playerKey), Integer.toString(playerInfo.ingot()));
        mappings.put(String.format("<%s.Tokens>", playerKey), Integer.toString(playerInfo.token()));
        mappings.put(String.format("<%s.Rank.DisplayName>", playerKey), RankHandler.getRankFromSQLName(playerInfo.sqlRank()).getGameName());
        mappings.put(String.format("<%s.Rank.SQLName>", playerKey), playerInfo.sqlRank());
        mappings.put(String.format("<%s.Server>", playerKey), ProxyHandler.getPlayerServer(player.getName()));
    }

    public String replaceKeywords(String text, Player viewer, PlayerInvData data) {
        var mappings = new HashMap<String, String>();
        addPlayerMappings(mappings, "Target", data.target());
        addPlayerMappings(mappings, "Viewer", viewer);
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
            case PLAYER_HEAD:
                if (!idxDefinition.containsKey("owner")) {
                    throw new RuntimeException("Each PLAYER_HEAD needs an \"owner\" property.");
                }
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
                String owningPlayerName = (String) idxDefinition.get("owner");
                OfflinePlayer owningPlayer;
                if (owningPlayerName.equals("$Target")) {
                    owningPlayer = data.target();
                } else if (owningPlayerName.equals("$Viewer")) {
                    owningPlayer = viewer;
                } else {
                    owningPlayer = Bukkit.getOfflinePlayer(owningPlayerName);
                }
                skull.setOwningPlayer(owningPlayer);
                item.setItemMeta(skull);
                inventory.setItem(i, item);
                break;
            default:
                break;
            }
        }
    }

}
