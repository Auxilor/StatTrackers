@file:Suppress("UsePropertyAccessSyntax")

package com.willfp.stattrackers.display

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.util.NumberUtils
import com.willfp.stattrackers.stats.statTracker
import com.willfp.stattrackers.stats.trackedStats
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class StatTrackersDisplay(plugin: EcoPlugin) : DisplayModule(plugin, DisplayPriority.HIGH) {
    override fun display(
        itemStack: ItemStack,
        vararg args: Any
    ) {
        val fis = FastItemStack.wrap(itemStack)

        if (!displayRegularItem(fis)) {
            displayTracker(itemStack, fis)
        }
    }

    private fun displayTracker(
        itemStack: ItemStack,
        fis: FastItemStack
    ) {
        val stat = fis.persistentDataContainer.statTracker ?: return

        val trackerMeta = stat.tracker.itemMeta ?: return

        val meta = itemStack.itemMeta ?: return

        meta.setDisplayName(trackerMeta.displayName)

        if (trackerMeta.hasCustomModelData()) {
            meta.setCustomModelData(trackerMeta.customModelData)
        }

        val lore = mutableListOf<String>()

        lore.addAll(FastItemStack.wrap(stat.tracker).lore)

        meta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        val itemLore = getLore(meta)

        lore.addAll(itemLore)

        meta.lore = lore

        itemStack.itemMeta = meta
    }

    private fun displayRegularItem(fis: FastItemStack): Boolean {
        val pdc = fis.persistentDataContainer

        val stats = pdc.trackedStats
        if (stats.isEmpty()) {
            return false
        }

        val itemLore = fis.lore

        val statLore = mutableListOf<String>()

        for (stat in stats) {
            statLore.add(
                Display.PREFIX + stat.stat.display
                    .replace("%value%", NumberUtils.format(stat.value))
            )
        }

        if (plugin.configYml.getBool("display-at-top")) {
            itemLore.addAll(0, statLore)
        } else {
            itemLore.addAll(statLore)
        }

        fis.lore = itemLore

        return true
    }

    private fun getLore(meta: ItemMeta): MutableList<String> {
        var itemLore: MutableList<String>? = ArrayList()

        if (meta.hasLore()) {
            itemLore = meta.lore
        }

        if (itemLore == null) {
            itemLore = ArrayList()
        }

        return itemLore
    }
}
