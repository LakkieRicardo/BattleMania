package net.battle.test.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.battle.test.BMTestPlugin;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public abstract class BungeeHandler implements PluginMessageListener {
    private static String[] currentData;
    private String waitingFor;
    private boolean array;
    private boolean received = false;

    public BungeeHandler(String waitingFor, boolean array) {
        this.waitingFor = waitingFor;
        this.array = array;
    }

    public void onPluginMessageReceived(String channel, Player pl, byte[] message) {
        if (this.received) {
            return;
        }
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals(this.waitingFor)) {
            this.received = true;
            if (this.array) {
                onReceive(in.readUTF().split(", "));
            } else {
                onReceive(new String[] { in.readUTF() });
            }
        }
    }

    public boolean hasReceived() {
        return this.received;
    }

    public static String[] getStatFromBungee(String stat, String[] args, boolean isOutputArray, Player messenger) {
        BungeeHandler receiver = new BungeeHandler(stat, isOutputArray) {
            public void onReceive(String[] received) {
                BungeeHandler.currentData = received;
            }
        };
        sendRequest(stat, args, messenger);
        do {
        } while (!receiver.hasReceived());

        return currentData;
    }

    public static void sendRequest(String stat, String[] args, Player messenger) {
        BungeeRequest req = startRequest(stat);
        addArgumentsToRequest(req, args);
        finishRequest(req, messenger);
    }

    public static BungeeRequest startRequest(String stat) {
        BungeeRequest req = new BungeeRequest();
        try {
            req.out.writeUTF(stat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return req;
    }

    public static void addArgumentsToRequest(BungeeRequest req, String[] args) {
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = args).length, b = 0; b < i;) {
            String s = arrayOfString[b];
            addArgumentToRequest(req, s);
            b++;
        }

    }

    public static void addArgumentToRequest(BungeeRequest req, String arg) {
        try {
            req.out.writeUTF(arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finishRequest(BungeeRequest req, Player messenger) {
        messenger.sendPluginMessage((Plugin) BMTestPlugin.ACTIVE_PLUGIN, "BungeeCord", req.bytes.toByteArray());
    }

    public static String[] getStatFromBungee(String stat, String[] args, boolean isOutputArray) {
        return getStatFromBungee(stat, args, isOutputArray, Bukkit.getOnlinePlayers().iterator().next());
    }

    public abstract void onReceive(String[] paramArrayOfString);
}