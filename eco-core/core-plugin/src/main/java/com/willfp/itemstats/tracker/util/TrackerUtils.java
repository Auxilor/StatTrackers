package com.willfp.itemstats.tracker.util;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.itemstats.stats.Stat;
import com.willfp.itemstats.stats.Stats;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class TrackerUtils {
    /**
     * Instance of ItemStats to create keys.
     */
    private static final AbstractEcoPlugin PLUGIN = AbstractEcoPlugin.getInstance();

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

        return null;
    }
}