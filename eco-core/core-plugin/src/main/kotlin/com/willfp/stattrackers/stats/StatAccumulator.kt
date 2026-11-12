package com.willfp.stattrackers.stats

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import wiki.ecohub.game.entity.runOwned
import wiki.ecohub.rules.counter.Accumulator

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
