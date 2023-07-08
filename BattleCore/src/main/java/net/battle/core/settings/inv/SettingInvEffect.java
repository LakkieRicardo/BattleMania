package net.battle.core.settings.inv;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import net.battle.core.handlers.InventoryUtils;
import net.battle.core.handlers.RankHandler;
import net.battle.core.layouts.IInvLayoutEffect;
import net.battle.core.layouts.InvLayout;
import net.battle.core.layouts.plinv.PlayerInvMeta;
import net.battle.core.sql.impl.PlayerInfoSql;
import net.battle.core.sql.impl.PlayerSettingsSql;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SettingInvEffect implements IInvLayoutEffect {

    SettingInvEffect() {
    }

    @Override
    public void applyEffect(Inventory inv, Player viewer, InvLayout layout, Object meta) {
        if (!(meta instanceof PlayerInvMeta plMeta)) {
            throw new IllegalArgumentException("Setting inventory must use PlayerInvMeta meta type!");
        }

        OfflinePlayer targetPlayer = plMeta.target();
        for (int i = 0; i < layout.getLayout().length(); i++) {
            char current = layout.getLayout().charAt(i);
            JSONObject definition = layout.getItemDefines().get(current);
            if (definition.containsKey("button_id")) {
                String buttonId = (String) definition.get("button_id");
                if (buttonId.equals("back")) {
                    continue;
                }
                ItemStack currentItem = inv.getItem(i);
                boolean currentSetting = PlayerSettingsSql.getSetting(targetPlayer.getUniqueId().toString(), buttonId);
                if ((buttonId.equals("public.showswears") && PlayerInfoSql.getPlayerInfo(targetPlayer).ingameHours() < 5.0F
                        && !RankHandler.ownerPermission(targetPlayer.getUniqueId().toString()))
                        || (buttonId.equals("hub.antivelocity") && !RankHandler.moderatorPermission(targetPlayer.getUniqueId().toString()))
                        || (buttonId.equals("hub.forcefield") && !RankHandler.operatorPermission(targetPlayer.getUniqueId().toString()))) {
                    // Mark this item as inaccessible
                    InventoryUtils.renameItem(currentItem, "§4" + SettingInvHandler.getSettingDisplayName(buttonId));
                    InventoryUtils.insertItemLore(currentItem, "§4§lINACCESSIBLE", 0);
                    currentItem.setType(Material.IRON_BARS);
                } else {
                    InventoryUtils.renameItem(currentItem, "§a" + SettingInvHandler.getSettingDisplayName(buttonId));
                }
                Component defaultComp;
                if (SettingInvHandler.getDefaultSettingValue(buttonId)) {
                    defaultComp = Component.text("enabled").color(TextColor.color(128, 213, 128));
                } else {
                    defaultComp = Component.text("disabled").color(TextColor.color(213, 128, 128));
                }
                InventoryUtils.addItemLore(currentItem, Component.text("Default: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(defaultComp));
                InventoryUtils.addItemLore(currentItem, currentSetting ? "§a§lENABLED" : "§c§lDISABLED");
                if (currentSetting) {
                    currentItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                }
                ItemMeta itemMeta = currentItem.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                currentItem.setItemMeta(itemMeta);
                inv.setItem(i, currentItem);
            }
        }

    }

}
