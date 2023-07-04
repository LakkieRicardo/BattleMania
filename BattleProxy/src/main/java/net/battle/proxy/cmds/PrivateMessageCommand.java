package net.battle.proxy.cmds;

import java.util.Optional;

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

public class PrivateMessageCommand extends CommandBase {

    public PrivateMessageCommand(BMProxyPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLabel() {
        return "msg";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "tell", "message", "t" };
    }

    @Override
    public String getUsage() {
        return "/msg <player> <message...>";
    }

    @Override
    public String getDescription() {
        return "Private message a player";
    }

    @Override
    public void create(CommandManager commandManager) {
        LiteralCommandNode<CommandSource> command = LiteralArgumentBuilder.<CommandSource>literal(getLabel())
        // With player argument
        .then(RequiredArgumentBuilder.<CommandSource, String>argument("player", StringArgumentType.word())
            .suggests((context, builder) -> {
                plugin.server.getAllPlayers().forEach(player -> builder.suggest(player.getUsername()));
                return builder.buildFuture();
            })
            // With message argument
            .then(RequiredArgumentBuilder.<CommandSource, String>argument("message", StringArgumentType.greedyString())
                .executes(context -> {
                    CommandSource executor = context.getSource();
                    if (!(executor instanceof Player executorPlayer)) {
                        executor.sendMessage(Component.text("§cOnly ingame players can send private messages"));
                        return Command.SINGLE_SUCCESS;
                    }
                    String playerName = context.getArgument("player", String.class);
                    String message = context.getArgument("message", String.class);
                    Optional<Player> target = plugin.server.getPlayer(playerName);
                    target.ifPresentOrElse(
                        targetPlayer -> {
                            PrivateMessageHandler.sendPrivateMessage(executorPlayer, targetPlayer, message);
                        },
                        () -> executor.sendMessage(Component.text(Prefixes.ERROR + "Invalid player"))
                    );

                    return Command.SINGLE_SUCCESS;
                })
            )
        )
        // Else...
        .executes(context -> {
            context.getSource().sendMessage(Component.text(Prefixes.ERROR + "Usage: " + getUsage()));
            return Command.SINGLE_SUCCESS;
        }).build();

        CommandMeta commandMeta = commandManager.metaBuilder(getLabel()).aliases(getAliases()).plugin(plugin).build();
        commandManager.register(commandMeta, new BrigadierCommand(command));
    }
    
}