package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.Stats
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.drop.DropQueue
import wiki.ecohub.game.entity.runOwned

internal fun CommandBuilder<CommandSender>.giveCommand() = subcommand(
    name = "give",
    permission = "stattrackers.command.give"
) {
    executes {
        val receiverName = argument(0, "needs-player")
        val statId = argument(1, "needs-stat")

        val receiver = Bukkit.getPlayer(receiverName) ?: fail("invalid-player")
        val stat = Stats[statId] ?: fail("invalid-stat")

        receiver.runOwned {
            DropQueue(receiver)
                .addItem(stat.tracker)
                .forceTelekinesis()
                .push()

            reply("give-success", "stat" to stat.name, "recipient" to receiver.name)
        }
    }

    completes {
        when (args.size) {
            1 -> playerNames()
            2 -> Stats.values().map { it.id }
            else -> emptyList()
        }
    }
}
