package net.battle.core.proxy;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.battle.core.BMCorePlugin;

public class ProxyHandler implements Listener {

    public static final List<ProxyCommandInfo> PROXY_COMMANDS = new ArrayList<>();

    public static void sendToServer(List<UUID> players, String targetServer) {
        StringBuilder playerUUIDList = new StringBuilder();
        for (UUID uuid : players) {
            playerUUIDList.append(',');
            playerUUIDList.append(uuid);
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("ConnectPlayers");
            out.writeUTF(playerUUIDList.substring(1));
            out.writeUTF(targetServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BMCorePlugin.ACTIVE_PLUGIN.sendPluginMessage(b.toByteArray());
    }

    public static void sendAllToServer(String targetServer) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("ConnectAllPlayers");
            out.writeUTF(targetServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BMCorePlugin.ACTIVE_PLUGIN.sendPluginMessage(b.toByteArray());
    }

    public static void kickPlayer(Player player, String playerUUID, String kickMessage) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("KickPlayer");
            out.writeUTF(playerUUID);
            out.writeUTF(kickMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BMCorePlugin.ACTIVE_PLUGIN.sendPluginMessage(b.toByteArray());
    }

    public static boolean hasPlayerPlayedBefore(String name) {
        // TODO: query player list DB
        return Bukkit.getOfflinePlayer(name).hasPlayedBefore();
    }

    public static String getPlayerServer(String name) {
        // TODO: implement this
        return "TestServer";
    }

    public static boolean isPlayerOnline(String name) {
        return Bukkit.getPlayerExact(name) != null;
    }

    public static void queryForProxyCommands() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("GetCommands");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BMCorePlugin.ACTIVE_PLUGIN.sendPluginMessage(b.toByteArray());
    }
}