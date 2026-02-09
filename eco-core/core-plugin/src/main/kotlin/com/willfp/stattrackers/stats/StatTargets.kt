package com.willfp.stattrackers.stats

import com.willfp.eco.core.registry.Registry
import com.willfp.stattrackers.plugin

object StatTargets: Registry<StatTarget>() {
    init {
        update()
    }

    internal fun update() {
        clear()

        for (config in plugin.targetsYml.getSubsections("targets")) {
            register(StatTarget(config))
        }
    }
}
