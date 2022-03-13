package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent

class StatDamageTaken : Stat("damage_taken") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return

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

            itemStack.incrementStatValue(this, event.damage)
        }
    }
}
