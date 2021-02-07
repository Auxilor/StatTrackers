package com.willfp.stattrackers.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandStreload extends AbstractCommand {
    /**
     * Instantiate a new /istatsreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandStreload(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "streload", "stattrackers.reload", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        this.getPlugin().reload();
        sender.sendMessage(this.getPlugin().getLangYml().getMessage("reloaded"));
    }
}
