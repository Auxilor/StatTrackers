package com.willfp.itemstats.stats.util;


import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.itemstats.stats.Stat;
import com.willfp.itemstats.stats.Stats;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class StatChecks {
    /**
     * Instance of ItemStats.
     */
    private final AbstractEcoPlugin PLUGIN = AbstractEcoPlugin.getInstance();

    /**
     * The key for storing the currently displayed stat.
     */
    private static final NamespacedKey ACTIVE_KEY = PLUGIN.getNamespacedKeyFactory().create("active");

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

        if (!container.has(stat.getKey(), PersistentDataType.DOUBLE)) {
            setStatOnItem(item, stat, 0);
        }

        item.setItemMeta(meta);

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

        if (!container.has(stat.getKey(), PersistentDataType.DOUBLE)) {
            container.set(stat.getKey(), PersistentDataType.DOUBLE, value);
        }

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
            return null;
        }

        return Stats.getByKey(PLUGIN.getNamespacedKeyFactory().create(active.split(":")[1]));
    }
}
