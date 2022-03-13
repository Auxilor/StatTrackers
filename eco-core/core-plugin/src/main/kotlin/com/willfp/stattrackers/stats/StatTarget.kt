package com.willfp.stattrackers.stats

import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
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
}
