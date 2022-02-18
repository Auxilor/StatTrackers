package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public void onExecute(@NotNull CommandSender sender, @NotNull List<String> args) {
        sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-command"));
    }
}