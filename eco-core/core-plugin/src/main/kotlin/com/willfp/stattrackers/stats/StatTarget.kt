package com.willfp.stattrackers.stats

import org.bukkit.inventory.ItemStack
import wiki.ecohub.config.Config
import wiki.ecohub.core.registry.KRegistrable
import wiki.ecohub.game.item.Items
import wiki.ecohub.game.item.lookup.EmptyTestableItem
import wiki.ecohub.rules.holder.slot.SlotTypes

class StatTarget(
    config: Config
) : KRegistrable {
    override val id = config.getString("id")

    val slot = SlotTypes[config.getString("slot")]!!

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
