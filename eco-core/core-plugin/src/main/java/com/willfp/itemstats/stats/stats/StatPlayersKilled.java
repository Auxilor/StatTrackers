package com.willfp.itemstats.stats.stats;

import com.willfp.eco.util.events.entitydeathbyentity.EntityDeathByEntityEvent;
import com.willfp.itemstats.stats.Stat;
import com.willfp.itemstats.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StatPlayersKilled extends Stat {
    public StatPlayersKilled() {
        super("players_killed");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final EntityDeathByEntityEvent event) {
        if (!(event.getKiller() instanceof Player)) {
            return;
        }

        if (!(event.getVictim() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getKiller();

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() == Material.AIR) {
            return;
        }

        double value = StatChecks.getStatOnItem(itemStack, this);
        value += 1;
        StatChecks.setStatOnItem(itemStack, this, value);
    }
}
