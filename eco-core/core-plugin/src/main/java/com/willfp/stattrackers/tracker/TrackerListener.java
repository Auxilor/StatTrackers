package com.willfp.stattrackers.tracker;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import com.willfp.stattrackers.tracker.util.TrackerUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TrackerListener extends PluginDependent implements Listener {
    /**
     * Create new listeners for dragging trackers onto items.
     *
     * @param plugin The plugin to listen for.
     */
    public TrackerListener(@NotNull final AbstractEcoPlugin plugin) {
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

        if (cursor.getType() != Material.COMPASS) {
            return;
        }

        Stat tracked = TrackerUtils.getTrackedStat(cursor);

        if (current.getType() == Material.AIR) {
            return;
        }

        if (current.getType().getMaxStackSize() > 1) {
            return;
        }

        StatChecks.setActiveStat(current, tracked);

        event.getWhoClicked().setItemOnCursor(null);

        event.setCancelled(true);
    }
}
