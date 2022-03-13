package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerItemDamageEvent

class StatItemDamage : Stat("item_damage") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: PlayerItemDamageEvent) {
        val itemStack = event.item
        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementStatValue(this, event.damage.toDouble())
    }
}
