package com.willfp.stattrackers.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.core.drops.DropQueue
import com.willfp.stattrackers.stats.Stats
import com.willfp.stattrackers.stats.Stats.getByID
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

class CommandGive(plugin: EcoPlugin) : Subcommand(
    plugin,
    "give",
    "stattrackers.command.give",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        if (args.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("needs-player"))
            return
        }

        if (args.size == 1) {
            sender.sendMessage(plugin.langYml.getMessage("needs-stat"))
            return
        }

        val recieverName = args[0]
        val reciever = Bukkit.getPlayer(recieverName)

        if (reciever == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }

        val statID = args[1]
        val stat = getByID(statID)

        if (stat == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-stat"))
            return
        }

        val message = plugin.langYml.getMessage("give-success")
            .replace("%stat%", stat.id)
            .replace("%recipient%", reciever.name)

        sender.sendMessage(message)

        DropQueue(reciever)
            .addItem(stat.tracker)
            .forceTelekinesis()
            .push()
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return Stats.values().map { it.id }
        }

        if (args.size == 1) {
            StringUtil.copyPartialMatches(args[0], Bukkit.getOnlinePlayers().map { it.name }, completions)
            return completions
        }

        if (args.size == 2) {
            StringUtil.copyPartialMatches(args[1], Stats.values().map { it.id }, completions)
            completions.sort()
            return completions
        }

        return emptyList()
    }
}