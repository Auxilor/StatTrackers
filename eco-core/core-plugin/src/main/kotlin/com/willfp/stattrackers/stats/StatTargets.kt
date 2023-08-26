package com.willfp.stattrackers.stats

import com.willfp.eco.core.registry.Registry
import com.willfp.stattrackers.StatTrackersPlugin
import com.willfp.stattrackers.plugin
import org.bukkit.inventory.ItemStack

object StatTargets: Registry<StatTarget>() {
    init {
        update(plugin)
    }

    internal fun update(plugin: StatTrackersPlugin) {
        clear()

        for (config in plugin.targetsYml.getSubsections("targets")) {
            register(StatTarget(config))
        }
    }
}
