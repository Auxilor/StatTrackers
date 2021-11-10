package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.TabCompleteHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.Stats;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommandActivestat extends Subcommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandActivestat(@NotNull final EcoPlugin plugin) {
        super(plugin, "activestat", "stattrackers.command.activestat", true);
    }

    @Override
    public TabCompleteHandler getTabCompleter() {
        return new TabCompleterActivestat();
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            Player player = (Player) sender;

            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack == null || itemStack.getType() == Material.AIR) {
                player.sendMessage(this.getPlugin().getLangYml().getMessage("must-hold-item"));
                return;
            }

            if (args.isEmpty()) {
                StatChecks.setActiveStat(itemStack, null);
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("removed-stat"));
                return;
            }

            String keyName = args.get(0);

            if (keyName.equals("none")) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("removed-stat"));
                StatChecks.setActiveStat(itemStack, null);
                return;
            }

            Stat stat = Stats.getByKey(this.getPlugin().getNamespacedKeyFactory().create(keyName));

            if (stat == null) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-stat"));
                return;
            }

            StatChecks.setActiveStat(itemStack, stat);

            player.sendMessage(this.getPlugin().getLangYml().getMessage("set-active-stat"));
        };
    }
}
