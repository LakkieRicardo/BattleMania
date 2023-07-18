package net.battle.core.assets.particle.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import net.battle.core.assets.particle.BMParticle;
import net.battle.core.assets.particle.gui.ParticleInventoryBuilder;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.navinv.NavigatorClickEvent;
import net.battle.core.layouts.navinv.NavigatorInvLayout;
import net.battle.core.layouts.navinv.NavigatorInvData;

public class ParticleInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(NavigatorClickEvent e) {
        NavigatorInvLayout navLayout = e.getLayout();
        if (!navLayout.getId().equals(ParticleInventoryBuilder.PARTICLES_LAYOUT_ID)) {
            return;
        }
        e.getClickEvent().setCancelled(true);

        Player pl = e.getWhoClicked();
        Inventory inv = e.getClickEvent().getClickedInventory();
        switch (e.getClickType()) {
        case CONTENT_CLICK:
            BMParticle particle = (BMParticle) e.getContentItem();
            particle.equip(pl);
            pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            pl.closeInventory();
            break;
        case PROP_CLICK:
            if (InventoryUtils.compareItemNames(e.getClickEvent().getCurrentItem(), "§aDisable", true)) {
                BMParticle.dequip(pl);
                pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5.0F, 0.5F);
                pl.closeInventory();
            }
            break;
        case NEXT_CLICK:
            pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.updateInventoryData(inv, new NavigatorInvData(e.getOpenPage() + 1, e.getContentList()));
            navLayout.updateInventory(inv, pl);
            break;
        case PREVIOUS_CLICK:
            pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 5.0F, 0.5F);
            InvLayout.updateInventoryData(inv, new NavigatorInvData(e.getOpenPage() - 1, e.getContentList()));
            navLayout.updateInventory(inv, pl);
            break;
        case INVALID_CLICK:
            pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5.0F, 0.5F);
            break;
        }
    }
}