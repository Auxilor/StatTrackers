package com.willfp.stattrackers.stats

import com.willfp.eco.core.registry.Registry
import com.willfp.libreforge.slot.SlotTypes
import com.willfp.stattrackers.plugin

object StatTargets: Registry<StatTarget>() {
    init {
        update()
    }

    internal fun update() {
        clear()

        val slotOptions = SlotTypes.values().map { it.id }.filterNot { id -> id.all { it.isDigit() } }

        for (config in plugin.targetsYml.getSubsections("targets")) {
            if (SlotTypes[config.getString("slot")] == null) {
                plugin.logger.warning(
                    "Skipping target ${config.getString("id")}: invalid slot ${config.getString("slot")}, " +
                            "options are $slotOptions"
                )
                continue
            }

            register(StatTarget(config))
        }
    }
}
