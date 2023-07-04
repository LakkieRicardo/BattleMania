package net.battle.core.handlers.module.test;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.ServerEvent;

import net.battle.core.handlers.module.BMButton;
import net.battle.core.handlers.module.BMRunnable;

public class ModuleTestRunnable implements BMRunnable {
  public void onEventFire(Event event) {
  }

  public void onPlayerEventFire(PlayerEvent event) {
  }

  public void onEntityEventFire(EntityEvent event) {
  }

  public void onInventoryEventFire(InventoryEvent event) {
  }

  public void onServerEventFire(ServerEvent event) {
  }

  public void onCommandExecute(String label, String[] args) {
  }

  public void onCommandPermissibleExecute(String label, String[] args, int code) {
  }

  public void onButtonClick(BMButton button) {
  }

  public void onInventoryOpen(InventoryOpenEvent event) {
  }

  public void onSafeRawInventoryClick(InventoryClickEvent inv) {
  }

  public void onInventoryClose(InventoryCloseEvent event) {
  }
}