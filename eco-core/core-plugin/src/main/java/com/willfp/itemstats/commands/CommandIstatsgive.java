package com.willfp.itemstats.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.command.AbstractTabCompleter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.itemstats.stats.Stat;
import com.willfp.itemstats.stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandIstatsgive extends AbstractCommand {
    /**
     * Instantiate a new /istatsgive command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandIstatsgive(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "istatsgive", "itemstats.give", false);
    }

    @Override
    public AbstractTabCompleter getTab() {
        return new TabcompleterIstatsgive();
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {

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
    }
}
