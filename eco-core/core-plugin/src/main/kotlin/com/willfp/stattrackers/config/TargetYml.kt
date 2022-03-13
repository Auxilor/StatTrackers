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
        val items = mutableSetOf<TestableItem>()
        this.getStrings(target).forEach { items.add(Items.lookup(it)) }
        return items
    }
}
