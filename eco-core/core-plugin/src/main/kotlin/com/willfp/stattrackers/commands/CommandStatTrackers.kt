package com.willfp.stattrackers.commands

import com.willfp.stattrackers.StatTrackersPlugin
import com.willfp.stattrackers.stats.StatTrackersGUI
import com.willfp.stattrackers.stats.canTrackStats
import org.bukkit.entity.Player
import wiki.ecohub.core.command.command

/**
 * The GUI needs a player; the subcommands can also run from the console.
 */
internal fun StatTrackersPlugin.statTrackersCommand() = command(
    name = "stattrackers",
    permission = "stattrackers.command.stattrackers"
) {
    reloadCommand()
    giveCommand()
    statsCommand()
    setCommand()
    addCommand()
    subtractCommand()

    executes {
        val player = sender as? Player ?: fail("not-player")

        if (!player.inventory.itemInMainHand.canTrackStats) {
            fail("item-cannot-have-trackers")
        }

        StatTrackersGUI.open(player)
    }
}
