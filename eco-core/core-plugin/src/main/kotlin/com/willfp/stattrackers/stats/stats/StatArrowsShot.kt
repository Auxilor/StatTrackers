package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityShootBowEvent

class StatArrowsShot : Stat("arrows_shot") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onDamage(event: EntityShootBowEvent) {
        val player = event.entity as? Player ?: return

        val itemStack = player.inventory.itemInMainHand ?: return

        if (itemStack.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementStatValue(this, 1.0)
    }
}
