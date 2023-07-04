package net.battle.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;

public final class PrivateMessageHandler {

    /**
     * Keeps track of who was last private messaged by who. The key is the player
     * who received the message, the value is the player who sent the message.
     */
    public static final Map<UUID, DirectMessageInfo> LAST_DM_RECEIVED = new HashMap<>();

    private PrivateMessageHandler() {
    }

    public static void sendPrivateMessage(Player source, Player target, String message) {
        source.sendMessage(Component.text(Prefixes.DM + "§7§lYOU§7 -> " + target.getUsername() + ": §f" + message));
        target.sendMessage(Component.text(Prefixes.DM + source.getUsername() + " -> §7§lYOU§7: §f" + message));
        LAST_DM_RECEIVED.put(target.getUniqueId(), new PrivateMessageHandler.DirectMessageInfo(source.getUniqueId(), System.currentTimeMillis()));
    }

    public static class DirectMessageInfo {
        public final UUID source;
        public final long messageTime;

        public DirectMessageInfo(UUID source, long messageTime) {
            this.source = source;
            this.messageTime = messageTime;
        }
    }

}
