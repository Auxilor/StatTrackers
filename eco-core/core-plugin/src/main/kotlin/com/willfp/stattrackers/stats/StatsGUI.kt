package com.willfp.stattrackers.stats

import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object StatsGUI {
    private val gui: Menu = menu(1) {
        for (column in (1..9)) {
            setSlot(1, column, slot({ player, _ ->
                val toTrack = player.inventory.itemInMainHand.statsToTrack
                toTrack.toList().getOrNull(column - 1)?.tracker?.clone() ?: ItemStack(Material.AIR)
            }) {
                setCaptive()
            })
        }

        setTitle("Stats")
        onClose { event, menu ->
            val player = event.player as Player
            val captive = menu.getCaptiveItems(player)
            val toReturn = captive.filter { it.statTracker == null }
            val trackers = captive.mapNotNull { it.statTracker }
            val item = player.inventory.itemInMainHand
            item.statsToTrack = trackers

            DropQueue(player)
                .addItems(toReturn)
                .forceTelekinesis()
                .push()
        }
    }

    fun open(player: Player) {
        gui.open(player)
    }
}