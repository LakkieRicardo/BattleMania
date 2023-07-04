package net.battle.core.supplydrop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.math.MathFunction;

public class SupplyDrop {
    private Player playing = null;

    private Location playingLocation;

    private SupplyDropType type;

    private boolean isPlaying;

    private int frameNumber;

    private int taskNumber;

    private MathFunction height;

    private Location chestLocation;

    private final Region location;
    private final Location playerLocation;

    public SupplyDrop(SupplyDropType type, MathFunction height, Region location, Location playerLocation,
            Location chestLocation) {
        this.type = type;
        this.isPlaying = false;
        this.frameNumber = 0;
        this.taskNumber = -1;
        this.height = height;
        this.location = location;
        this.playerLocation = playerLocation;
        this.chestLocation = chestLocation;
    }

    public boolean play(Player pl) {
        if (this.isPlaying) {
            return false;
        }
        this.isPlaying = true;
        this.taskNumber = Bukkit.getScheduler().scheduleSyncDelayedTask(BMCorePlugin.ACTIVE_PLUGIN, () -> update(), 5L);
        this.playing = pl;
        this.playingLocation = pl.getLocation();
        return true;
    }

    private void stopPlaying() {
        if (this.taskNumber == -1) {
            return;
        }

        Bukkit.getScheduler().cancelTask(this.taskNumber);

        this.playing.teleport(this.playingLocation);
        this.playing = null;
        this.isPlaying = false;
    }

    private void update() {
        if (!isPlaying()) {
            return;
        }
        if (this.frameNumber < 20) {
            stopPlaying();

            return;
        }
        this.playing.teleport(this.playingLocation);

        this.frameNumber++;
    }

    public Player getCurrentPlaying() {
        return this.playing;
    }

    public SupplyDropType getType() {
        return this.type;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public int getFrameNumber() {
        return this.frameNumber;
    }

    public int getTaskNumber() {
        return this.taskNumber;
    }

    public void setType(SupplyDropType type) {
        this.type = type;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public MathFunction getHeightCalculator() {
        return this.height;
    }

    public Region getLocation() {
        return this.location;
    }

    public Location getPlayerLocation() {
        return this.playerLocation;
    }

    public Location getChestLocation() {
        return this.chestLocation;
    }

    public void setChestLocation(Location chestLocation) {
        this.chestLocation = chestLocation;
    }
}