package com.willfp.stattrackers.display

import com.willfp.stattrackers.plugin
import com.willfp.stattrackers.stats.statTracker
import com.willfp.stattrackers.stats.trackedStats
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import wiki.ecohub.game.item.FastItemStack
import wiki.ecohub.game.item.fast
import wiki.ecohub.math.NumberUtils
import wiki.ecohub.ui.display.Display
import wiki.ecohub.ui.display.DisplayContext
import wiki.ecohub.ui.display.DisplayModule
import wiki.ecohub.ui.display.DisplayPriority
import wiki.ecohub.ui.display.lore.DisplayLore
import wiki.ecohub.ui.text.toComponent

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

        Registry.ENCHANTMENT.get(NamespacedKey.minecraft("smite"))?.let { meta.addEnchant(it, 1, true) }
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
