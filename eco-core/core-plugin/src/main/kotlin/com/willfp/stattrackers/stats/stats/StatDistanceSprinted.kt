package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

class StatDistanceSprinted : Stat("distance_sprinted") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerMoveEvent) {
        val player = event.player

        if (!player.isSprinting) {
            return
        }

        val from = event.from
        val to = event.to ?: return
        if (from.world != to.world) {
            return
        }
        val distance = from.distance(to)

        val itemStack = player.inventory.boots ?: return

        if (itemStack.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementIfToTrack(this, distance)
    }
}
