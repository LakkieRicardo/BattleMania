package net.battle.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.battle.proxy.cmds.CommandBase;
import net.battle.proxy.cmds.LocateCommand;
import net.battle.proxy.cmds.PrivateMessageCommand;
import net.battle.proxy.cmds.PingCommand;
import net.battle.proxy.cmds.ReplyCommand;
import net.battle.proxy.listeners.PluginMessageListener;

@Plugin(id = "bm-proxy-plugin", name = "BMProxyPlugin", version = "1", authors = { "Lakkie" })
public class BMProxyPlugin {

    @Inject
    public ProxyServer server;

    @Inject
    public Logger logger;

    public final List<CommandBase> commandList = new ArrayList<>();

    private static final ChannelIdentifier bmIdentifier = MinecraftChannelIdentifier.from("battlemania:messages");

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Registering listeners...");

        logger.info("Registering commands...");
        registerCommand(new PingCommand(this));
        registerCommand(new PrivateMessageCommand(this));
        registerCommand(new ReplyCommand(this));
        registerCommand(new LocateCommand(this));

        logger.info("Setting up plugin messages channel on " + bmIdentifier.getId() + "...");
        server.getChannelRegistrar().register(bmIdentifier);
        registerListener(new PluginMessageListener(this, bmIdentifier));
    }

    public void registerListener(Object listener) {
        logger.info(String.format("Registering listener %s", listener.getClass().getSimpleName()));
        server.getEventManager().register(this, listener);
    }

    public void registerCommand(CommandBase command) {
        logger.info(String.format("Registering command %s", command.getClass().getSimpleName()));
        commandList.add(command);
        command.create(server.getCommandManager());
    }

    public Optional<RegisteredServer> getLobbyServer() {
        Optional<RegisteredServer> lobby = server.getServer("Lobby");
        if (!lobby.isPresent()) {
            return server.getServer("TestServer");
        }
        return lobby;
    }
}