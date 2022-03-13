package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import com.willfp.stattrackers.stats.tryAsPlayer
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class StatDamageDealt : Stat("damage_dealt") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val player = event.damager.tryAsPlayer() ?: return

        if (!(event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            return
        }

        val itemStack = player.inventory.itemInMainHand ?: return

        if (itemStack.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementStatValue(this, event.damage)
    }
}
