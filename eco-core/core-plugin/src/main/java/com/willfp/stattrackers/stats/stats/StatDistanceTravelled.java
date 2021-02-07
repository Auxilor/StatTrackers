package com.willfp.stattrackers.stats.stats;

import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StatDistanceTravelled extends Stat {
    public StatDistanceTravelled() {
        super("distance_travelled");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo() == null) {
            return;
        }

        if (!Objects.equals(event.getFrom().getWorld(), event.getTo().getWorld())) {
            return;
        }

        double distance = event.getFrom().distance(event.getTo());

        ItemStack itemStack = player.getInventory().getBoots();

        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() == Material.AIR) {
            return;
        }

        if (itemStack.getType().getMaxStackSize() > 1) {
            return;
        }

        double value = StatChecks.getStatOnItem(itemStack, this);
        value += distance;
        StatChecks.setStatOnItem(itemStack, this, value);
    }
}
