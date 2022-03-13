package com.willfp.stattrackers.stats.stats

import com.willfp.eco.core.events.PlayerJumpEvent
import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class StatTimesJumped : Stat("times_jumped") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerJumpEvent) {
        val player = event.player

        val itemStack = player.inventory.boots ?: return

        if (itemStack.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementIfToTrack(this, 1.0)
    }
}
