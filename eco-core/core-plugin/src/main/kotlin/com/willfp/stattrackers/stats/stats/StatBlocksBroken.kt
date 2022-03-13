package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent

class StatBlocksBroken : Stat("blocks_broken") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: BlockBreakEvent) {
        val player = event.player
        val itemStack = player.inventory.itemInMainHand

        if (itemStack.type == Material.AIR) {
            return
        }

        if (event.block.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementIfToTrack(this, 1.0)
    }
}
