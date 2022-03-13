package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

class StatDistanceFlown : Stat("distance_flown") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerMoveEvent) {
        val player = event.player

        if (!player.isGliding) {
            return
        }

        if (event.to == null) {
            return
        }

        if (event.from.world != event.to.world) {
            return
        }

        val distance = event.from.distance(event.to)
        val itemStack = player.inventory.chestplate ?: return
        if (itemStack.type != Material.ELYTRA) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementStatValue(this, distance)
    }
}
