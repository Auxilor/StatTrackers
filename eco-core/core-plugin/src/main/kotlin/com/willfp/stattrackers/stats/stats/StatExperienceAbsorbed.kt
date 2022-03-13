package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerExpChangeEvent

class StatExperienceAbsorbed : Stat("experience_absorbed") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerExpChangeEvent) {
        val player = event.player

        if (player.isBlocking) {
            return
        }

        for (itemStack in player.inventory.armorContents) {
            if (itemStack == null) {
                continue
            }

            if (itemStack.type == Material.AIR) {
                continue
            }

            if (itemStack.type.maxStackSize > 1) {
                return
            }

            itemStack.incrementIfToTrack(this, event.amount.toDouble())
        }
    }
}
