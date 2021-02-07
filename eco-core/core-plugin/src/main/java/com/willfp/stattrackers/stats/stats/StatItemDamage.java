package com.willfp.stattrackers.stats.stats;

import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StatItemDamage extends Stat {
    public StatItemDamage() {
        super("item_damage");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final PlayerItemDamageEvent event) {
        ItemStack itemStack = event.getItem();

        if (itemStack.getType().getMaxStackSize() > 1) {
            return;
        }

        double value = StatChecks.getStatOnItem(itemStack, this);
        value += event.getDamage();
        StatChecks.setStatOnItem(itemStack, this, value);
    }
}
