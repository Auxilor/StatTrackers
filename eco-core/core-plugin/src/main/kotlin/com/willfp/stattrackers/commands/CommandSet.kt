package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.setStatValue
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder

internal fun CommandBuilder<CommandSender>.setCommand() = subcommand(
    name = "set",
    permission = "stattrackers.command.set"
) {
    executes {
        changeTrackedStat { player, item, stat, amount ->
            val clampedAmount = amount.coerceAtLeast(0.0)
            item.setStatValue(stat, clampedAmount)

            reply("set-success", "stat" to stat.name, "value" to clampedAmount, "player" to player.name)
        }
    }

    completes {
        StatCommandArgs.tabCompleteTarget(sender, args)
    }
}
