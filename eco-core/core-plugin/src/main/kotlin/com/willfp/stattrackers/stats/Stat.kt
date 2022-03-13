package com.willfp.stattrackers.stats

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.Prerequisite
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

    val key: NamespacedKey

    val config: Config

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

    /**
     * Create a new Stat.
     *
     * @param key           The key name of the stat.
     * @param prerequisites Optional [Prerequisite]s that must be met.
     */
    init {
        this.key = this.plugin.namespacedKeyFactory.create(id)
        config = this.plugin.configYml.getSubsection("stat." + this.id)
        register()
        update()
    }

    private fun register() {
        Stats.addNewStat(this)
    }

    /**
     * Update the stat based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    fun update() {
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

        postUpdate()
    }

    protected fun postUpdate() {
        // Unused as some stats may have postUpdate tasks, however most won't.
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
