package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandReload extends Subcommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandReload(@NotNull final EcoPlugin plugin) {
        super(plugin, "reload", "stattrackers.command.reload", false);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull List<String> args) {
        sender.sendMessage(
                this.getPlugin().getLangYml().getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                        .replace("%time%", NumberUtils.format(this.getPlugin().reloadWithTime()))
        );
    }
}
