package com.willfp.stattrackers.config

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.StaticBaseConfig
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem

class TargetYml(plugin: EcoPlugin) : StaticBaseConfig("target", plugin, ConfigType.YAML) {
    val targets: List<String>
        get() = getKeys(false)

    fun getTargetItems(target: String): Set<TestableItem> {
        return this.getStrings(target).map { Items.lookup(it) }.toSet()
    }
}
