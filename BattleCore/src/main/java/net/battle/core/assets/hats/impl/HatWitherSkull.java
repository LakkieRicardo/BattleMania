package net.battle.core.assets.hats.impl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.battle.core.assets.hats.Hat;
import net.battle.core.handlers.ItemStackBuilder;

public class HatWitherSkull
  extends Hat
{
  public HatWitherSkull() {
    super("wither");
  }
  
  protected ItemStack getItem() {
    return (new ItemStackBuilder(Material.WITHER_SKELETON_SKULL)).withName("§aWither").build();
  }
}