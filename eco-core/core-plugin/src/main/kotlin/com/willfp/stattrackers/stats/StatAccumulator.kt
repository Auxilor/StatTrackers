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
            .filter { stat in it.statsToTrack }

        for (item in items) {
            if (item.canTrackStats) {
                item.incrementIfToTrack(stat, count)
            }
        }
    }
}
