package com.willfp.stattrackers.tracker.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.stattrackers.StatTrackersPlugin;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.Stats;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TrackerUtils {

    /**
     * A list of materials used by trackers.
     */
    public static List<Material> trackerMaterials = new ArrayList<>();

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

        return getTrackedStat(meta);
    }

    /**
     * Get tracked stat on item.
     *
     * @param meta The item to check.
     * @return The found stat, or null.
     */
    @Nullable
    public static Stat getTrackedStat(@NotNull final ItemMeta meta) {
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

    /**
     * Register material used by trackers.
     *
     * @param material - Material to register
     */
    public static void registerMaterial(Material material) {
        if (!trackerMaterials.contains(material)) {
            trackerMaterials.add(material);
        }
    }

}
