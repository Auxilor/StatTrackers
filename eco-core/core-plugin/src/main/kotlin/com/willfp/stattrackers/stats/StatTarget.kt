package com.willfp.stattrackers.stats

import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.stattrackers.StatTrackersPlugin
import org.bukkit.inventory.ItemStack

class StatTarget(
    val name: String,
    val items: MutableSet<TestableItem>
) {
    init {
        items.removeIf { it is EmptyTestableItem }
    }

    fun matches(itemStack: ItemStack): Boolean {
        for (item in items) {
            if (item.matches(itemStack)) {
                return true
            }
        }
        return false
    }

    companion object {
        private val ALL = StatTarget("all", mutableSetOf())

        private val REGISTERED: MutableMap<String, StatTarget> = HashMap()

        init {
            REGISTERED["all"] = ALL
            update(StatTrackersPlugin.instance)
        }

        fun getByName(name: String): StatTarget? {
            return REGISTERED[name]
        }

        fun getForItem(item: ItemStack): List<StatTarget> {
            return REGISTERED.values
                .filter { !it.name.equals("all", ignoreCase = true) }
                .filter { it.matches(item) }
        }

        @JvmStatic
        @ConfigUpdater
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
}
