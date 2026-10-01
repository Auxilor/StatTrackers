package com.willfp.stattrackers.display

import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayContext
import com.willfp.eco.core.display.DisplayLore
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.toComponent
import com.willfp.stattrackers.plugin
import com.willfp.stattrackers.stats.statTracker
import com.willfp.stattrackers.stats.trackedStats
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@Suppress("DEPRECATION")
object StatTrackersDisplay : DisplayModule(plugin, DisplayPriority.HIGH) {
    override fun display(context: DisplayContext) {
        val fis = context.itemStack.fast()

        if (!displayRegularItem(fis, context.lore)) {
            displayTracker(context.itemStack, fis, context.lore)
        }
    }

    private fun displayTracker(
        itemStack: ItemStack,
        fis: FastItemStack,
        lore: DisplayLore
    ) {
        val stat = fis.persistentDataContainer.statTracker ?: return
        val trackerMeta = stat.tracker.itemMeta ?: return
        val meta = itemStack.itemMeta ?: return

        meta.setDisplayName(trackerMeta.displayName)
        if (trackerMeta.hasCustomModelData()) {
            meta.setCustomModelData(trackerMeta.customModelData)
        }

        meta.addEnchant(Registry.ENCHANTMENT.get(NamespacedKey.minecraft("smite"))!!, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemStack.itemMeta = meta

        lore.prepend(stat.tracker.fast().loreComponents.map { Display.stripDisplayMarker(it) })
    }

    private fun displayRegularItem(
        fis: FastItemStack,
        lore: DisplayLore
    ): Boolean {
        val stats = fis.persistentDataContainer.trackedStats

        if (stats.isEmpty()) {
            return false
        }

        val statLore = stats.map {
            it.stat.display.replace("%value%", NumberUtils.format(it.value)).toComponent()
        }

        if (plugin.configYml.getBool("display-at-top")) {
            lore.prepend(statLore)
        } else {
            lore.append(statLore)
        }

        return true
    }
}
