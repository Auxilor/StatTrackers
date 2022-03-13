@file:JvmName("StatUtils")

package com.willfp.stattrackers.stats

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.util.safeNamespacedKeyOf
import com.willfp.stattrackers.StatTrackersPlugin
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Tameable
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val plugin: EcoPlugin = StatTrackersPlugin.instance
private val legacyActiveKey = plugin.namespacedKeyFactory.create("active")
private val trackedStatsKey = plugin.namespacedKeyFactory.create("tracked_stats")
private val statId = plugin.namespacedKeyFactory.create("stat_id")
private val statValue = plugin.namespacedKeyFactory.create("stat_value")
private val trackerKey = plugin.namespacedKeyFactory.create("stat_tracker")

private fun TrackedStat.toNBTTag(context: PersistentDataAdapterContext): PersistentDataContainer {
    val container = context.newPersistentDataContainer()
    container.set(statId, PersistentDataType.STRING, this.stat.id)
    container.set(statValue, PersistentDataType.DOUBLE, this.value)
    return container
}

private fun PersistentDataContainer.toTrackedStat(): TrackedStat? {
    val id = this.get(statId, PersistentDataType.STRING) ?: return null
    val value = this.get(statValue, PersistentDataType.DOUBLE) ?: return null
    val stat = Stats.getByID(id) ?: return null
    return TrackedStat(stat, value)
}

var ItemStack?.activeStats: Collection<TrackedStat>
    get() = this?.itemMeta?.activeStats ?: emptyList()
    set(value) {
        this?.itemMeta?.activeStats = value
    }

var ItemMeta?.activeStats: Collection<TrackedStat>
    get() {
        val container = this?.persistentDataContainer ?: return emptyList()

        val tracked = container.get(trackedStatsKey, PersistentDataType.TAG_CONTAINER_ARRAY)
            ?.filterNotNull()?.toMutableList() ?: mutableListOf()

        return tracked.mapNotNull { it.toTrackedStat() }
    }
    set(value) {
        val container = this?.persistentDataContainer ?: return

        val filtered = value.distinctBy { it.stat }

        container.set(
            trackedStatsKey,
            PersistentDataType.TAG_CONTAINER_ARRAY,
            filtered.map { it.toNBTTag(container.adapterContext) }.toTypedArray()
        )
    }

fun ItemStack?.getStatValue(stat: Stat): Double =
    this?.itemMeta?.getStatValue(stat) ?: 0.0

fun ItemMeta?.getStatValue(stat: Stat): Double {
    val active = this.activeStats
    return active.firstOrNull { it.stat == stat }?.value ?: 0.0
}

fun ItemStack?.incrementStatValue(stat: Stat, amount: Double) {
    this.setStatValue(stat, this.getStatValue(stat) + amount)
}

fun ItemStack?.setStatValue(stat: Stat, value: Double) =
    this?.itemMeta?.setStatValue(stat, value)

fun ItemMeta?.setStatValue(stat: Stat, value: Double) =
    this.addActiveStat(TrackedStat(stat, value))

fun ItemStack?.addActiveStat(stat: TrackedStat) =
    this?.itemMeta?.addActiveStat(stat)

fun ItemMeta?.addActiveStat(stat: TrackedStat) {
    this.activeStats = this.activeStats.toMutableList().apply {
        removeIf { it.stat == stat.stat }
        add(stat)
    }
}

var ItemStack?.statTracker: Stat?
    get() = this?.itemMeta?.statTracker
    set(value) {
        this?.itemMeta?.statTracker = value
    }

var ItemMeta?.statTracker: Stat?
    get() {
        val container = this?.persistentDataContainer ?: return null
        val tracker = container.get(trackerKey, PersistentDataType.STRING) ?: return null
        return Stats.getByID(tracker)
    }
    set(value) {
        val container = this?.persistentDataContainer ?: return
        if (value == null) {
            container.remove(trackerKey)
        } else {
            container.set(trackerKey, PersistentDataType.STRING, value.id)
        }
    }

fun ItemMeta.migrateFromLegacy() {
    val container = this.persistentDataContainer ?: return
    val statKey = container.get(legacyActiveKey, PersistentDataType.STRING) ?: return
    val key = safeNamespacedKeyOf(statKey) ?: return
    val stat = Stats.getByID(key.key) ?: return
    val value = container.get(key, PersistentDataType.DOUBLE) ?: return
    container.remove(legacyActiveKey)
    container.remove(key)

    this.addActiveStat(TrackedStat(stat, value))
}

fun Entity?.tryAsPlayer(): Player? {
    return when (this) {
        is Projectile -> this.shooter as? Player
        is Player -> this
        is Tameable -> this.owner as? Player
        else -> null
    }
}
