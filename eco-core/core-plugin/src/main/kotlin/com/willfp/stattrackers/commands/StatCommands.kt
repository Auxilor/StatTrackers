package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.Stat
import com.willfp.stattrackers.stats.Stats
import com.willfp.stattrackers.stats.canTrackStats
import com.willfp.stattrackers.stats.statsToTrack
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import wiki.ecohub.core.command.CommandContext
import wiki.ecohub.game.entity.runOwned

/**
 * Resolve `[player] <stat> <amount>` and run [change] on the target's region with their held
 * item, the stat and the amount. Checks after the region switch reply directly, as a failure
 * there may run off the command's thread.
 */
internal fun CommandContext<CommandSender>.changeTrackedStat(
    change: (player: Player, item: ItemStack, stat: Stat, amount: Double) -> Unit
) {
    val (player, remaining) = resolveTarget()

    player.runOwned {
        val item = player.inventory.itemInMainHand
        val stat = remaining.getOrNull(0)?.let { Stats[it] }
        val amount = remaining.getOrNull(1)?.toDoubleOrNull()

        when {
            !item.canTrackStats -> reply("item-cannot-have-trackers")
            remaining.isEmpty() -> reply("needs-stat")
            remaining.size < 2 -> reply("needs-amount")
            stat == null -> reply("invalid-stat")
            stat !in item.statsToTrack -> reply("stat-not-tracked")
            amount == null || !amount.isFinite() -> reply("invalid-amount")
            else -> change(player, item, stat, amount)
        }
    }
}

/**
 * The target player and the arguments after them, or a failure with the lang key.
 */
internal fun CommandContext<CommandSender>.resolveTarget(): Pair<Player, List<String>> =
    when (val result = StatCommandArgs.resolveTarget(sender, args)) {
        is StatCommandArgs.TargetResult.Failure -> fail(result.errorKey)
        is StatCommandArgs.TargetResult.Success -> result.player to result.remainingArgs
    }
