package net.battle.core.layouts.plinv;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

import net.battle.core.layouts.InvLayout;

public class PlayerInvListener implements Listener {

    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (!event.isLeftClick() || event.getSlot() < 0 || event.getSlot() >= inv.getSize()) {
            return;
        }
        if (!InvLayout.IsInvLayoutGenerated(inv) || !(InvLayout.getLayoutFromInv(inv) instanceof PlayerInvLayout plLayout) || !InvLayout.doesInvHaveMeta(inv)
                || event.getClickedInventory() != inv) {
            return;
        }

        PlayerInvMeta plMeta = (PlayerInvMeta) InvLayout.getMetaFromInv(inv);
        JSONObject clickedJsonObject = plLayout.getItemDefines().get(plLayout.getLayout().charAt(event.getSlot()));
        String buttonId = clickedJsonObject.containsKey("button_id") ? (String) clickedJsonObject.get("button_id") : null;
        Bukkit.getPluginManager().callEvent(new PlayerInvClickEvent(plLayout, event, plMeta.target(), buttonId));
    }

}
