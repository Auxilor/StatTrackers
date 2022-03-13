package com.willfp.stattrackers.stats.stats

import com.willfp.eco.core.events.EntityDeathByEntityEvent
import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import com.willfp.stattrackers.stats.tryAsPlayer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class StatPlayersKilled : Stat("players_killed") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: EntityDeathByEntityEvent) {
        val player = event.killer.tryAsPlayer() ?: return

        if (event.victim !is Player) {
            return
        }

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
