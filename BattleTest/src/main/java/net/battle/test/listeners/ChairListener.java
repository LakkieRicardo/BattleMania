package net.battle.test.listeners;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ChairListener implements Listener {
    private Map<String, ArmorStand> stands = new HashMap<>();
    private Map<String, Location> lastLocs = new HashMap<>();

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        if (pl.isSneaking()) {
            return;
        }
        Block target = e.getClickedBlock();
        if (target == null) {
            return;
        }
        BlockData blockData = target.getBlockData();
        if (!(blockData instanceof Stairs)) {
            return;
        }
        Stairs stairsData = (Stairs) blockData;
        if (stairsData.getHalf() != Half.BOTTOM || (stairsData.getFacing() != BlockFace.WEST && stairsData.getFacing() != BlockFace.EAST
                && stairsData.getFacing() != BlockFace.NORTH && stairsData.getFacing() != BlockFace.SOUTH)) {
            return;
        }

        if (!this.stands.containsKey(pl.getUniqueId().toString())) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }
            this.lastLocs.put(pl.getUniqueId().toString(), pl.getLocation());
            Location pLoc = pl.getLocation();
            pLoc.setYaw(pLoc.getYaw() + 180.0F);
            pl.teleport(pLoc);
            Location targetLoc = target.getLocation();
            targetLoc.setX(targetLoc.getX() + 0.5D);
            targetLoc.setY(targetLoc.getY() - 0.5D);
            targetLoc.setZ(targetLoc.getZ() + 0.5D);
            targetLoc.setYaw(pLoc.getYaw());
            ArmorStand stand = (ArmorStand) pl.getWorld().spawn(targetLoc, ArmorStand.class);
            targetLoc.setYaw(pLoc.getYaw());
            stand.setCollidable(false);
            stand.teleport(targetLoc);
            stand.setGravity(false);
            stand.setSmall(true);
            stand.setVisible(false);
            stand.addPassenger((Entity) pl);
            stand.teleport(targetLoc);
            this.stands.put(pl.getUniqueId().toString(), stand);
        }
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        Player pl = e.getPlayer();
        if (this.stands.containsKey(pl.getUniqueId().toString())) {
            ArmorStand stand = this.stands.get(pl.getUniqueId().toString());
            stand.remove();
            if (!stand.isDead()) {
                stand.remove();
            }
            if (!stand.isDead()) {
                stand.setHealth(0.0D);
            }
            this.stands.remove(pl.getUniqueId().toString());
            Location lastLoc = this.lastLocs.get(pl.getUniqueId().toString());
            lastLoc.setYaw(pl.getLocation().getYaw());
            lastLoc.setPitch(pl.getLocation().getPitch());
            pl.teleport(lastLoc);
            this.lastLocs.remove(pl.getUniqueId().toString());
        }
    }
}