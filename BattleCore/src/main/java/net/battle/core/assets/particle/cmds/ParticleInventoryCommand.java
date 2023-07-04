package net.battle.core.assets.particle.cmds;

import org.bukkit.entity.Player;

import net.battle.core.assets.particle.gui.ParticleInventoryBuilder;
import net.battle.core.command.CommandBase;

public class ParticleInventoryCommand implements CommandBase {
    public String getLabel() {
        return "particleinv";
    }

    public String[] getAliases() {
        return new String[] { "ptinv" };
    }

    @Override
    public String getDescription() {
        return "Open particle inventory";
    }

    public void onCommandExecute(Player pl, String[] args) {
        pl.openInventory(ParticleInventoryBuilder.create(0, pl.getUniqueId()));
        pl.sendMessage("§9§lCOMMAND§8 > §fOpened §cparticle§f inventory");
    }

    @Override
    public String getUsage() {
        return "/particleinv";
    }
}