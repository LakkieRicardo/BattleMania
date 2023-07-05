package net.battle.core.handlers.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMTextConvert;
import net.battle.core.handlers.InventoryUtils;
import net.kyori.adventure.text.Component;

public class ComponentInventory extends ComponentEvent {
    private final List<BMClickableButton> buttons;
    private final List<BMButton> dullButtons;
    private final String inventoryName;
    private final int inventorySize;

    public ComponentInventory(ComponentType type, Module parent, String invName, int invSize) {
        super(type, parent);
        this.buttons = new ArrayList<>();
        this.dullButtons = new ArrayList<>();
        this.inventoryName = invName;
        this.inventorySize = invSize;
    }

    public void addClickableButtons(BMClickableButton... buttons) {
        this.buttons.addAll(Arrays.asList(buttons));
    }

    public void addDullButtons(BMButton... buttons) {
        this.dullButtons.addAll(Arrays.asList(buttons));
    }

    public Inventory buildInventory() {
        Inventory inv = Bukkit.createInventory(null, this.inventorySize, Component.text(this.inventoryName));
        List<BMButton> buttons = new ArrayList<>();
        buttons.addAll(this.buttons);
        buttons.addAll(this.dullButtons);
        for (BMButton button : buttons) {
            inv.setItem(button.getPosition(), button.getItem());
        }
        return inv;
    }

    @EventHandler
    public void onInventory(InventoryEvent event) {
        if (event == null) {
            return;
        }
        if (event instanceof InventoryOpenEvent) {
            getParent().getRunnable().onInventoryOpen((InventoryOpenEvent) event);
        }
        if (event instanceof InventoryCloseEvent) {
            getParent().getRunnable().onInventoryClose((InventoryCloseEvent) event);
        }
        if (event instanceof InventoryClickEvent) {
            InventoryClickEvent cEvent = (InventoryClickEvent) event;

            if (cEvent.getInventory() == null) {
                return;
            }

            if (cEvent.getCurrentItem() == null) {
                return;
            }
            String invName = BMTextConvert.CTS.serialize(cEvent.getView().title());
            if (!invName.equalsIgnoreCase(this.inventoryName)) {
                return;
            }
            ((InventoryClickEvent) event).setCancelled(true);
            getParent().getRunnable().onSafeRawInventoryClick(cEvent);
            ItemStack item = cEvent.getCurrentItem();
            for (BMClickableButton button : this.buttons) {
                if (InventoryUtils.isItemSimilarTo(item, button.getItem()))
                    button.onClick((Player) cEvent.getWhoClicked());
            }
        }
    }
}