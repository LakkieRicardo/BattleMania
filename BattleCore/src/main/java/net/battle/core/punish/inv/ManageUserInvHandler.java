package net.battle.core.punish.inv;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.LayoutHolder;
import net.battle.core.layouts.navinv.INavigatorContentItem;
import net.battle.core.layouts.navinv.NavigatorInvData;
import net.battle.core.layouts.plinv.PlayerInvLayout;
import net.battle.core.punish.PunishManager;
import net.battle.core.sql.impl.PunishmentSql;
import net.battle.core.sql.records.PlayerPunishInfo;
import net.battle.core.sql.records.PunishType;
import net.kyori.adventure.text.Component;

public class ManageUserInvHandler {

    private final PlayerInvLayout invHome, invPunishTemp, invPunishPerm, invViewPunish;

    public ManageUserInvHandler() throws RuntimeException {
        invHome = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("manage_home").orElseThrow(() -> new RuntimeException("manage_home layout must be defined for PunishInvHandler!")));
        invPunishTemp = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("manage_punish_temp").orElseThrow(() -> new RuntimeException("manage_home layout must be defined for PunishInvHandler!")));
        invPunishPerm = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("manage_punish_perm").orElseThrow(() -> new RuntimeException("manage_home layout must be defined for PunishInvHandler!")));
        invViewPunish = new PlayerInvLayout(InvLayout.getLayoutJSONFromId("manage_view_punishments").orElseThrow(() -> new RuntimeException("manage_home layout must be defined for PunishInvHandler!")));

        invPunishTemp.getEffects().add(ManageUserInvHandler::applyPunishEffect);
        invPunishPerm.getEffects().add(ManageUserInvHandler::applyPunishEffect);
    }

    public PlayerInvLayout getHomeLayout() {
        return invHome;
    }

    public PlayerInvLayout getPermanentPunishLayout() {
        return invPunishPerm;
    }

    public PlayerInvLayout getTemporaryPunishLayout() {
        return invPunishTemp;
    }

    public PlayerInvLayout getViewPunishmentsLayout() {
        return invViewPunish;
    }

    public ItemStack getPunishItem(PlayerPunishInfo punish) {
        ItemStack item = switch (punish.type()) {
        case MUTE -> new ItemStack(Material.BARRIER);
        case BAN -> new ItemStack(Material.REDSTONE_BLOCK);
        case WARN -> new ItemStack(Material.PAPER);
        default -> new ItemStack(Material.EGG); // Should not be reached
        };
        ItemMeta meta = item.getItemMeta();
        if (punish.expiration() == null) {
            meta.displayName(Component.text("§aPermanent " + punish.type().name()));
        } else {
            meta.displayName(Component.text("§aTemporary " + punish.type().name()));
        }

        var lore = meta.lore();
        lore.add(Component.text("§cType: §7" + punish.type().name()));
        if (punish.expiration() == null) {
            lore.add(Component.text("§c" + punish.type().name() + "Duration: §7FOREVER"));
        } else if (!punish.isActive()) {
            lore.add(Component.text("§c" + punish.type().name() + "Duration: §7EXPIRED"));
        } else {
            lore.add(Component.text("§c" + punish.type().name() + "Expiration: §7" + PunishManager.PUNISHMENT_DATE_FORMAT.format(punish.expiration()) + " at "
                    + PunishManager.PUNISHMENT_TIME_FORMAT.format(punish.expiration())));
        }

        lore.add(Component.text("§7-"));
        lore.add(Component.text("§cPunisher: §7" + Bukkit.getOfflinePlayer(UUID.fromString(punish.punisherUUID())).getName()));
        lore.add(Component.text("§cPunish Reason: §7" + punish.reason()));
        lore.add(Component.text("§cActivity: §7" + (punish.isActive() ? "Active" : "Inactive")));
        if (punish.isActive()) {
            lore.add(Component.text("§7-"));
            lore.add(Component.text("§cClick me to de-activate punishment!"));
        }
        if (punish.isActive()) {
            item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        }
        meta.getPersistentDataContainer().set(NamespacedKey.fromString("punish-id", BMCorePlugin.ACTIVE_PLUGIN), PersistentDataType.INTEGER, punish.id());
        item.setItemMeta(meta);
        return item;
    }

    public List<INavigatorContentItem> createPunishmentList(OfflinePlayer target) {
        var punishments = PunishmentSql.getPlayerPunishmentHistory(target.getUniqueId().toString());
        var result = new ArrayList<INavigatorContentItem>(punishments.size());
        for (var punish : punishments) {
            // TODO: Reports go in a separate UI
            if (punish.type() != PunishType.REPORT) {
                result.add(() -> getPunishItem(punish));
            }
        }
        return result;
    }

    public Inventory getViewPunishmentsInventory(OfflinePlayer target) {
        NavigatorInvData data = new NavigatorInvData(0, createPunishmentList(target));
        var holder = new LayoutHolder(invViewPunish, data);
        return holder.getInventory();
    }

    /**
     * Adds lore which specifies the punishment duration, which could be a number of days or permanent.
     * 
     * @param inv The inventory to add the effect to
     */
    private static void applyPunishEffect(Inventory inv, Player viewer) {
        var holder = InvLayout.getLayoutHolder(inv);
        var layout = (PlayerInvLayout) holder.getLayout();
        var layoutStr = layout.getLayout();

        for (int i = 0; i < layoutStr.length(); i++) {
            var definition = layout.getItemDefines().get(layoutStr.charAt(i));
            if (!definition.containsKey("button_id")) {
                continue;
            }
            String buttonId = (String) definition.get("button_id");
            if (!buttonId.equals("Mute") && !buttonId.equals("Ban")) {
                continue;
            }
            int punishDays = (int) definition.get("punish_days");
            ItemStack item = inv.getItem(i);
            InventoryUtils.addItemLore(item, "§7-");
            if (punishDays == -1) {
                InventoryUtils.addItemLore(item, String.format("§c%s duration: §c§lPermanent", buttonId));
            } else {
                InventoryUtils.addItemLore(item, String.format("§c%s duration: §c§l%d %s", buttonId, punishDays, punishDays > 1 ? "days" : "day"));
            }
            inv.setItem(i, item);
        }
    }

}
