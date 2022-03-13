package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class StatCriticalsDealt : Stat("criticals_dealt") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return

        if (!(event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            return
        }

        @Suppress("DEPRECATION")
        if (!(player.fallDistance > 0 && !player.isOnGround)) {
            return
        }

        val itemStack = player.inventory.itemInMainHand

        if (itemStack.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementIfToTrack(this, event.damage)
    }
}
