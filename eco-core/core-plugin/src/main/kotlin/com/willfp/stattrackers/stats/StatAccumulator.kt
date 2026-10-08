package com.willfp.stattrackers.stats

import com.willfp.libreforge.counters.Accumulator
import com.willfp.stattrackers.runOwned
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class StatAccumulator(
    private val stat: Stat
) : Accumulator {
    override fun accept(player: Player, count: Double) {
        player.runOwned {
            val items = mutableListOf<ItemStack>()

            for (target in stat.targets) {
                for (item in target.slot.getItems(player)) {
                    if (stat in item.statsToTrack && items.none { it == item }) {
                        items += item
                    }
                }
            }

            for (item in items) {
                item.incrementIfToTrack(stat, count)
            }
        }
    }
}
