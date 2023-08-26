package com.willfp.stattrackers.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.core.drops.DropQueue
import com.willfp.stattrackers.stats.Stats
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

        val receiver = Bukkit.getPlayer(args[0])

        if (receiver == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }

        val stat = Stats[args[1]]

        if (stat == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-stat"))
            return
        }

        val message = plugin.langYml.getMessage("give-success")
            .replace("%stat%", stat.id)
            .replace("%recipient%", receiver.name)

        sender.sendMessage(message)

        DropQueue(receiver)
            .addItem(stat.tracker)
            .forceTelekinesis()
            .push()
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        if (args.size == 1) {
            StringUtil.copyPartialMatches(args[0], Bukkit.getOnlinePlayers().map { it.name }, completions)
        }

        if (args.size == 2) {
            StringUtil.copyPartialMatches(args[1], Stats.values().map { it.id }, completions)
        }

        return completions
    }
}
