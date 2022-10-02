package com.willfp.stattrackers

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.display.DisplayModule
import com.willfp.stattrackers.commands.CommandStatTrackers
import com.willfp.stattrackers.config.TargetYml
import com.willfp.stattrackers.display.StatTrackersDisplay
import com.willfp.stattrackers.stats.DiscoverRecipeListener
import com.willfp.stattrackers.stats.Stats
import org.bukkit.event.Listener

class StatTrackersPlugin : EcoPlugin(623, 10261, "&d", true) {
    val targetYml = TargetYml(this)

    init {
        instance = this
    }

    override fun handleEnable() {
        logger.info(Stats.values().size.toString() + " Stats Loaded")

        for (stat in Stats.values()) {
            eventManager.registerListener(stat)
        }
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

    override fun getMinimumEcoVersion(): String {
        return "6.43.0"
    }

    companion object {
        /**
         * Instance of the plugin.
         */
        lateinit var instance: StatTrackersPlugin
            private set
    }
}