package com.willfp.stattrackers.tracker.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.stattrackers.StatTrackersPlugin;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.Stats;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class TrackerUtils {
    /**
     * Legacy stat tracker key.
     */
    @Deprecated
    private static final NamespacedKey LEGACY_KEY = new NamespacedKey("itemstats", "stat_tracker");

    /**
     * Instance of StatTrackers to create keys.
     */
    private static final EcoPlugin PLUGIN = StatTrackersPlugin.getInstance();

    /**
     * Get tracked stat on item.
     *
     * @param itemStack The item to check.
     * @return The found stat, or null.
     */
    @Nullable
    public static Stat getTrackedStat(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) {
            return null;
        }

        if (meta.getPersistentDataContainer().has(PLUGIN.getNamespacedKeyFactory().create("stat_tracker"), PersistentDataType.STRING)) {
            String statKeyName = meta.getPersistentDataContainer().get(PLUGIN.getNamespacedKeyFactory().create("stat_tracker"), PersistentDataType.STRING);
            if (statKeyName == null) {
                return null;
            }
            return Stats.getByKey(PLUGIN.getNamespacedKeyFactory().create(statKeyName));
        }

        if (meta.getPersistentDataContainer().has(LEGACY_KEY, PersistentDataType.STRING)) {
            String statKeyName = meta.getPersistentDataContainer().get(LEGACY_KEY, PersistentDataType.STRING);
            if (statKeyName == null) {
                return null;
            }
            return Stats.getByKey(PLUGIN.getNamespacedKeyFactory().create(statKeyName));
        }

        return null;
    }
}
