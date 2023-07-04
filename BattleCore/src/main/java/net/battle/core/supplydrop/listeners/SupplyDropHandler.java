package net.battle.core.supplydrop.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.battle.core.BMMacro;
import net.battle.core.handlers.BMLogger;
import net.battle.core.handlers.InventoryUtils;
import net.battle.core.supplydrop.SupplyDropType;
import net.kyori.adventure.text.Component;

public class SupplyDropHandler
    implements Listener {
  public static Inventory inventory;

  @EventHandler
  public void onPlayerInteractBlock(PlayerInteractEvent e) {
    Player pl = e.getPlayer();
    Action a = e.getAction();
    if (!pl.getGameMode().equals(GameMode.SURVIVAL)) {
      return;
    }
    if (a == Action.RIGHT_CLICK_BLOCK || a == Action.LEFT_CLICK_BLOCK) {
      Block b = pl.getTargetBlock(null, 5);
      if (b.getType() == Material.CHEST) {

        Location newLocation = b.getLocation();
        newLocation.setY(newLocation.getY() + 100.0D);
        ArmorStand stand = (ArmorStand) b.getWorld().spawnEntity(newLocation, EntityType.ARMOR_STAND);
        stand.setGravity(false);
        stand.setVisible(false);

        boolean foo = false;
        for (Entity en : stand.getNearbyEntities(0.5D, 110.0D, 0.5D)) {
          if (en.isCustomNameVisible() &&
              BMMacro.LCTS.serialize(en.customName()).equalsIgnoreCase("§7Supply Drops")) {
            foo = true;
          }
        }

        stand.teleport(stand.getLocation().add(0.0D, 100.0D, 0.0D));
        stand.remove();

        if (!foo) {
          return;
        }

        if (foo) {
          updateInventory();

          e.setCancelled(true);
          BMLogger.info(pl.getName() + " has opened the supply drop chest.");
          pl.openInventory(inventory);
        }
      }
    }
  }

  @EventHandler
  public void onPlayerClickSupplyDrop(InventoryClickEvent e) {
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
    ItemStack clicked = e.getCurrentItem();
    Material m = clicked.getType();

    if (BMMacro.CTS.serialize(inv.title()).equalsIgnoreCase("Supply Drops")) {
      e.setCancelled(true);
      if (m == Material.IRON_INGOT && !SupplyDropType.NORMAL.getSupplyDrop().play(pl))
        pl.sendMessage("§4§lERROR§8 > §cThis supply drop is currently occupied");
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
  }

  public static void updateInventory() {
    inventory = Bukkit.createInventory(null, 27, Component.text("Supply drops"));
    ItemStack normal = new ItemStack(Material.IRON_INGOT);
    normal.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    InventoryUtils.renameItem(normal, "§aNormal Supply Drop");

    InventoryUtils.setItem(inventory, 2, 1, normal);
    ItemStack rare = new ItemStack(Material.GOLD_INGOT);
    rare.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    InventoryUtils.renameItem(rare, "§aRare Supply Drop");

    InventoryUtils.setItem(inventory, 4, 1, rare);
    ItemStack legendary = new ItemStack(Material.DIAMOND);
    legendary.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    InventoryUtils.renameItem(legendary, "§aLegendary Supply Drop");

    InventoryUtils.setItem(inventory, 6, 1, legendary);

    InventoryUtils.fillBlanks(inventory);
  }

  static {
    updateInventory();
  }
}