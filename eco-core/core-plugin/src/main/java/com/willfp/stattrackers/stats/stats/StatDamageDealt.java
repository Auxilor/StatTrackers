package com.willfp.stattrackers.stats.stats;

import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

public class StatDamageDealt extends Stat {
    public StatDamageDealt() {
        super("damage_dealt");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(@NotNull final EntityDamageByEntityEvent event) {
        Player player = null;

        if (event.getDamager() instanceof Player) {
            player = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
            if (shooter == null) {
                return;
            }
            if (shooter instanceof Player) {
                player = (Player) shooter;
            }
        }

        if (player == null) {
            return;
        }

        if (!(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

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
        value += event.getDamage();
        StatChecks.setStatOnItem(itemStack, this, value);
    }
}
