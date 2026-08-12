package com.willfp.stattrackers.commands

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.stattrackers.plugin
import com.willfp.stattrackers.stats.canTrackStats
import com.willfp.stattrackers.stats.trackedStats
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

object CommandStats : Subcommand(
    plugin,
    "stats",
    "stattrackers.command.stats",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val result = StatCommandArgs.resolveTarget(sender, args)

        val (player, item) = when (result) {
            is StatCommandArgs.TargetResult.Failure -> {
                sender.sendMessage(plugin.langYml.getMessage(result.errorKey))
                return
            }
            is StatCommandArgs.TargetResult.Success -> result.player to result.item
        }

        if (!item.canTrackStats) {
            sender.sendMessage(plugin.langYml.getMessage("item-cannot-have-trackers"))
            return
        }

        val tracked = item.trackedStats

        if (tracked.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("no-tracked-stats"))
            return
        }

        sender.sendMessage(
            plugin.langYml.getMessage("stats-list-header").replace("%player%", player.name)
        )

        for (trackedStat in tracked) {
            sender.sendMessage(
                plugin.langYml.getMessage("stats-list-entry")
                    .replace("%stat%", trackedStat.stat.id)
                    .replace("%value%", trackedStat.value.toString())
            )
        }
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        val candidates = when (args.size) {
            0, 1 -> org.bukkit.Bukkit.getOnlinePlayers().map { it.name }
            else -> emptyList()
        }

        StringUtil.copyPartialMatches(args.lastOrNull() ?: "", candidates, completions)

        return completions
    }
}
