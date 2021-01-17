package com.willfp.itemstats.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandIstatsreload extends AbstractCommand {
    /**
     * Instantiate a new /istatsreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandIstatsreload(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "istatsreload", "itemstats.reload", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        this.getPlugin().reload();
        sender.sendMessage(this.getPlugin().getLangYml().getMessage("reloaded"));
    }
}
