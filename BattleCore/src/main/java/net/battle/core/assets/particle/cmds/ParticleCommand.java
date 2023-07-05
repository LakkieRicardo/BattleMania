package net.battle.core.assets.particle.cmds;

import org.bukkit.entity.Player;

import net.battle.core.assets.particle.BMParticle;
import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.Prefixes;

public class ParticleCommand implements CommandBase {
    public String getLabel() {
        return "particle";
    }

    public String[] getAliases() {
        return new String[] { "pt" };
    }

    @Override
    public String getDescription() {
        return "Equip or unequip a particle";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (args.length != 1) {
            CommandHandler.sendUsage(pl, this);
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
            String particles = "";

            for (BMParticle particle1 : BMParticle.particles) {
                particles = particles + ", " + particle1.getName();
            }

            particles = particles.replaceFirst(", ", "");
            pl.sendMessage(Prefixes.COMMAND + "Particles: " + particles);
            return;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            BMParticle.dequip(pl);
            return;
        }

        BMParticle particle = null;
        for (BMParticle current : BMParticle.particles) {
            if (current.getName().equalsIgnoreCase(args[0])) {
                particle = current;
            }
        }

        if (particle == null) {
            pl.sendMessage(Prefixes.ERROR + "Invalid particle");
            CommandHandler.sendUsage(pl, this);
            return;
        }

        particle.equip(pl);
    }

    @Override
    public String getUsage() {
        return "/particle <list,particle,disable>";
    }
}