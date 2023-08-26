package com.willfp.stattrackers

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.stattrackers.commands.CommandStatTrackers
import com.willfp.stattrackers.config.TargetsYml
import com.willfp.stattrackers.display.StatTrackersDisplay
import com.willfp.stattrackers.util.DiscoverRecipeListener
import com.willfp.stattrackers.stats.StatTargets
import com.willfp.stattrackers.stats.Stats
import com.willfp.stattrackers.stats.StatsGUI
import org.bukkit.event.Listener

internal lateinit var plugin: StatTrackersPlugin
    private set

class StatTrackersPlugin : LibreforgePlugin() {
    val targetsYml = TargetsYml(this)

    init {
        plugin = this
    }

    override fun handleReload() {
        StatTargets.update(this)
        StatsGUI.update(this)
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            Stats
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandStatTrackers(this)
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            DiscoverRecipeListener(this)
        )
    }

    override fun createDisplayModule(): DisplayModule {
        return StatTrackersDisplay(this)
    }
}
