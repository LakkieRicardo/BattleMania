package net.battle.core.assets.particle.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.particle.BMParticle;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.layouts.INavigatorContentItem;
import net.battle.core.layouts.InventoryLayouts;
import net.battle.core.layouts.NavigatorInventoryLayout;

public class ParticleInventoryBuilder {
    public static final String PARTICLES_LAYOUT_ID = "particles_manager";
    public static final String CURRENTLY_SELECTED_PARTICLE = "§7This particle is currently selected.";

    public static List<INavigatorContentItem> getParticlesAsContentItems() {
        List<INavigatorContentItem> result = new ArrayList<>();
        for (BMParticle particle : BMParticle.particles) {
            result.add(particle);
        }
        return result;
    }

    public static void applyEffects(Inventory inv, NavigatorInventoryLayout navLayout, int page, UUID viewer) {
        BMParticle selected = BMParticle.getPlayerParticle(viewer);
        if (selected != null) {
            int contentIdx = -1;
            for (int i = 0; i < navLayout.getContentList().size(); i++) {
                BMParticle particle = (BMParticle) navLayout.getContentList().get(i);
                if (particle.getName().equals(selected.getName())) {
                    contentIdx = i;
                    break;
                }
            }

            if (contentIdx >= page * navLayout.getSlotsOfContent() && contentIdx < (page + 1) * navLayout.getSlotsOfContent()) {
                int selectedIdx = navLayout.getContentSlotIndex(page, contentIdx);
                if (selectedIdx != -1) {
                    ItemStack item = inv.getItem(selectedIdx);
                    item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                    InventoryUtils.addItemLore(item, CURRENTLY_SELECTED_PARTICLE);
                    inv.setItem(selectedIdx, item);
                }
            }
        }
    }

    public static Inventory create(int page, UUID viewer) {
        NavigatorInventoryLayout navLayout = InventoryLayouts.createNavigatorFromId(PARTICLES_LAYOUT_ID);
        navLayout.setContentList(getParticlesAsContentItems());
        Inventory inv = navLayout.createPage(page);
        applyEffects(inv, navLayout, page, viewer);
        return inv;

    }
}