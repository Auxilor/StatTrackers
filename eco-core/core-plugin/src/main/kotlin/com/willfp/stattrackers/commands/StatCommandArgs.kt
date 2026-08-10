package com.willfp.stattrackers.commands

import com.willfp.stattrackers.stats.Stats
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object StatCommandArgs {
    val SLOT_COMPLETIONS = listOf("slot:mainhand", "slot:offhand")

    sealed class TargetResult {
        data class Success(
            val player: Player,
            val item: ItemStack,
            val remainingArgs: List<String>
        ) : TargetResult()

        data class Failure(val errorKey: String) : TargetResult()
    }

    /**
     * Resolves an optional leading `[player] [slot:<n>]` from [args] against [sender].
     *
     * - If the first token isn't a `slot:` token and isn't a known stat id, it's treated
     *   as a player name.
     * - Otherwise the target is [sender] (which must be a player).
     * - After the optional player token, a `slot:` token (mainhand/offhand/0-40) is
     *   consumed if present; defaults to the target's main hand.
     */
    fun resolveTarget(sender: CommandSender, args: List<String>): TargetResult {
        var index = 0
        var targetPlayer: Player? = null

        if (index < args.size && !args[index].startsWith("slot:") && Stats[args[index]] == null) {
            targetPlayer = Bukkit.getPlayer(args[index]) ?: return TargetResult.Failure("invalid-player")
            index++
        }

        if (targetPlayer == null) {
            if (sender !is Player) {
                return TargetResult.Failure("not-player")
            }
            targetPlayer = sender
        }

        var slot = targetPlayer.inventory.heldItemSlot

        if (index < args.size && args[index].startsWith("slot:")) {
            val slotArg = args[index].removePrefix("slot:")
            slot = when (slotArg) {
                "mainhand" -> targetPlayer.inventory.heldItemSlot
                "offhand" -> 40
                else -> slotArg.toIntOrNull()?.takeIf { it in 0..40 }
                    ?: return TargetResult.Failure("invalid-slot")
            }
            index++
        }

        val item = targetPlayer.inventory.getItem(slot) ?: ItemStack(Material.AIR)

        return TargetResult.Success(targetPlayer, item, args.drop(index))
    }

    fun tabCompleteTarget(args: List<String>): List<String> {
        val statIds = Stats.values().map { it.id }

        return when (args.size) {
            0, 1 -> Bukkit.getOnlinePlayers().map { it.name } + SLOT_COMPLETIONS + statIds
            2 -> SLOT_COMPLETIONS + statIds
            else -> statIds
        }
    }
}
