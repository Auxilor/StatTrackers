package com.willfp.stattrackers.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.StringUtils
import com.willfp.stattrackers.stats.StatsGUI
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandEdit(plugin: EcoPlugin) : Subcommand(
    plugin,
    "edit",
    "stattrackers.command.edit",
    true
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val player = sender as Player

        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR || item.type.maxStackSize > 1) {
            player.sendMessage(plugin.langYml.getMessage("item-cannot-have-trackers"))
            return
        }

        StatsGUI.open(player)
    }
}
