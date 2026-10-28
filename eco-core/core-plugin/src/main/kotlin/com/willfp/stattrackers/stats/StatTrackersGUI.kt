package com.willfp.stattrackers.stats

import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.fast.fast
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

private const val SLOTS_PER_ROW = 9

object StatTrackersGUI : Listener {
    @Volatile
    private lateinit var gui: Menu

    private val openSessions = ConcurrentHashMap<UUID, HeldItemSession>()

    private class HeldItemSession(
        val slot: Int,
        val type: Material,
        val stats: Set<Stat>,
        val hiddenStats: Set<Stat>,
        val shownValues: Map<Stat, Double>
    )

    internal fun update() {
        val rows = plugin.configYml.getInt("gui.rows")

        gui = menu(rows) {
            title = plugin.configYml.getFormattedString("gui.title")

            for (row in 1..rows) {
                for (column in (1..9)) {
                    setSlot(row, column, slot({ player, _ ->
                        val itemInHand = player.inventory.itemInMainHand
                        val stat = itemInHand.statsToTrack.toList().getOrNull(((row - 1) * 9) + (column - 1))
                        stat?.run {
                            tracker.clone().withValue(itemInHand.getStatValue(this))
                        } ?: ItemStack(Material.AIR)
                    }) {
                        setCaptive(true)
                    })
                }
            }

            onClose { event, menu ->
                val player = event.player as Player
                applyTrackers(player, menu.getCaptiveItems(player))
            }
        }
    }

    private fun applyTrackers(player: Player, captive: List<ItemStack>) {
        val itemInHand = player.inventory.itemInMainHand

        val session = openSessions.remove(player.uniqueId)

        if (session == null || !session.matches(player)) {
            DropQueue(player)
                .addItems(captive.filter { it.itemMeta?.statTracker !in (session?.stats ?: emptySet()) })
                .forceTelekinesis()
                .push()
            return
        }

        val toApply = linkedSetOf<Stat>()
        val carriedValues = mutableMapOf<Stat, Double>()
        val toReturn = mutableListOf<ItemStack>()

        for (trackerStack in captive) {
            val stat = trackerStack.itemMeta?.statTracker

            when {
                stat == null || stat in toApply || stat in session.hiddenStats -> {
                    toReturn.add(trackerStack)
                }
                !stat.canPutOn(itemInHand) -> {
                    player.sendMessage(
                        plugin.langYml.getMessage("cannot-apply")
                            .replace("%tracker%", trackerStack.fast().displayName)
                    )
                    toReturn.add(trackerStack)
                }
                else -> {
                    if (trackerStack.amount > 1) {
                        toReturn.add(trackerStack.clone().apply { amount -= 1 })
                    }
                    toApply.add(stat)
                    if (session.shownValues[stat] != trackerStack.trackerValue) {
                        carriedValues[stat] = trackerStack.trackerValue
                    }
                }
            }
        }

        toApply.addAll(session.hiddenStats)

        itemInHand.statsToTrack = toApply

        for ((stat, value) in carriedValues) {
            itemInHand.setStatValue(stat, value)
        }

        DropQueue(player)
            .addItems(toReturn)
            .forceTelekinesis()
            .push()
    }

    private fun ItemStack.withValue(value: Double): ItemStack {
        if (value != 0.0) {
            this.trackerValue = value
        }

        return this
    }

    private fun HeldItemSession.matches(player: Player): Boolean {
        val itemInHand = player.inventory.itemInMainHand

        return player.inventory.heldItemSlot == slot
                && itemInHand.type == type
                && itemInHand.canTrackStats
                && itemInHand.statsToTrack.toSet() == stats
    }

    fun open(player: Player) {
        val itemInHand = player.inventory.itemInMainHand
        val stats = itemInHand.statsToTrack

        openSessions[player.uniqueId] = HeldItemSession(
            slot = player.inventory.heldItemSlot,
            type = itemInHand.type,
            stats = stats.toSet(),
            hiddenStats = stats.drop(plugin.configYml.getInt("gui.rows") * SLOTS_PER_ROW).toSet(),
            shownValues = stats.associateWith { itemInHand.getStatValue(it) }
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
