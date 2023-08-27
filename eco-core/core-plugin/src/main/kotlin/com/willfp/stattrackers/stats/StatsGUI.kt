package com.willfp.stattrackers.stats

import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.stattrackers.StatTrackersPlugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object StatsGUI {
    private lateinit var gui: Menu

    internal fun update(plugin: StatTrackersPlugin) {
        val rows = plugin.configYml.getInt("gui.rows")

        gui = menu(rows) {
            title = plugin.configYml.getFormattedString("gui.title")

            for (row in 1..rows) {
                for (column in (1..9)) {
                    setSlot(row, column, slot({ player, _ ->
                        val toTrack = player.inventory.itemInMainHand.statsToTrack
                        toTrack.toList().getOrNull(column - 1)?.tracker?.clone() ?: ItemStack(Material.AIR)
                    }) {
                        setCaptive(true)
                    })
                }
            }

            onClose { event, menu ->
                val player = event.player as Player
                val captive = menu.getCaptiveItems(player)

                val itemInHand = player.inventory.itemInMainHand

                val toApply = mutableListOf<Stat>()
                val toReturn = mutableListOf<ItemStack>()

                for (trackerStack in captive) {
                    val meta = trackerStack.itemMeta

                    val stat = meta.statTracker
                    if (stat == null || meta == null) {
                        toReturn.add(trackerStack)
                        continue
                    }

                    if (!stat.canPutOn(itemInHand)) {
                        @Suppress("DEPRECATION")
                        player.sendMessage(
                            plugin.langYml.getMessage("cannot-apply")
                                .replace("%tracker%", meta.displayName)
                        )
                        toReturn.add(trackerStack)
                        continue
                    }

                    toApply.add(stat)
                }

                itemInHand.statsToTrack = toApply

                DropQueue(player)
                    .addItems(toReturn)
                    .forceTelekinesis()
                    .push()
            }
        }
    }

    fun open(player: Player) {
        gui.open(player)
    }
}
