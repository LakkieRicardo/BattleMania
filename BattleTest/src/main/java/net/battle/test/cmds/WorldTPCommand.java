package net.battle.test.cmds;

import java.io.File;
import java.io.FilenameFilter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.core.handlers.RankHandler;

public class WorldTPCommand implements CommandBase {
    public String getLabel() {
        return "worldtp";
    }

    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Teleport to a certain world in the test server";
    }

    public void onCommandExecute(Player pl, String[] args) {
        if (!RankHandler.developerPermission(pl)) {
            CommandHandler.sendPerms(pl);
            return;
        }
        if (args.length == 0) {
            CommandHandler.sendUsage(pl, this);
            return;
        }
        String wName = args[0];

        File serverDir = Bukkit.getWorldContainer();
        File[] serverDirFiles = serverDir.listFiles(new ServerDirectoryFilter());
        boolean found = false;
        byte b;
        int i;
        File[] arrayOfFile1;
        for (i = (arrayOfFile1 = serverDirFiles).length, b = 0; b < i;) {
            File file = arrayOfFile1[b];
            if (file != null && file.getName().equals(wName))
                found = true;
            b++;
        }

        if (!found) {
            pl.sendMessage("§4§lERROR§8 > §cThis world does not exist.");

            return;
        }
        File uidFile = new File(Bukkit.getWorldContainer().toPath().toAbsolutePath().toString() + "/" + wName + "/uid.dat");
        System.out.printf("uid.dat file exists for world %s? ? %s\n", new Object[] { wName, Boolean.valueOf(uidFile.exists()) });
        if (uidFile.exists()) {
            uidFile.delete();
        }

        World w = Bukkit.getWorld(wName);

        if (w == null) {
            WorldCreator creator = new WorldCreator(wName);
            w = creator.createWorld();
        }

        pl.setGameMode(GameMode.SPECTATOR);
        if (w.getSpawnLocation() != null) {
            pl.teleport(w.getSpawnLocation());
        } else {
            pl.teleport(new Location(w, 0.0D, 0.0D, 0.0D));
        }
        pl.sendMessage("§9§lCOMMAND§8 > §fYou teleported to §c" + w.getName());
    }

    public static class ServerDirectoryFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return true;
        }
    }

    @Override
    public String getUsage() {
        return "/worldtp <world>";
    }
}