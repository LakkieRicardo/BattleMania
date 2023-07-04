package net.battle.core.layouts;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import net.battle.core.SettingsFiles;

public class InventoryLayouts {

    private static JSONArray root = null;

    private InventoryLayouts() {}

    public static NavigatorInventoryLayout createNavigatorFromId(String particlesLayoutId) {
        if (root == null) {
            throw new IllegalStateException("Layout JSON must be initialized");
        }

        for (int i = 0; i < root.size(); i++) {
            JSONObject member = (JSONObject) root.get(i);
            if (((String)member.get("id")).equalsIgnoreCase(particlesLayoutId)) {
                return new NavigatorInventoryLayout(member);
            }
        }

        return null;
    }

    public static void initializeLayoutsFile() throws IOException, ParseException {
        root = (JSONArray) SettingsFiles.initializeJSONResource("/InventoryLayouts.json");
    }
    
}
