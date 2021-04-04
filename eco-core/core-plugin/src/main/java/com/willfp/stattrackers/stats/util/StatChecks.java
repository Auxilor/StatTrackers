package com.willfp.stattrackers.stats.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.stattrackers.StatTrackersPlugin;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.Stats;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"deprecation", "DeprecatedIsStillUsed"})
@UtilityClass
public class StatChecks {
    /**
     * Instance of StatTrackers.
     */
    private static final EcoPlugin PLUGIN = StatTrackersPlugin.getInstance();

    /**
     * The key for storing the currently displayed stat.
     */
    private static final NamespacedKey ACTIVE_KEY = PLUGIN.getNamespacedKeyFactory().create("active");

    /**
     * The key for storing the currently displayed stat.
     * <p>
     * For legacy support.
     */
    @Deprecated
    private static final NamespacedKey LEGACY_ACTIVE_KEY = new NamespacedKey("itemstats", "active");

    /**
     * What stat value is present on an item?
     *
     * @param item The item to query.
     * @param stat The stat to query.
     * @return The value, or 0 if not found.
     */
    public static double getStatOnItem(@Nullable final ItemStack item,
                                       @NotNull final Stat stat) {
        if (item == null) {
            return 0;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return 0;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (container.has(stat.getLegacyKey(), PersistentDataType.DOUBLE)) {
            double value = container.get(stat.getLegacyKey(), PersistentDataType.DOUBLE);
            container.remove(stat.getLegacyKey());
            item.setItemMeta(meta);
            setStatOnItem(item, stat, value);
            return getStatOnItem(item, stat);
        }

        if (!container.has(stat.getKey(), PersistentDataType.DOUBLE)) {
            setStatOnItem(item, stat, 0);
            return getStatOnItem(item, stat);
        }

        return container.get(stat.getKey(), PersistentDataType.DOUBLE);
    }

    /**
     * Set a stat on an item.
     *
     * @param item  The item to modify.
     * @param stat  The stat to set.
     * @param value The value to set.
     */
    public static void setStatOnItem(@Nullable final ItemStack item,
                                     @NotNull final Stat stat,
                                     final double value) {
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(stat.getKey(), PersistentDataType.DOUBLE, value);

        item.setItemMeta(meta);
    }

    /**
     * Set active stat on an item.
     *
     * @param item The item to modify.
     * @param stat The stat to set.
     */
    public static void setActiveStat(@Nullable final ItemStack item,
                                     @Nullable final Stat stat) {
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (stat != null) {
            container.set(ACTIVE_KEY, PersistentDataType.STRING, stat.getKey().toString());
        } else {
            container.set(ACTIVE_KEY, PersistentDataType.STRING, "null");
        }

        item.setItemMeta(meta);

        if (PLUGIN.getConfigYml().getBool("reset-on-apply")) {
            if (stat != null) {
                setStatOnItem(item, stat, 0);
            }
        }
    }

    /**
     * Get active stat on an item.
     *
     * @param item The item to query.
     * @return The found stat, or null if none active.
     */
    public static Stat getActiveStat(@Nullable final ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        String active = container.get(ACTIVE_KEY, PersistentDataType.STRING);

        if (active == null || active.equals("null")) {
            active = container.get(LEGACY_ACTIVE_KEY, PersistentDataType.STRING);
            if (active == null || active.equals("null")) {
                return null;
            }
        }

        return Stats.getByKey(PLUGIN.getNamespacedKeyFactory().create(active.split(":")[1]));
    }
}
