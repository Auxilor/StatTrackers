package com.willfp.stattrackers.stats

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.display.Display
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

    lateinit var display: String
        private set

    lateinit var tracker: ItemStack
        private set

    lateinit var trackerCustomItem: CustomItem
        private set

    var recipe: CraftingRecipe? = null
        private set

    lateinit var targets: Collection<StatTarget>
        private set

    init {
        register()
        update()
    }

    private fun register() {
        Stats.addNewStat(this)
    }

    private fun update() {
        display = this.config.getFormattedString("display")
        tracker = ItemStackBuilder(Items.lookup(this.config.getString("tracker.item")))
            .addLoreLines(this.config.getStrings("tracker.lore").map { Display.PREFIX + it })
            .setDisplayName(this.config.getString("tracker.name"))
            .writeMetaKey(plugin.namespacedKeyFactory.create("stat_tracker"), PersistentDataType.STRING, this.id)
            .build()

        trackerCustomItem = CustomItem(
            this.key,
            { it.statTracker == this },
            this.tracker
        ).apply { register() }

        targets = this.config.getStrings("applicable-to").mapNotNull { StatTargets.getByName(it) }

        if (this.config.getBool("tracker.craftable")) {
            recipe = Recipes.createAndRegisterRecipe(
                plugin,
                this.id,
                this.tracker,
                this.config.getStrings("tracker.recipe"),
                this.config.getStringOrNull("tracker.recipe-permission")
            )
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
        return ("Stat{"
                + this.key
                ) + "}"
    }
}

data class TrackedStat(
    val stat: Stat,
    var value: Double
)
