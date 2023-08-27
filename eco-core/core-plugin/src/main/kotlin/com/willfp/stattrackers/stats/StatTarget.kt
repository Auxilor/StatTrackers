package com.willfp.stattrackers.stats

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.libreforge.slot.SlotTypes
import org.bukkit.inventory.ItemStack

class StatTarget(
    config: Config
) : KRegistrable {
    override val id = config.getString("id")

    val slot = SlotTypes[config.getString("slot")]

    val items = config.getStrings("items")
        .map { Items.lookup(it) }
        .filterNot { it is EmptyTestableItem }


    fun matches(itemStack: ItemStack): Boolean {
        return items.any { it.matches(itemStack) }
    }

    override fun toString(): String {
        return "StatTarget(id='$id')"
    }

    override fun equals(other: Any?): Boolean {
        return other is StatTarget && this.id == other.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}
