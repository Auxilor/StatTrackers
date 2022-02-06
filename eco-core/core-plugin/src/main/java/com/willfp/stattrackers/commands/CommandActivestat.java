package com.willfp.stattrackers.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.TabCompleteHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.util.StringUtils;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.Stats;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandActivestat extends Subcommand {

    /**
     * The cached stat names.
     */
    private static final List<String> STAT_NAMES = Stats.values().stream().map(Stat::getKey).map(NamespacedKey::getKey).collect(Collectors.toList());

    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandActivestat(@NotNull final EcoPlugin plugin) {
        super(plugin, "activestat", "stattrackers.command.activestat", true);
    }

    @ConfigUpdater
    public static void reload() {
        STAT_NAMES.clear();
        STAT_NAMES.addAll(Stats.values().stream().map(Stat::getKey).map(NamespacedKey::getKey).collect(Collectors.toList()));
        STAT_NAMES.add("none");
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull List<String> args) {
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

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull List<String> args) {
        List<String> completions = new ArrayList<>();

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return STAT_NAMES;
        }

        StringUtil.copyPartialMatches(String.join(" ", args), STAT_NAMES, completions);

        if (args.size() > 1) { // Remove all previous words from the candidate of completions
            ArrayList<String> finishedArgs = new ArrayList<>(args);
            finishedArgs.remove(args.size() - 1);

            String prefix = String.join(" ", finishedArgs);
            completions = completions.stream().map(statName -> StringUtils.removePrefix(statName, prefix).trim()).collect(Collectors.toList());
        }

        Collections.sort(completions);
        return completions;
    }
}
