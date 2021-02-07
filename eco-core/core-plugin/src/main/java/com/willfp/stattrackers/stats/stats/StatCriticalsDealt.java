package com.willfp.stattrackers.stats.stats;

import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class StatCriticalsDealt extends Stat {
    public StatCriticalsDealt() {
        super("criticals_dealt");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();

        if (player == null) {
            return;
        }

        if (!(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            return;
        }

        if (!(player.getFallDistance() > 0 && !player.isOnGround())) {
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

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
