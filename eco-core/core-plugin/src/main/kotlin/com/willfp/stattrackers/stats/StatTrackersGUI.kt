package com.willfp.stattrackers.stats

import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.stattrackers.plugin
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack

object StatTrackersGUI : Listener {
    @Volatile
    private lateinit var gui: Menu

    private val openSessions = ConcurrentHashMap<UUID, HeldItemSession>()

    private class HeldItemSession(
        val slot: Int,
        val type: Material,
        val stats: Set<Stat>
    )

    internal fun update() {
        val rows = plugin.configYml.getInt("gui.rows")

        gui = menu(rows) {
            title = plugin.configYml.getFormattedString("gui.title")

            for (row in 1..rows) {
                for (column in (1..9)) {
                    setSlot(row, column, slot({ player, _ ->
                        val toTrack = player.inventory.itemInMainHand.statsToTrack
                        toTrack.toList().getOrNull(((row - 1) * 9) + (column - 1))?.tracker?.clone() ?: ItemStack(Material.AIR)
                    }) {
                        setCaptive(true)
                    })
                }
            }

            onClose { event, menu ->
                val player = event.player as Player
                val captive = menu.getCaptiveItems(player)

                val itemInHand = player.inventory.itemInMainHand

                val session = openSessions.remove(player.uniqueId)

                if (session == null || !session.matches(player)) {
                    DropQueue(player)
                        .addItems(captive.filter { it.itemMeta?.statTracker !in (session?.stats ?: emptySet()) })
                        .forceTelekinesis()
                        .push()
                    return@onClose
                }

                val toApply = mutableListOf<Stat>()
                val toReturn = mutableListOf<ItemStack>()

                for (trackerStack in captive) {
                    val meta = trackerStack.itemMeta
                    if (meta == null) {
                        toReturn.add(trackerStack)
                        continue
                    }

                    val stat = meta.statTracker
                    if (stat == null) {
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

    private fun HeldItemSession.matches(player: Player): Boolean {
        val itemInHand = player.inventory.itemInMainHand

        return player.inventory.heldItemSlot == slot
                && itemInHand.type == type
                && itemInHand.canTrackStats
                && itemInHand.statsToTrack.toSet() == stats
    }

    fun open(player: Player) {
        openSessions[player.uniqueId] = HeldItemSession(
            player.inventory.heldItemSlot,
            player.inventory.itemInMainHand.type,
            player.inventory.itemInMainHand.statsToTrack.toSet()
        )

        gui.open(player)
    }

    @EventHandler(ignoreCancelled = true)
    fun preventHeldItemClick(event: InventoryClickEvent) {
        val session = openSessions[event.whoClicked.uniqueId] ?: return

        if ((event.clickedInventory == event.view.bottomInventory && event.slot == session.slot)
            || (event.click == ClickType.NUMBER_KEY && event.hotbarButton == session.slot)
        ) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun preventHeldItemDrag(event: InventoryDragEvent) {
        val session = openSessions[event.whoClicked.uniqueId] ?: return

        if (event.rawSlots.any { event.view.getInventory(it) == event.view.bottomInventory && event.view.convertSlot(it) == session.slot }) {
            event.isCancelled = true
        }
    }
}
