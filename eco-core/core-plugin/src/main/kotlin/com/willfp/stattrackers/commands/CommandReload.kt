package com.willfp.stattrackers.commands

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.stattrackers.plugin
import com.willfp.stattrackers.stats.Stats
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.plugin.startup.Prerequisite
import wiki.ecohub.core.stdlib.toNiceString
import wiki.ecohub.ui.text.StringUtils

object CommandReload : Subcommand(
    plugin,
    "reload",
    "stattrackers.command.reload",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        if (Prerequisite.HAS_FOLIA.isMet && !Bukkit.isGlobalTickThread()) {
            plugin.scheduler.global().run { onExecute(sender, args) }
            return
        }

        sender.sendMessage(
            plugin.langYml.getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%time%", plugin.reloadWithTime().toNiceString())
                .replace("%count%", Stats.values().size.toString())
        )
    }
}
