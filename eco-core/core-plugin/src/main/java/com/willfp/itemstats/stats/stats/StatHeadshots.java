package com.willfp.itemstats.stats.stats;

import com.willfp.eco.util.events.entitydeathbyentity.EntityDeathByEntityEvent;
import com.willfp.itemstats.stats.Stat;
import com.willfp.itemstats.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StatHeadshots extends Stat {
    public StatHeadshots() {
        super("headshots");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final EntityDeathByEntityEvent event) {
        if (!(event.getKiller() instanceof Projectile)) {
            return;
        }

        Projectile projectile = (Projectile) event.getKiller();

        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) projectile.getShooter();

        if (player == null) {
            return;
        }

        if (projectile.getLocation().getY() < event.getVictim().getLocation().getY() + event.getVictim().getEyeHeight() - 0.22) {
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.BOW && itemStack.getType() != Material.CROSSBOW) {
            return;
        }

        double value = StatChecks.getStatOnItem(itemStack, this);
        value += 1;
        StatChecks.setStatOnItem(itemStack, this, value);
    }
}
