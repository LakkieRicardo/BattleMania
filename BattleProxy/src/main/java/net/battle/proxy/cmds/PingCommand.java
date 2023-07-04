package net.battle.proxy.cmds;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import net.battle.proxy.BMProxyPlugin;
import net.battle.proxy.Prefixes;
import net.kyori.adventure.text.Component;

public class PingCommand extends CommandBase {
    
    public PingCommand(BMProxyPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLabel() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getUsage() {
        return "/ping";
    }

    @Override
    public String getDescription() {
        return "Get your own ping";
    }

    @Override
    public void create(CommandManager commandManager) {
        LiteralCommandNode<CommandSource> command = LiteralArgumentBuilder.<CommandSource>literal(getLabel()).executes(context -> {
            if (!(context.getSource() instanceof Player player)) {
                context.getSource().sendMessage(Component.text("Run this command ingame"));
                return Command.SINGLE_SUCCESS;
            }
            player.sendMessage(Component.text(Prefixes.COMMAND + "Your ping is " + player.getPing() + "ms"));
            return Command.SINGLE_SUCCESS;
        }).build();
        CommandMeta commandMeta = commandManager.metaBuilder(getLabel()).plugin(plugin).build();
        commandManager.register(commandMeta, new BrigadierCommand(command));
    }

}