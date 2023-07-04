package net.battle.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SettingsFiles {

    private SettingsFiles() { }

    public static YamlConfiguration initializeSettingsFile(String settingsFileLocation) throws IOException {
        File settingsFile = new File(BMCorePlugin.ACTIVE_PLUGIN.getDataFolder() + File.separator + settingsFileLocation);
        if (!settingsFile.exists()) {
            InputStream istream = BMCorePlugin.class.getResourceAsStream("/BMSettingsTemplate.yml");
            Files.copy(istream, settingsFile.toPath());
            istream.close();
        }
        return YamlConfiguration.loadConfiguration(settingsFile);
    }

    public static Object initializeJSONResource(String jsonResourceLocation) throws IOException, ParseException {
        // Might want to save this to the plugin data folder?
        return new JSONParser().parse(new InputStreamReader(BMCorePlugin.class.getResourceAsStream(jsonResourceLocation)));
    }

}
