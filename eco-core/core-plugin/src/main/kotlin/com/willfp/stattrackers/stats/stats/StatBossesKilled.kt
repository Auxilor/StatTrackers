package com.willfp.stattrackers.stats.stats

import com.willfp.eco.core.events.EntityDeathByEntityEvent
import com.willfp.eco.util.namespacedKeyOf
import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementIfToTrack
import com.willfp.stattrackers.stats.tryAsPlayer
import org.bukkit.Material
import org.bukkit.entity.Boss
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.persistence.PersistentDataType

class StatBossesKilled : Stat("bosses_killed") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: EntityDeathByEntityEvent) {
        val player = event.killer.tryAsPlayer()

        if (player == null) {
            return
        }

        if (
            event.victim !is Boss
            && !event.victim.persistentDataContainer.has(
                namespacedKeyOf("ecobosses", "boss"),
                PersistentDataType.STRING
            )
        ) {
            return
        }

        val itemStack = player.inventory.itemInMainHand

        if (itemStack.type == Material.AIR) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementIfToTrack(this, 1.0)
    }
}
