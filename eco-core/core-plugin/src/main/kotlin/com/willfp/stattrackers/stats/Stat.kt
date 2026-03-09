package com.willfp.stattrackers.stats

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.recipe.Recipes
import com.willfp.eco.core.recipe.recipes.CraftingRecipe
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.counters.Counters
import com.willfp.stattrackers.plugin
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.Objects

class Stat(
    override val id: String,
    val config: Config
) : KRegistrable {
    val display = config.getFormattedString("display")

    val tracker: ItemStack = ItemStackBuilder(Items.lookup(this.config.getString("tracker.item")))
        .addLoreLines(this.config.getStrings("tracker.lore").map { Display.PREFIX + it })
        .setDisplayName(this.config.getString("tracker.name"))
        .writeMetaKey(plugin.namespacedKeyFactory.create("stat_tracker"), PersistentDataType.STRING, this.id)
        .build()

    val trackerCustomItem = CustomItem(
        plugin.createNamespacedKey(id),
        { it.statTracker == this },
        this.tracker
    ).apply { register() }

    val recipe: CraftingRecipe? = config.getBool("tracker.craftable")
        .takeIf { it }
        ?.let {
            val recipeStrings = config.getStrings("tracker.recipe")
            if (recipeStrings.isEmpty()) return@let null

            Recipes.createAndRegisterRecipe(
                plugin,
                id,
                tracker,
                recipeStrings,
                config.getStringOrNull("tracker.recipe-permission"),
                config.getBool("tracker.shapeless")
            )
        }

    val targets = config.getStrings("applicable-to")
        .mapNotNull { StatTargets[it] }

    val counters = config.getSubsections("counters")
        .mapNotNull { Counters.compile(it, ViolationContext(plugin, "stat $id counters")) }

    fun canPutOn(itemStack: ItemStack): Boolean {
        return targets.any { it.matches(itemStack) }
    }

    override fun onRegister() {
        val accumulator = StatAccumulator(this)

        for (counter in counters) {
            counter.bind(accumulator)
        }
    }

    override fun onRemove() {
        for (counter in counters) {
            counter.unbind()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Stat) {
            return false
        }

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }

    override fun toString(): String {
        return "Stat(id='$id')"
    }
}
