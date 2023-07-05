package net.battle.core.assets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.InventoryUtils;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Gadget implements Listener {
    private static final List<Gadget> GADGETS = new ArrayList<>();
    public static final List<String> BYPASS = new ArrayList<>();

    private List<Player> users = new ArrayList<>();
    private Map<Player, Long> activeCooldown = new HashMap<>();

    protected boolean usingLeft = true, usingRight = true;

    public Gadget(ItemStack label, String name, UUID id, float cooldown) {
        setName(name);
        setId(id);
        this.cooldown = cooldown;
        this.label = label;
        BMCorePlugin.ACTIVE_PLUGIN.registerAndLogListener(this);
        GADGETS.add(this);
        GADGETS_INV.addItem(new ItemStack[] { label });
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand() == null) {
            return;
        }
        if (InventoryUtils.getItemName(e.getPlayer().getInventory().getItemInMainHand()) == null) {
            return;
        }
        if (getLabel() == null) {
            return;
        }
        if (getLabel().getItemMeta() == null) {
            return;
        }
        if (!getLabel().getItemMeta().hasDisplayName()) {
            return;
        }
        if (!InventoryUtils.isItemSimilarTo(e.getPlayer().getInventory().getItemInMainHand(), this.label)) {
            return;
        }
        Action a = e.getAction();
        if (!this.usingRight && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!this.usingLeft && (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        if (!BYPASS.contains(e.getPlayer().getName()) && this.activeCooldown.containsKey(e.getPlayer())) {
            long cooldown = (long) (getCooldown() * 1000.0F);
            long currentTime = System.currentTimeMillis();
            long usedTime = ((Long) this.activeCooldown.get(e.getPlayer())).longValue();
            if (usedTime + cooldown > currentTime) {

                float delta = (float) (usedTime + cooldown - currentTime);
                delta /= 1000.0F;
                e.getPlayer().sendMessage("§4§lERROR§8 > §cYou must wait " + delta + " seconds before using this again");

                return;
            }
        }
        this.activeCooldown.put(e.getPlayer(), Long.valueOf(System.currentTimeMillis()));
        e.getPlayer().sendMessage("§a§lGADGET§8 > §fYou used " + getName() + "§f gadget");
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            rightClickGadgetAction(e.getPlayer());
        } else {
            leftClickGadgetAction(e.getPlayer());
        }
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getLabel() {
        return this.label;
    }

    public void setLabel(ItemStack label) {
        this.label = label;
    }

    public boolean selectGadget(Player pl) {
        if (this.users.contains(pl)) {
            return false;
        }
        if (!InventoryUtils.isItemSimilarTo(pl.getInventory().getItem(1), NO_GADGET_ITEM)) {
            Gadget g = getByLabel(pl.getInventory().getItem(1));
            if (g != null) {
                g.unselectGadget(pl);
            }
        }
        this.users.add(pl);
        pl.getInventory().setItem(1, getLabel());
        pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5.0F, 0.5F);
        return true;
    }

    public boolean unselectGadget(Player pl) {
        if (!this.users.contains(pl)) {
            return false;
        }

        this.users.remove(pl);
        pl.getInventory().setItem(1, NO_GADGET_ITEM);
        pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5.0F, 0.5F);
        return true;
    }

    public List<Player> getUsers() {
        return this.users;
    }

    public float getCooldown() {
        return this.cooldown;
    }

    public static List<Gadget> getAllGadgets() {
        return GADGETS;
    }

    public static Gadget getByLabel(ItemStack label) {
        for (Gadget g : getAllGadgets()) {
            if (InventoryUtils.isItemSimilarTo(label, g.getLabel())) {
                return g;
            }
        }
        return null;
    }

    public static Gadget getByUUID(UUID id) {
        for (Gadget g : getAllGadgets()) {
            if (g.getId().toString().equalsIgnoreCase(id.toString())) {
                return g;
            }
        }
        return null;
    }

    public static Gadget getByName(String name) {
        for (Gadget g : getAllGadgets()) {
            if (g.getName().equalsIgnoreCase(name)) {
                return g;
            }
        }
        return null;
    }

    public static final Inventory GADGETS_INV = Bukkit.createInventory(null, 54, Component.text("§cGadgets §7Inventory"));
    static {
        ItemStack gadgetInv = new ItemStack(Material.CHEST);
        ItemMeta meta = gadgetInv.getItemMeta();
        meta.displayName(Component.text("§aGadgets"));
        gadgetInv.setItemMeta(meta);
        GADGET_ITEM = gadgetInv;
    }

    public static final ItemStack NO_GADGET_ITEM = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    static {
        NO_GADGET_ITEM.setAmount(1);
        ItemMeta meta2 = NO_GADGET_ITEM.getItemMeta();
        meta2.displayName(Component.text("§cNo Gadget Selected"));
        NO_GADGET_ITEM.setItemMeta(meta2);
    }

    public static final ItemStack UNSELECT_GADGET_ITEM = new ItemStack(Material.BARRIER);
    static {
        ItemMeta meta0 = UNSELECT_GADGET_ITEM.getItemMeta();
        meta0.displayName(Component.text("§cUnselect Gadget"));
        UNSELECT_GADGET_ITEM.setItemMeta(meta0);
        GADGETS_INV.addItem(new ItemStack[] { UNSELECT_GADGET_ITEM });
    }

    public static final ItemStack GADGET_ITEM;
    private ItemStack label;
    private String name;
    private UUID id;
    private final float cooldown;

    public abstract void leftClickGadgetAction(Player paramPlayer);

    public abstract void rightClickGadgetAction(Player paramPlayer);
}