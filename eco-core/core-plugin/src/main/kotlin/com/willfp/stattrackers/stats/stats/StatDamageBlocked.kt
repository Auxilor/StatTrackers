package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class StatDamageBlocked : Stat("damage_blocked") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return

        if (!(event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            return
        }

        if (!player.isBlocking) {
            return
        }

        val mainHand = player.inventory.itemInMainHand
        val offHand = player.inventory.itemInOffHand
        val itemStack = if (mainHand.type == Material.SHIELD) {
            mainHand
        } else {
            if (offHand.type == Material.SHIELD) {
                offHand
            } else {
                return
            }
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementIfToTrack(this, event.damage)
    }
}
