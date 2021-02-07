package com.willfp.stattrackers.stats.stats;

import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StatExperienceAbsorbed extends Stat {
    public StatExperienceAbsorbed() {
        super("experience_absorbed");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final PlayerExpChangeEvent event) {
        Player player = event.getPlayer();

        if (player.isBlocking()) {
            return;
        }

        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack == null) {
                continue;
            }

            if (itemStack.getType() == Material.AIR) {
                continue;
            }

            if (itemStack.getType().getMaxStackSize() > 1) {
                return;
            }

            double value = StatChecks.getStatOnItem(itemStack, this);
            value += event.getAmount();
            StatChecks.setStatOnItem(itemStack, this, value);
        }
    }
}
