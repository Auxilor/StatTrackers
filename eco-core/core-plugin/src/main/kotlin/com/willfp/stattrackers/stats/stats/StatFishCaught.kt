package com.willfp.stattrackers.stats.stats

import com.willfp.eco.core.events.EntityDeathByEntityEvent
import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import com.willfp.stattrackers.stats.tryAsPlayer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerFishEvent

class StatFishCaught : Stat("fish_caught") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerFishEvent) {
        val player = event.player as? Player ?: return

        if(event.state != PlayerFishEvent.State.CAUGHT_FISH) {
            return
        }

        val itemStack = player.inventory.itemInMainHand

        if (itemStack.type != Material.FISHING_ROD) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementIfToTrack(this, 1.0)

    }
}
