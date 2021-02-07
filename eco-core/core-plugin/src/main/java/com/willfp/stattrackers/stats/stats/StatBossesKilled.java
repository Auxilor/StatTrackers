package com.willfp.stattrackers.stats.stats;

import com.willfp.eco.util.events.entitydeathbyentity.EntityDeathByEntityEvent;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

public class StatBossesKilled extends Stat {
    public StatBossesKilled() {
        super("bosses_killed");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final EntityDeathByEntityEvent event) {
        Player player = null;

        if (event.getKiller() instanceof Player) {
            player = (Player) event.getKiller();
        } else if (event.getKiller() instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) event.getKiller()).getShooter();
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

        if (!(event.getVictim() instanceof Boss || event.getVictim() instanceof Illusioner)) {
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
        value += 1;
        StatChecks.setStatOnItem(itemStack, this, value);
    }
}
