package net.battle.proxy.listeners;

import java.util.Optional;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.battle.proxy.BMProxyPlugin;
import net.kyori.adventure.text.Component;

public class ServerKickListener extends ListenerBase {

    public ServerKickListener(BMProxyPlugin plugin) {
        super(plugin);
    }
    @Subscribe
    public void onPlayerKick(KickedFromServerEvent event) {
        if (event.getServer().getServerInfo().getName().equalsIgnoreCase("Lobby")) {
            return;
        }

        Optional<RegisteredServer> lobby = plugin.getLobbyServer();
        if (!lobby.isPresent()) {
            return;
        }
        
        Component message = Component.text("The server you were on has crashed or was forcibly shutdown. In the meantime you, will be in the \"Lobby\" server.");
        event.setResult(KickedFromServerEvent.RedirectPlayer.create(lobby.get(), message));
    }

}
