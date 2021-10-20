package com.willfp.stattrackers.tracker;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import com.willfp.stattrackers.tracker.util.TrackerUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TrackerListener extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Create new listeners for dragging trackers onto items.
     *
     * @param plugin The plugin to listen for.
     */
    public TrackerListener(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Listen for inventory click event.
     *
     * @param event The event to handle.
     */
    @EventHandler
    public void onDrag(@NotNull final InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        if (current == null || cursor == null) {
            return;
        }

        if (!TrackerUtils.TRACKER_MATERIALS.contains(cursor.getType())) {
            return;
        }

        Stat tracked = TrackerUtils.getTrackedStat(cursor);

        if (current.getType() == Material.AIR) {
            return;
        }

        if (current.getType().getMaxStackSize() > 1) {
            return;
        }

        if (StatChecks.getActiveStat(current) == tracked) {
            return;
        }

        StatChecks.setActiveStat(current, tracked);

        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));

        event.setCancelled(true);
    }
}
