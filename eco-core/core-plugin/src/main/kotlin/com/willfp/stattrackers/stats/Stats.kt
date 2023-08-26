package com.willfp.stattrackers.stats

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registry
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory

object Stats: ConfigCategory("stat", "stats") {
    private val registry = Registry<Stat>()

    override fun clear(plugin: LibreforgePlugin) {
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(Stat(id, config, plugin))
    }

    operator fun get(id: String?): Stat? {
        return registry[id ?: return null]
    }

    fun values(): Set<Stat> = registry.values()
}
