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
import net.kyori.adventure.text.Component;

public class LocateCommand extends CommandBase {

    public LocateCommand(BMProxyPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLabel() {
        return "locate";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "findserver" };
    }

    @Override
    public String getUsage() {
        return "/locate <player>";
    }

    @Override
    public String getDescription() {
        return "Find the location of a player";
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
            .executes(context -> {
                CommandSource executor = context.getSource();

                String playerName = context.getArgument("player", String.class);
                Optional<Player> target = plugin.server.getPlayer(playerName);
                target.ifPresentOrElse(
                    targetPlayer -> executor.sendMessage(Component.text(Prefixes.COMMAND + "Player server is §c" + targetPlayer.getCurrentServer().get().getServerInfo().getName())),
                    () -> executor.sendMessage(Component.text(Prefixes.ERROR + "Invalid player"))
                );
                
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