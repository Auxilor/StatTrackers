package com.willfp.stattrackers.stats

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.recipe.Recipes
import com.willfp.eco.core.recipe.recipes.CraftingRecipe
import com.willfp.stattrackers.StatTrackersPlugin
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

abstract class Stat(
    val id: String
) : Listener {
    private val plugin: EcoPlugin = StatTrackersPlugin.instance

    val key: NamespacedKey = this.plugin.namespacedKeyFactory.create(id)

    val config: Config = this.plugin.configYml.getSubsection("stat." + this.id)

    lateinit var description: String
        private set

    lateinit var color: String
        private set

    lateinit var tracker: ItemStack
        private set

    lateinit var trackerCustomItem: CustomItem
        private set

    var recipe: CraftingRecipe? = null
        private set

    init {
        register()
        update()
    }

    private fun register() {
        Stats.addNewStat(this)
    }

    private fun update() {
        description = this.config.getFormattedString("name")
        color = this.config.getFormattedString("color")
        tracker = ItemStackBuilder(Items.lookup(this.config.getString("tracker.item")))
            .addLoreLines(this.config.getStrings("tracker.lore"))
            .setDisplayName(this.config.getString("tracker.name"))
            .writeMetaKey(plugin.namespacedKeyFactory.create("stat_tracker"), PersistentDataType.STRING, this.id)
            .build()

        trackerCustomItem = CustomItem(
            this.key,
            { it.statTracker == this },
            this.tracker
        ).apply { register() }

        recipe = Recipes.createAndRegisterRecipe(
            plugin,
            this.id,
            this.tracker,
            this.config.getStrings("tracker.recipe"),
            this.config.getStringOrNull("tracker.recipe-permission")
        )
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
        return ("Stat{"
                + this.key
                ) + "}"
    }
}

data class TrackedStat(
    val stat: Stat,
    var value: Double
)
