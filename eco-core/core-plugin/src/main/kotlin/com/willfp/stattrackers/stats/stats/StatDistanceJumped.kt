package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

class StatDistanceJumped : Stat("distance_jumped") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerMoveEvent) {
        val player = event.player

        @Suppress("DEPRECATION")
        if (player.isOnGround) {
            return
        }

        if (player.isFlying || player.isGliding || player.isSwimming) {
            return
        }

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
