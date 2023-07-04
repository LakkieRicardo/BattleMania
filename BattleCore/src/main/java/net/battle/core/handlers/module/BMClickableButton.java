package net.battle.core.handlers.module;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.battle.core.handlers.ItemStackBuilder;

public abstract class BMClickableButton extends BMButton {
    public BMClickableButton(ItemStack item, ItemStackBuilder builder, boolean usingBuilder, String parent, int position) {
        super(item, builder, usingBuilder, parent, position);
    }

    public abstract void onClick(Player paramPlayer);

    public static BMClickableButton createSimpleClick(ItemStack item, String parentInv, int pos, final ClickListener click) {
        BMClickableButton instance = new BMClickableButton(item, null, false, parentInv, pos) {
            public void onClick(Player pl) {
                click.click(pl);
            }
        };
        return instance;
    }

    public static interface ClickListener {
        void click(Player param1Player);
    }
}