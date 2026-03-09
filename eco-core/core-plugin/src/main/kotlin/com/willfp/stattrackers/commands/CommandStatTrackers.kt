package com.willfp.stattrackers.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.stattrackers.plugin
import com.willfp.stattrackers.stats.StatTrackersGUI
import com.willfp.stattrackers.stats.canTrackStats
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CommandStatTrackers : PluginCommand(
    plugin,
    "stattrackers",
    "stattrackers.command.stattrackers",
    true
) {
    init {
        this.addSubcommand(CommandReload)
            .addSubcommand(CommandGive)
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        val player = sender as Player

        val item = player.inventory.itemInMainHand

        if (!item.canTrackStats) {
            player.sendMessage(plugin.langYml.getMessage("item-cannot-have-trackers"))
            return
        }

        StatTrackersGUI.open(player)
    }
}
