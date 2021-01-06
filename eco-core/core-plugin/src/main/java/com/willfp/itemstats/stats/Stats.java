package com.willfp.itemstats.stats;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.itemstats.stats.stats.StatDamageDealt;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

@UtilityClass
@SuppressWarnings({"unused", "checkstyle:JavadocVariable"})
public class Stats {
    public static final String CONFIG_LOCATION = "config.";
    public static final String GENERAL_LOCATION = "general-config.";

    private static final BiMap<NamespacedKey, Stat> BY_KEY = HashBiMap.create();

    public static final Stat DAMAGE_DEALT = new StatDamageDealt();

    /**
     * Get all registered {@link Stat}s.
     *
     * @return A list of all {@link Stat}s.
     */
    public static List<Stat> values() {
        return ImmutableList.copyOf(BY_KEY.values());
    }

    /**
     * Get {@link Stat} matching key.
     *
     * @param key The NamespacedKey to search for.
     * @return The matching {@link Stat}, or null if not found.
     */
    public static Stat getByKey(@Nullable final NamespacedKey key) {
        if (key == null) {
            return null;
        }
        return BY_KEY.get(key);
    }

    /**
     * Update all {@link Stat}s.
     */
    @ConfigUpdater
    public static void update() {
        for (Stat stat : new HashSet<>(values())) {
            stat.update();
        }
    }

    /**
     * Add new {@link Stat} to ItemStats.
     * <p>
     * Only for internal use, stats are automatically added in the constructor.
     *
     * @param stat The {@link Stat} to add.
     */
    public static void addNewStat(@NotNull final Stat stat) {
        BY_KEY.remove(stat.getKey());
        BY_KEY.put(stat.getKey(), stat);
    }

    /**
     * Remove {@link Stat} from ItemStats.
     *
     * @param stat The {@link Stat} to remove.
     */
    public static void removeStat(@NotNull final Stat stat) {
        BY_KEY.remove(stat.getKey());
    }
}
