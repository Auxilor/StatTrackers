package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.canTrackStats
import com.willfp.stattrackers.stats.trackedStats
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.entity.runOwned

internal fun CommandBuilder<CommandSender>.statsCommand() = subcommand(
    name = "stats",
    permission = "stattrackers.command.stats"
) {
    executes {
        val (player, _) = resolveTarget()

        player.runOwned {
            val item = player.inventory.itemInMainHand
            val tracked = item.trackedStats

            when {
                !item.canTrackStats -> {
                    reply("item-cannot-have-trackers")
                }

                tracked.isEmpty() -> {
                    reply("no-tracked-stats")
                }

                else -> {
                    reply("stats-list-header", "player" to player.name)

                    for (trackedStat in tracked) {
                        reply("stats-list-entry", "stat" to trackedStat.stat.name, "value" to trackedStat.value)
                    }
                }
            }
        }
    }

    completes {
        if (args.size == 1) {
            playerNames()
        } else {
            emptyList()
        }
    }
}
