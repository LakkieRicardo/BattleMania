package net.battle.core.assets.particle.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import net.battle.core.assets.particle.BMParticle;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutHolder;
import net.battle.core.layouts.navinv.INavigatorContentItem;
import net.battle.core.layouts.navinv.NavigatorInvLayout;
import net.battle.core.layouts.navinv.NavigatorInvData;

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

    public static void applyEffects(Inventory inv, Player viewer, NavigatorInvLayout layout, NavigatorInvData meta) {
        BMParticle selected = BMParticle.getPlayerParticle(viewer.getUniqueId());
        int page = meta.page();
        if (selected != null) {
            int contentIdx = -1;
            for (int i = 0; i < layout.getContentList().size(); i++) {
                BMParticle particle = (BMParticle) layout.getContentList().get(i);
                if (particle.getName().equals(selected.getName())) {
                    contentIdx = i;
                    break;
                }
            }

            if (contentIdx >= page * layout.getSlotsOfContent() && contentIdx < (page + 1) * layout.getSlotsOfContent()) {
                int selectedIdx = layout.getContentSlotIndex(page, contentIdx);
                if (selectedIdx != -1) {
                    ItemStack item = inv.getItem(selectedIdx);
                    item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                    InventoryUtils.addItemLore(item, CURRENTLY_SELECTED_PARTICLE);
                    inv.setItem(selectedIdx, item);
                }
            }
        }
    }

    public static Inventory create(int page, Player viewer) {
        JSONObject layoutJSON = InvLayout.getLayoutJSONFromId(PARTICLES_LAYOUT_ID).orElseThrow(() -> new RuntimeException("Failed to find particle inventory ("
                + PARTICLES_LAYOUT_ID + ")"));
        NavigatorInvLayout navLayout = new NavigatorInvLayout(layoutJSON);
        navLayout.setContentList(getParticlesAsContentItems());
        navLayout.getEffects().add((inv, currentViewer) -> applyEffects(inv, currentViewer, navLayout, (NavigatorInvData) ((LayoutHolder) inv.getHolder()).getData()));
        return InvLayout.initializeInventory(navLayout, new NavigatorInvData(page), viewer).getInventory();
    }
}