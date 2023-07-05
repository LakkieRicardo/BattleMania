package net.battle.proxy.listeners;

import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent.ForwardResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.battle.proxy.BMProxyPlugin;
import net.battle.proxy.cmds.CommandBase;
import net.kyori.adventure.text.Component;

public class PluginMessageListener extends ListenerBase {

    private final ChannelIdentifier identifier;

    public PluginMessageListener(BMProxyPlugin plugin, ChannelIdentifier identifier) {
        super(plugin);
        this.identifier = identifier;
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        plugin.logger.info(event.getIdentifier().getId());
        if (!event.getIdentifier().equals(identifier)) {
            return;
        }

        event.setResult(ForwardResult.handled());
        ByteArrayDataInput in = ByteStreams.newDataInput(event.dataAsInputStream());
        String messageType = in.readUTF();
        plugin.logger.info("Received message of type " + messageType + " from " + event.getSource().getClass().getSimpleName());

        if (messageType.equals("TrySendMessage")) {
            UUID target = UUID.fromString(in.readUTF());
            String message = in.readUTF();
            plugin.server.getPlayer(target).ifPresent(player -> player.sendMessage(Component.text(message)));
        } else if (messageType.equals("KickPlayer")) {
            UUID target = UUID.fromString(in.readUTF());
            String message = in.readUTF();
            plugin.server.getPlayer(target).ifPresent(player -> player.disconnect(Component.text(message)));
        } else if (messageType.equals("ConnectPlayers")) {
            String rawPlayerList = in.readUTF();
            String targetServerName = in.readUTF();
            var targetServerOptional = plugin.server.getServer(targetServerName);
            if (targetServerOptional.isPresent()) {
                RegisteredServer server = targetServerOptional.get();
                String[] playerUUIDArray;
                if (rawPlayerList.contains(",")) {
                    playerUUIDArray = rawPlayerList.split(",");
                } else {
                    playerUUIDArray = new String[] { rawPlayerList };
                }
                for (String playerUUID : playerUUIDArray) {
                    plugin.server.getPlayer(UUID.fromString(playerUUID)).ifPresent(player -> {
                        player.createConnectionRequest(server).fireAndForget();
                    });
                }
            }
        } else if (messageType.equals("ConnectAllPlayers")) {
            String targetServerName = in.readUTF();
            plugin.server.getServer(targetServerName).ifPresent(targetServer -> {
                if (event.getSource() instanceof ServerConnection sourceServer) {
                    for (Player player : sourceServer.getServer().getPlayersConnected()) {
                        player.createConnectionRequest(targetServer).fireAndForget();
                    }
                }
            });
        } else if (messageType.equals("GetCommands")) {
            if (event.getSource() instanceof ServerConnection sourceServer) {   
                List<CommandBase> commands = plugin.commandList;
                var paramLabels = new StringBuilder();
                var paramAliases = new StringBuilder();
                var paramUsages = new StringBuilder();
                var paramDescriptions = new StringBuilder();
                for (CommandBase command : commands) {
                    paramLabels.append(',');
                    paramLabels.append(command.getLabel());
                    paramAliases.append(';');
                    if (command.getAliases() != null) {
                        paramAliases.append(String.join(",", command.getAliases()));
                    }
                    paramUsages.append(',');
                    paramUsages.append(command.getUsage().replaceAll(",", "\\,"));
                    paramDescriptions.append(',');
                    paramDescriptions.append(command.getDescription().replaceAll(",", "\\,"));
                }

                ByteArrayDataOutput data = ByteStreams.newDataOutput();
                data.writeUTF("GetCommands_Reply");
                data.writeUTF(paramLabels.substring(1));
                data.writeUTF(paramAliases.substring(1));
                data.writeUTF(paramUsages.substring(1));
                data.writeUTF(paramDescriptions.substring(1));

                sourceServer.getServer().sendPluginMessage(identifier, data.toByteArray());
            }
            return;
        } else if (messageType.equals("GetPlayerServer")) {
            String targetPlayer = in.readUTF();
            if (event.getSource() instanceof ServerConnection sourceServer) {
                plugin.server.getPlayer(UUID.fromString(targetPlayer)).ifPresent(player -> {
    
                    player.getCurrentServer().ifPresent(playerServer -> {
                        String targetServerName = playerServer.getServerInfo().getName();
    
                        ByteArrayDataOutput data = ByteStreams.newDataOutput();
                        data.writeUTF("GetPlayerServer_Reply");
                        data.writeUTF(player.getUniqueId().toString());
                        data.writeUTF(targetServerName);
    
                        sourceServer.getServer().sendPluginMessage(identifier, data.toByteArray());
                    });
                });
            }
        } else if (messageType.equals("PlayerStatUpdated")) {
            String updateUsername = in.readUTF();
            UUID targetPlayerUUID = UUID.fromString(in.readUTF());
            String fieldName = in.readUTF();
            String fieldValue = in.readUTF();

            plugin.server.getPlayer(targetPlayerUUID).ifPresent(target -> {
                ByteArrayDataOutput data = ByteStreams.newDataOutput();
                data.writeUTF("PlayerStatUpdated_Forward");
                data.writeUTF(updateUsername);
                data.writeUTF(targetPlayerUUID.toString());
                data.writeUTF(fieldName);
                data.writeUTF(fieldValue);

                target.getCurrentServer().get().sendPluginMessage(identifier, data.toByteArray());

            });
        } else {
            plugin.logger.warn("Received invalid message type from channel bmcore:messages: " + messageType);
        }

    }
    
}
