package net.battle.core.assets.gadget.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMCorePlugin;
import net.battle.core.BMTextConvert;
import net.battle.core.assets.gadget.Gadget;
import net.battle.core.handlers.InventoryUtils;

public class GadgetInventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e == null) {
            return;
        }
        if (e.getInventory() == null) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        Player pl = (Player) e.getWhoClicked();
        InventoryView inv = e.getView();
        if (BMTextConvert.CTS.serialize(inv.title()).equalsIgnoreCase("Gadgets Inventory")) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            Gadget g = Gadget.getByLabel(item);
            if (g == null) {
                if (InventoryUtils.isItemSimilarTo(item, Gadget.UNSELECT_GADGET_ITEM)) {
                    for (Gadget gc : Gadget.getAllGadgets()) {
                        if (gc.getUsers().contains(pl)) {
                            gc.unselectGadget(pl);
                            pl.sendMessage("§a§lGADGET§8 > §fYou deselected " + gc.getName() + " §fgadget");
                            pl.closeInventory();

                            return;
                        }
                    }
                }

                return;
            }
            g.selectGadget(pl);
            pl.sendMessage("§a§lGADGET§8 > §fYou enabled " + g.getName() + " §fgadget");
            pl.closeInventory();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player pl = e.getPlayer();

        if (InventoryUtils.isItemSimilarTo(Gadget.GADGET_ITEM, pl.getInventory().getItemInMainHand())) {
            if (!BMCorePlugin.ACTIVE_PLUGIN.areGadgetsAllowed()) {
                pl.sendMessage("§4§lERROR§8 > §cGadgets are disabled");
                return;
            }
            pl.sendMessage("§a§lGADGET§8 > §fOpened gadgets inventory");
            pl.openInventory(Gadget.GADGETS_INV);
        }
    }
}