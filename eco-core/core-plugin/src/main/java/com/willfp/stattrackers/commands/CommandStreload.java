package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import org.jetbrains.annotations.NotNull;

public class CommandStreload extends PluginCommand {
    /**
     * Instantiate a new /istatsreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandStreload(@NotNull final EcoPlugin plugin) {
        super(plugin, "streload", "stattrackers.reload", false);
    }


    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            this.getPlugin().reload();
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("reloaded"));
        };
    }
}
