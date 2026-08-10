package com.willfp.stattrackers.commands

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.stattrackers.plugin
import com.willfp.stattrackers.stats.Stats
import com.willfp.stattrackers.stats.canTrackStats
import com.willfp.stattrackers.stats.getStatValue
import com.willfp.stattrackers.stats.setStatValue
import com.willfp.stattrackers.stats.statsToTrack
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

object CommandAdd : Subcommand(
    plugin,
    "add",
    "stattrackers.command.add",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val result = StatCommandArgs.resolveTarget(sender, args)

        val (player, item, remaining) = when (result) {
            is StatCommandArgs.TargetResult.Failure -> {
                sender.sendMessage(plugin.langYml.getMessage(result.errorKey))
                return
            }
            is StatCommandArgs.TargetResult.Success -> Triple(result.player, result.item, result.remainingArgs)
        }

        if (!item.canTrackStats) {
            sender.sendMessage(plugin.langYml.getMessage("item-cannot-have-trackers"))
            return
        }

        if (remaining.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("needs-stat"))
            return
        }

        if (remaining.size < 2) {
            sender.sendMessage(plugin.langYml.getMessage("needs-amount"))
            return
        }

        val stat = Stats[remaining[0]]

        if (stat == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-stat"))
            return
        }

        if (stat !in item.statsToTrack) {
            sender.sendMessage(plugin.langYml.getMessage("stat-not-tracked"))
            return
        }

        val amount = remaining[1].toDoubleOrNull()

        if (amount == null || !amount.isFinite()) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-amount"))
            return
        }

        val newValue = (item.getStatValue(stat) + amount).coerceAtLeast(0.0)
        item.setStatValue(stat, newValue)

        sender.sendMessage(
            plugin.langYml.getMessage("add-success")
                .replace("%stat%", stat.id)
                .replace("%amount%", amount.toString())
                .replace("%new%", newValue.toString())
                .replace("%player%", player.name)
        )
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()
        StringUtil.copyPartialMatches(args.lastOrNull() ?: "", StatCommandArgs.tabCompleteTarget(args), completions)
        return completions
    }
}
