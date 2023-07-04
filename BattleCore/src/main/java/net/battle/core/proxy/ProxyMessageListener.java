package net.battle.core.proxy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.battle.core.BMCorePlugin;
import net.battle.core.handlers.BMLogger;

public class ProxyMessageListener implements PluginMessageListener {

    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals(BMCorePlugin.PROXY_CHANNEL_MESSAGES)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String requestType = in.readUTF();
        BMLogger.info("Received message of type " + requestType + " from proxy");
        if (requestType.equals("GetCommands_Reply")) {
            ProxyHandler.PROXY_COMMANDS.clear();
            String[] labels = in.readUTF().split(",");
            String[] aliasesList = in.readUTF().split(";");
            List<String> usages = splitListWithEscapes(in.readUTF());
            List<String> descriptions = splitListWithEscapes(in.readUTF());
            for (int i = 0; i < labels.length; i++) {
                String[] aliases = null;
                if (aliasesList[i] != null) {
                    if (aliasesList[i].contains(",")) {
                        aliases = aliasesList[i].split(",");
                    } else if (!aliasesList[i].equals("")) {
                        aliases = new String[] { aliasesList[i] };
                    }
                }
                ProxyHandler.PROXY_COMMANDS.add(new ProxyCommandInfo(labels[i], usages.get(i), descriptions.get(i), aliases));
            }
            BMLogger.info("Received commands from proxy: " + String.join(", ", labels));
        }
    }

    /**
     * Splits a comma-separated list with escaped commas. For example:
     * <code>
     *   a,b     -->   {"a", "b"}
     *   a\,b,c  -->   { "a,b", "c" }
     * </code>
     * 
     * @param input String to split
     * @return List with escaped strings
     */
    public static List<String> splitListWithEscapes(String input) {
        List<String> output = new ArrayList<>();
        boolean escaped = false;
        StringBuilder currentToken = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '\\') {
                if (escaped) {
                    escaped = false;
                    currentToken.append('\\');
                    continue;
                }
                escaped = true;
                continue;
            }
            if (c == ',') {
                if (escaped) {
                    escaped = false;
                    currentToken.append(',');
                    continue;
                }
                output.add(currentToken.toString());
                currentToken.setLength(0);
                continue;
            }
            currentToken.append(c);
        }

        if (currentToken.length() > 0) {
            output.add(currentToken.toString());
        }

        return output;
    }

}
