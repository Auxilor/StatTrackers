package com.willfp.stattrackers.stats

import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.stattrackers.StatTrackersPlugin

object StatTargets {
    private val ALL = StatTarget("all", mutableSetOf())

    private val REGISTERED: MutableMap<String, StatTarget> = HashMap()

    init {
        REGISTERED["all"] = ALL
        update(StatTrackersPlugin.instance)
    }

    fun getByName(name: String): StatTarget? {
        return REGISTERED[name]
    }

    @ConfigUpdater
    @JvmStatic
    fun update(plugin: StatTrackersPlugin) {
        ALL.items.clear()
        for (id in ArrayList(REGISTERED.keys)) {
            if (id.equals("all", ignoreCase = true)) {
                continue
            }
            REGISTERED.remove(id)
        }
        for (id in plugin.targetYml.targets) {
            val target = StatTarget(
                id,
                plugin.targetYml.getTargetItems(id).toMutableSet()
            )
            REGISTERED[id] = target
            ALL.items.addAll(target.items)
        }
    }

    fun values(): Set<StatTarget> {
        return REGISTERED.values.toSet()
    }
}
