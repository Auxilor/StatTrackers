package com.willfp.stattrackers.commands;

import com.willfp.eco.core.command.TabCompleteHandler;
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

public class TabCompleterGive implements TabCompleteHandler {
    /**
     * The cached enchantment names.
     */
    private static final List<String> STAT_NAMES = Stats.values().stream().map(Stat::getKey).map(NamespacedKey::getKey).collect(Collectors.toList());

    /**
     * Called on reload.
     */
    @ConfigUpdater
    public static void reload() {
        STAT_NAMES.clear();
        STAT_NAMES.addAll(Stats.values().stream().map(Stat::getKey).map(NamespacedKey::getKey).collect(Collectors.toList()));
        STAT_NAMES.add("none");
    }

    @Override
    public List<String> tabComplete(@NotNull final CommandSender sender,
                                    @NotNull final List<String> args) {
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
