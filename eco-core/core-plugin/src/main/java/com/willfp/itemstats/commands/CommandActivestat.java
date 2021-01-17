package com.willfp.itemstats.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.command.AbstractTabCompleter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.itemstats.stats.Stat;
import com.willfp.itemstats.stats.Stats;
import com.willfp.itemstats.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandActivestat extends AbstractCommand {
    /**
     * Instantiate a new /activestat command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandActivestat(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "activestat", "itemstats.activestat", true);
    }

    @Override
    public AbstractTabCompleter getTab() {
        return new TabcompleterActivestat();
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
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
    }
}
