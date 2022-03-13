package com.willfp.stattrackers.stats.stats

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.incrementStatValue
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent

class StatHeadshots : Stat("headshots") {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun statListener(event: EntityDamageByEntityEvent) {

        val projectile = event.damager as? Projectile ?: return
        val player = projectile.shooter as? Player ?: return
        val victim = event.entity as? LivingEntity ?: return

        if (projectile.location.y < victim.location.y + victim.eyeHeight - 0.22) {
            return
        }

        val itemStack = player.inventory.itemInMainHand

        if (itemStack.type != Material.BOW && itemStack.type != Material.CROSSBOW) {
            return
        }

        if (itemStack.type.maxStackSize > 1) {
            return
        }

        itemStack.incrementStatValue(this, 1.0)
    }
}
