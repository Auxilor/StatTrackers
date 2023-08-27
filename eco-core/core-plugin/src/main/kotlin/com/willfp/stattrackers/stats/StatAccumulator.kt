package com.willfp.stattrackers.stats

import com.willfp.libreforge.counters.Accumulator
import org.bukkit.Material
import org.bukkit.entity.Player

class StatAccumulator(
    private val stat: Stat
) : Accumulator {
    override fun accept(player: Player, count: Double) {
        val items = stat.targets
            .flatMap { it.slot.getItems(player) }
            .toSet() // Remove duplicates
            .filter { stat in it.statsToTrack }

        for (item in items) {
            if (item.amount == 1 && item.type != Material.AIR) {
                item.incrementIfToTrack(stat, count)
            }
        }
    }
}
