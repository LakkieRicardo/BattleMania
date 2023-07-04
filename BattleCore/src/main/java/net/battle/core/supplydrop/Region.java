package net.battle.core.supplydrop;

import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Region {
    private Location lesser;
    private Location greater;

    public Region(Location loc1, Location loc2) {
        calculateLesserAndGreater(loc1, loc2);
    }

    public void calculateLesserAndGreater(Location loc1, Location loc2) {
        this.lesser = findLesserLocation(loc1, loc2);
        this.greater = findGreaterLocation(loc1, loc2);
    }

    public Location getLesser() {
        return this.lesser;
    }

    public Location getGreater() {
        return this.greater;
    }

    public void setLesser(Location lesser) {
        this.lesser = lesser;
    }

    public void setGreater(Location greater) {
        this.greater = greater;
    }

    public boolean isInside(Player pl) {
        return isInside(pl.getLocation());
    }

    public boolean isInside(Location l) {
        return (l.getX() >= this.lesser.getX() && l.getX() <= this.greater.getX() && l.getY() >= this.lesser.getY() && l.getY() <= this.greater.getY()
                && l.getZ() >= this.lesser.getZ() && l.getX() <= this.greater.getZ() && l.getWorld() == this.lesser.getWorld());
    }

    public double getXSize() {
        return getGreater().getX() - getLesser().getX();
    }

    public double getZSize() {
        return getGreater().getZ() - getLesser().getZ();
    }

    public double getYSize() {
        return getGreater().getY() - getLesser().getY();
    }

    public double getRandomX(List<Region> blacklisted) {
        Random rand = new Random();
        double current = getLesser().getX() + rand.nextInt((int) getGreater().getX());

        for (Region r : blacklisted) {
            if (r.getLesser().getX() > current || r.getGreater().getX() < current) {
                current = getLesser().getX() + rand.nextInt((int) getGreater().getX());
            }
        }

        return current;
    }

    public double getRandomY(List<Region> blacklisted) {
        Random rand = new Random();
        double current = getLesser().getY() + rand.nextInt((int) getGreater().getY());

        for (Region r : blacklisted) {
            if (r.getLesser().getY() > current || r.getGreater().getY() < current) {
                current = getLesser().getY() + rand.nextInt((int) getGreater().getY());
            }
        }

        return current;
    }

    public double getRandomZ(List<Region> blacklisted) {
        Random rand = new Random();
        double current = getLesser().getZ() + rand.nextInt((int) getGreater().getZ());

        for (Region r : blacklisted) {
            if (r.getLesser().getZ() > current || r.getGreater().getZ() < current) {
                current = getLesser().getZ() + rand.nextInt((int) getGreater().getZ());
            }
        }

        return current;
    }

    public BlockPosition[] getPositions() {
        BlockPosition[] array = new BlockPosition[(int) getXSize() * (int) getYSize() * (int) getZSize()];
        for (int x = 0; x < getXSize(); x++) {
            for (int z = 0; z < getZSize(); z++) {
                for (int y = 0; y < getYSize(); y++) {
                    array[x * y * z] = new BlockPosition(x, y, z, getLesser().getWorld());
                }
            }
        }
        return array;
    }

    public String toString() {
        return "[min=" + (new BlockPosition(getLesser().getBlockX(), getLesser().getBlockY(), getLesser().getBlockZ(), getLesser().getWorld())).toString()
                + ",max="
                + (new BlockPosition(getGreater().getBlockX(), getGreater().getBlockY(), getGreater().getBlockZ(), getGreater().getWorld())).toString() + "]";
    }

    public static double getHighestY(Location loc) {
        Location newLoc = loc;
        for (int y = 0; y < 256; y++) {
            int yy = y + 1;
            newLoc.setY(yy);
            if (newLoc.getBlock().getType() == Material.AIR) {
                return yy;
            }
        }
        return -1.0D;
    }

    public static Location findLesserLocation(Location loc1, Location loc2) {
        Location newLoc = new Location(loc1.getWorld(), 0.0D, 0.0D, 0.0D);
        if (loc1.getX() > loc2.getX()) {
            newLoc.setX(loc2.getX());
        } else {
            newLoc.setX(loc1.getX());
        }
        if (loc1.getY() > loc2.getY()) {
            newLoc.setY(loc2.getY());
        } else {
            newLoc.setY(loc1.getY());
        }
        if (loc1.getZ() > loc2.getZ()) {
            newLoc.setZ(loc2.getZ());
        } else {
            newLoc.setZ(loc1.getZ());
        }
        return newLoc;
    }

    public static Location findGreaterLocation(Location loc1, Location loc2) {
        Location newLoc = new Location(loc1.getWorld(), 0.0D, 0.0D, 0.0D);
        if (loc1.getX() < loc2.getX()) {
            newLoc.setX(loc2.getX());
        } else {
            newLoc.setX(loc1.getX());
        }
        if (loc1.getY() < loc2.getY()) {
            newLoc.setY(loc2.getY());
        } else {
            newLoc.setY(loc1.getY());
        }
        if (loc1.getZ() < loc2.getZ()) {
            newLoc.setZ(loc2.getZ());
        } else {
            newLoc.setZ(loc1.getZ());
        }
        return newLoc;
    }

    public static class BlockPosition {
        public int x;
        public int y;

        public BlockPosition(int x, int y, int z, World world) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
        }

        public int z;
        public World world;

        public String toString() {
            return "[x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",world=" + this.world.getName() + "]";
        }
    }
}