package net.battle.test.handlers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;

import net.battle.core.handlers.BMLogger;
import net.battle.core.supplydrop.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Booth {
    public static final List<Booth> BOOTHS = new ArrayList<>();

    private boolean isOccupied = false;
    private String creator;
    private List<String> invited = new ArrayList<>();
    private Region.BlockPosition signPosition;
    private Region region;

    public Booth(Region.BlockPosition sign, Region region) {
        this.signPosition = sign;
        this.region = region;
        BOOTHS.add(this);
        BMLogger.info("Created booth #" + (BOOTHS.size() - 1) + " :with sign position " + this.signPosition.toString() + " with a region "
                + this.region.toString());
    }

    public boolean isOccupied() {
        return this.isOccupied;
    }

    public String getCreator() {
        return this.creator;
    }

    public List<String> getInvited() {
        return this.invited;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setCreator(String creator) {
        this.creator = creator;
        getInvited().add(creator);
    }

    public Region getRegion() {
        return this.region;
    }

    public void updateSign() {
        Sign sign = (Sign) this.signPosition.world.getBlockAt(this.signPosition.x, this.signPosition.y, this.signPosition.z).getState();
        sign.getSide(Side.FRONT).line(0, Component.text("Status:").decorate(TextDecoration.BOLD).color(NamedTextColor.BLACK));
        sign.getSide(Side.FRONT).line(1, isOccupied() ? Component.text("OCCUPIED").decorate(TextDecoration.BOLD).color(NamedTextColor.DARK_RED) : Component.text("OPEN").decorate(TextDecoration.BOLD).color(NamedTextColor.GREEN));
        sign.update();
    }
}