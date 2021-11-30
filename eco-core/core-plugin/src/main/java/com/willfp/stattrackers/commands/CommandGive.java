package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.TabCompleteHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandGive extends Subcommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandGive(@NotNull final EcoPlugin plugin) {
        super(plugin, "give", "stattrackers.command.give", false);
    }

    @Override
    public TabCompleteHandler getTabCompleter() {
        return new TabCompleterGive();
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            if (args.isEmpty()) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("needs-player"));
                return;
            }

            if (args.size() == 1) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("needs-stat"));
                return;
            }

            String recieverName = args.get(0);
            Player reciever = Bukkit.getPlayer(recieverName);

            if (reciever == null) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-player"));
                return;
            }

            String statName = args.get(1);
            Stat stat = Stats.getByKey(this.getPlugin().getNamespacedKeyFactory().create(statName));
            if (stat == null) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-stat"));
                return;
            }

            String message = this.getPlugin().getLangYml().getMessage("give-success");
            message = message.replace("%stat%", stat.getColor() + stat.getDescription()).replace("%recipient%", reciever.getName());
            sender.sendMessage(message);
            reciever.getInventory().addItem(stat.getTracker().getItemStack());
        };
    }
}
