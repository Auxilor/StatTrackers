package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.TabCompleteHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandGive extends Subcommand {

    /**
     * The cached stat names.
     */
    private static final List<String> STAT_NAMES = Stats.values().stream().map(Stat::getKey).map(NamespacedKey::getKey).collect(Collectors.toList());

    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandGive(@NotNull final EcoPlugin plugin) {
        super(plugin, "give", "stattrackers.command.give", false);
    }

    /**
     * Called on plugin reload.
     */
    @ConfigUpdater
    public static void reload() {
        STAT_NAMES.clear();
        STAT_NAMES.addAll(Stats.values().stream().map(Stat::getKey).map(NamespacedKey::getKey).collect(Collectors.toList()));
        STAT_NAMES.add("none");
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull List<String> args) {
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

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull List<String> args) {
        List<String> completions = new ArrayList<>();

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return STAT_NAMES;
        }

        if (args.size() == 1) {
            StringUtil.copyPartialMatches(args.get(0), Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), completions);
            return completions;
        }

        if (args.size() == 2) {
            StringUtil.copyPartialMatches(args.get(1), STAT_NAMES, completions);

            Collections.sort(completions);
            return completions;
        }

        return new ArrayList<>(0);
    }
}
