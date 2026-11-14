package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.getStatValue
import com.willfp.stattrackers.stats.setStatValue
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder

internal fun CommandBuilder<CommandSender>.addCommand() = subcommand(
    name = "add",
    permission = "stattrackers.command.add"
) {
    executes {
        changeTrackedStat { player, item, stat, amount ->
            val newValue = (item.getStatValue(stat) + amount).coerceAtLeast(0.0)
            item.setStatValue(stat, newValue)

            reply(
                "add-success",
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
