package com.willfp.stattrackers

import com.willfp.eco.core.bstats.EcoMetricsChart
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import com.willfp.stattrackers.commands.CommandStatTrackers
import com.willfp.stattrackers.config.TargetsYml
import com.willfp.stattrackers.display.StatTrackersDisplay
import com.willfp.stattrackers.stats.StatTargets
import com.willfp.stattrackers.stats.StatTrackersGUI
import com.willfp.stattrackers.stats.Stats
import com.willfp.stattrackers.util.DiscoverRecipeListener
import org.bukkit.event.Listener

internal lateinit var plugin: StatTrackersPlugin
    private set

class StatTrackersPlugin : LibreforgePlugin() {
    val targetsYml = TargetsYml(this)

    init {
        plugin = this
    }

    override fun handleReload() {
        StatTargets.update()
        StatTrackersGUI.update()
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            Stats
        )
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandStatTrackers
        )
    }

    override fun loadListeners(): List<Listener> {
        return listOf(
            DiscoverRecipeListener
        )
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun createDisplayModule(): DisplayModule {
        return StatTrackersDisplay
    }

    override fun getCustomCharts() = listOf(
        EcoMetricsChart.SingleLine("total_stats") { Stats.values().size },
        EcoMetricsChart.SingleLine("total_stat_targets") { StatTargets.values().size },
        EcoMetricsChart.SimplePie("discover_recipes") {
            if (configYml.getBool("discover-recipes")) "enabled" else "disabled"
        },
        EcoMetricsChart.SimplePie("display_at_top") {
            if (configYml.getBool("display-at-top")) "top" else "bottom"
        }
    )

}
