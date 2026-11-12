package com.willfp.stattrackers.stats

import wiki.ecohub.config.Config
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.core.registry.Registry
import wiki.ecohub.modules.recipe.Recipes

object Stats: ConfigCategory("stat", "stats") {
    private val registry = Registry<Stat>()

    override fun clear(plugin: LibreforgePlugin) {
        for (stat in registry.values()) {
            stat.recipe?.let { Recipes.unregister(it) }
        }
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(Stat(id, config))
    }

    operator fun get(id: String?): Stat? {
        return registry[id ?: return null]
    }

    fun values(): Set<Stat> = registry.values()
}
