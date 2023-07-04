package net.battle.core.handlers.module;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.ServerEvent;

public interface BMRunnable {
  void onEventFire(Event paramEvent);

  void onPlayerEventFire(PlayerEvent paramPlayerEvent);

  void onEntityEventFire(EntityEvent paramEntityEvent);

  void onInventoryEventFire(InventoryEvent paramInventoryEvent);

  void onServerEventFire(ServerEvent paramServerEvent);

  void onCommandExecute(String paramString, String[] paramArrayOfString);

  void onCommandPermissibleExecute(String paramString, String[] paramArrayOfString, int paramInt);

  void onButtonClick(BMButton paramBMButton);

  void onInventoryOpen(InventoryOpenEvent paramInventoryOpenEvent);

  void onSafeRawInventoryClick(InventoryClickEvent paramInventoryClickEvent);

  void onInventoryClose(InventoryCloseEvent paramInventoryCloseEvent);
}