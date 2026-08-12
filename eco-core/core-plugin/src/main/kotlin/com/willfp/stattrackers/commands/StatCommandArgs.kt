package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.Stats
import com.willfp.stattrackers.stats.getStatValue
import com.willfp.stattrackers.stats.statsToTrack
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object StatCommandArgs {
    val AMOUNT_COMPLETIONS = listOf("1", "5", "10", "25", "50", "100")

    sealed class TargetResult {
        data class Success(
            val player: Player,
            val item: ItemStack,
            val remainingArgs: List<String>
        ) : TargetResult()

        data class Failure(val errorKey: String) : TargetResult()
    }

    /**
     * Resolves an optional leading `[player]` from [args] against [sender].
     * The target's mainhand item is always used.
     *
     * - If the first token isn't a known stat id, it's treated as a player name.
     * - Otherwise the target is [sender] (which must be a player).
     */
    fun resolveTarget(sender: CommandSender, args: List<String>): TargetResult {
        var index = 0
        var targetPlayer: Player? = null

        if (index < args.size && Stats[args[index]] == null) {
            targetPlayer = Bukkit.getPlayer(args[index]) ?: return TargetResult.Failure("invalid-player")
            index++
        }

        if (targetPlayer == null) {
            if (sender !is Player) {
                return TargetResult.Failure("not-player")
            }
            targetPlayer = sender
        }

        val item = targetPlayer.inventory.itemInMainHand

        return TargetResult.Success(targetPlayer, item, args.drop(index))
    }

    /**
     * Suggests completions for the token currently being typed, given the
     * already-committed tokens preceding it (`args` minus the last element).
     *
     * Stages: `[player] <stat> <amount>`. Stat suggestions are limited to
     * the stats actually tracked on the resolved target's mainhand item.
     */
    fun tabCompleteTarget(sender: CommandSender, args: List<String>): List<String> {
        val committed = args.dropLast(1)

        var index = 0
        if (index < committed.size && Stats[committed[index]] == null) {
            index++
        }

        val remainingCount = committed.size - index

        if (committed.isNotEmpty() && remainingCount > 1) {
            return emptyList()
        }

        val result = resolveTarget(sender, committed.take(index))

        if (remainingCount == 1) {
            val stat = Stats[committed[index]] ?: return emptyList()
            val current = (result as? TargetResult.Success)?.item?.getStatValue(stat)

            return if (current != null) listOf(current.toString()) + AMOUNT_COMPLETIONS else AMOUNT_COMPLETIONS
        }

        val statIds = when (result) {
            is TargetResult.Success -> result.item.statsToTrack.map { it.id }
            is TargetResult.Failure -> Stats.values().map { it.id }
        }

        return if (committed.isEmpty()) Bukkit.getOnlinePlayers().map { it.name } + statIds else statIds
    }
}
