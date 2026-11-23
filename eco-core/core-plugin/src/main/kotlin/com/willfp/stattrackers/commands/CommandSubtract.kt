package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.getStatValue
import com.willfp.stattrackers.stats.setStatValue
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder

internal fun CommandBuilder<CommandSender>.subtractCommand() = subcommand(
    name = "subtract",
    permission = "stattrackers.command.subtract"
) {
    executes {
        changeTrackedStat { player, item, stat, amount ->
            val newValue = (item.getStatValue(stat) - amount).coerceAtLeast(0.0)
            item.setStatValue(stat, newValue)

            reply(
                "subtract-success",
                "stat" to stat.name,
                "amount" to amount,
                "new" to newValue,
                "player" to player.name
            )
        }
    }

    completes {
        StatCommandArgs.tabCompleteTarget(sender, args)
    }
}
