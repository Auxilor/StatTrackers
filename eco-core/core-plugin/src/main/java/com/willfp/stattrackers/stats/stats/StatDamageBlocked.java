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

public class StatDamageBlocked extends Stat {
    public StatDamageBlocked() {
        super("damage_blocked");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (!(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            return;
        }

        if (!player.isBlocking()) {
            return;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        ItemStack itemStack;

        if (mainHand.getType() == Material.SHIELD) {
            itemStack = mainHand;
        } else {
            if (offHand.getType() == Material.SHIELD) {
                itemStack = offHand;
            } else {
                return;
            }
        }

        if (itemStack.getType().getMaxStackSize() > 1) {
            return;
        }

        double value = StatChecks.getStatOnItem(itemStack, this);
        value += event.getDamage();
        StatChecks.setStatOnItem(itemStack, this, value);
    }
}
