package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import org.jetbrains.annotations.NotNull;

public class CommandStatTrackers extends PluginCommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandStatTrackers(@NotNull final EcoPlugin plugin) {
        super(plugin, "stattrackers", "stattrackers.command.stattrackers", false);

        this.addSubcommand(new CommandReload(plugin))
                .addSubcommand(new CommandGive(plugin))
                .addSubcommand(new CommandActivestat(plugin));
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-command"));
        };
    }
}