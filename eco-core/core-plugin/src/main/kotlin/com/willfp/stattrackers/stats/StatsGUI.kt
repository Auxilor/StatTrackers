package com.willfp.stattrackers.stats

import com.willfp.eco.core.config.updating.ConfigUpdater
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

    @JvmStatic
    @ConfigUpdater
    fun update(plugin: StatTrackersPlugin) {
        val rows = plugin.configYml.getInt("gui.rows")

        gui = menu(rows) {
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

            onOpen { player, menu ->
                val type = player.inventory.itemInMainHand.type
                menu.addState(player, "item", type)
            }

            onRender { player, menu ->
                if (menu.getState<Material>(player, "item") != player.inventory.itemInMainHand.type) {
                    menu.addState(player, "noreturn", true)
                    player.closeInventory()
                }
            }

            setTitle(plugin.configYml.getFormattedString("gui.title"))

            onClose { event, menu ->
                val player = event.player as Player
                val captive = menu.getCaptiveItems(player)

                if (menu.getState<Boolean>(player, "noreturn") == true) {
                    return@onClose
                }

                val item = player.inventory.itemInMainHand

                val toApply = mutableListOf<Stat>()
                val toReturn = mutableListOf<ItemStack>()

                for (itemStack in captive) {
                    val meta = itemStack.itemMeta

                    val stat = meta.statTracker
                    if (stat == null || meta == null) {
                        toReturn.add(itemStack)
                        continue
                    }

                    if (stat.targets.none { it.matches(item) }) {
                        player.sendMessage(
                            plugin.langYml.getMessage("cannot-apply")
                                .replace("%tracker%", meta.displayName)
                        )
                        toReturn.add(itemStack)
                        continue
                    }

                    toApply.add(stat)
                }

                item.statsToTrack = toApply

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
