package com.willfp.stattrackers.stats

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.recipe.Recipes
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.counters.Counters
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.Objects

class Stat(
    override val id: String,
    val config: Config,
    plugin: EcoPlugin
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

    val recipe = if (this.config.getBool("tracker.craftable")) {
        Recipes.createAndRegisterRecipe(
            plugin,
            this.id,
            this.tracker,
            this.config.getStrings("tracker.recipe"),
            this.config.getStringOrNull("tracker.recipe-permission")
        )
    } else null

    val targets = config.getStrings("applicable-to")
        .mapNotNull { StatTargets[it] }

    val counters = config.getSubsections("counters")
        .mapNotNull { Counters.compile(it, ViolationContext(plugin, "stat $id counters")) }

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
