package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

class StatDistanceTravelled : Stat("distance_travelled") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerMoveEvent) {
        val player = event.player

        if (event.to == null) {
            return
        }

        if (event.from.world != event.to.world) {
            return
        }

        val distance = event.from.distance(event.to)

        val itemStack = player.inventory.boots ?: return

        if (itemStack.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementStatValue(this, distance)
    }
}
