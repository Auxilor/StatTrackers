package com.willfp.stattrackers.display

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.StringUtils
import com.willfp.stattrackers.stats.activeStats
import com.willfp.stattrackers.stats.statTracker
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class StatTrackersDisplay(plugin: EcoPlugin) : DisplayModule(plugin, DisplayPriority.HIGHEST) {
    override fun display(
        itemStack: ItemStack,
        vararg args: Any
    ) {
        val meta = itemStack.itemMeta ?: return

        if (!displayItemMeta(meta)) {
            displayTrackerMeta(meta)
        }

        itemStack.itemMeta = meta
    }

    private fun displayTrackerMeta(meta: ItemMeta) {
        val stat = meta.statTracker ?: return

        meta.displayName = plugin.langYml.getFormattedString("tracker")

        val lore: MutableList<String> = ArrayList()

        for (s in plugin.langYml.getFormattedStrings(
            "tracker-description",
            StringUtils.FormatOption.WITHOUT_PLACEHOLDERS
        )) {
            lore.add(Display.PREFIX + StringUtils.format(s.replace("%stat%", stat.color + stat.description)))
        }

        meta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        if (hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }

        val itemLore = getLore(meta)

        lore.addAll(itemLore)

        meta.lore = lore
    }

    private fun displayItemMeta(meta: ItemMeta): Boolean {
        val stats = meta.activeStats
        if (stats.isEmpty()) {
            return false
        }

        val itemLore = getLore(meta)

        for (stat in stats) {
            itemLore.add(
                Display.PREFIX + stat.stat.color + stat.stat.description
                        + plugin.langYml.getFormattedString("delimiter")
                        + NumberUtils.format(stat.value)
            )
        }

        meta.lore = itemLore

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

    companion object {
        private var hideAttributes = false

        @ConfigUpdater
        @JvmStatic
        fun update(plugin: EcoPlugin) {
            hideAttributes = plugin.configYml.getBool("hide-attributes")
        }
    }
}
