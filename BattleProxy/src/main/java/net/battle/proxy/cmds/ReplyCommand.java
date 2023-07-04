package net.battle.proxy.cmds;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import net.battle.proxy.BMProxyPlugin;
import net.battle.proxy.Prefixes;
import net.battle.proxy.PrivateMessageHandler;
import net.kyori.adventure.text.Component;

public class ReplyCommand extends CommandBase {

    public static final long EXPIRATION_MINUTES = 15;

    public ReplyCommand(BMProxyPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLabel() {
        return "reply";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "r", "respond" };
    }

    @Override
    public String getUsage() {
        return "/reply <message...>";
    }

    @Override
    public String getDescription() {
        return "Respond to a private message";
    }

    @Override
    public void create(CommandManager commandManager) {
        LiteralCommandNode<CommandSource> command = LiteralArgumentBuilder.<CommandSource>literal(getLabel())
        // With message argument
        .then(RequiredArgumentBuilder.<CommandSource, String>argument("message", StringArgumentType.greedyString())
            .executes(context -> {
                CommandSource executor = context.getSource();
                if (!(executor instanceof Player executorPlayer)) {
                    executor.sendMessage(Component.text("§cOnly ingame players can reply to private messages"));
                    return Command.SINGLE_SUCCESS;
                }

                if (!PrivateMessageHandler.LAST_DM_RECEIVED.containsKey(executorPlayer.getUniqueId())) {
                    executor.sendMessage(Component.text(Prefixes.ERROR + "You have not received a private message to reply to"));
                    return Command.SINGLE_SUCCESS;
                }

                long currentTime = System.currentTimeMillis();
                var messageInfo = PrivateMessageHandler.LAST_DM_RECEIVED.get(executorPlayer.getUniqueId());
                if (currentTime - messageInfo.messageTime > TimeUnit.MINUTES.toMillis(EXPIRATION_MINUTES)) {
                    executor.sendMessage(Component.text(Prefixes.ERROR + "You have not received a private message in the last " + EXPIRATION_MINUTES + " minutes"));
                    return Command.SINGLE_SUCCESS;
                }
                Optional<Player> source = plugin.server.getPlayer(messageInfo.source);
                if (source.isEmpty()) {
                    executor.sendMessage(Component.text(Prefixes.ERROR + "The target player is offline"));
                    return Command.SINGLE_SUCCESS;
                }

                String message = context.getArgument("message", String.class);
                PrivateMessageHandler.sendPrivateMessage(executorPlayer, source.get(), message);
                return Command.SINGLE_SUCCESS;
            })
        )
        // With no arguments
        .executes(context -> {
            context.getSource().sendMessage(Component.text(Prefixes.ERROR + "Usage: " + getUsage()));
            return Command.SINGLE_SUCCESS;
        }).build();

        CommandMeta commandMeta = commandManager.metaBuilder(getLabel()).aliases(getAliases()).plugin(plugin).build();
        commandManager.register(commandMeta, new BrigadierCommand(command));
    }
    
}