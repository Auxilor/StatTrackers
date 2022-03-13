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
private val statsToTrackKey = plugin.namespacedKeyFactory.create("stats_to_track")
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

private fun Stat.toNBTTag(context: PersistentDataAdapterContext): PersistentDataContainer {
    val container = context.newPersistentDataContainer()
    container.set(statId, PersistentDataType.STRING, this.id)
    return container
}

private fun PersistentDataContainer.getStat(): Stat? {
    val id = this.get(statId, PersistentDataType.STRING) ?: return null
    val stat = Stats.getByID(id) ?: return null
    return stat
}

var ItemStack?.trackedStats: Collection<TrackedStat>
    get() = this?.itemMeta?.trackedStats ?: emptyList()
    set(value) {
        val meta = this?.itemMeta ?: return
        meta.trackedStats = value
        this.itemMeta = meta
    }

var ItemMeta?.trackedStats: Collection<TrackedStat>
    get() {
        val container = this?.persistentDataContainer ?: return emptyList()

        return container.get(trackedStatsKey, PersistentDataType.TAG_CONTAINER_ARRAY)
            ?.filterNotNull()?.mapNotNull { it.toTrackedStat() } ?: emptyList()
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

var ItemStack?.statsToTrack: Collection<Stat>
    get() = this?.itemMeta?.statsToTrack ?: emptyList()
    set(value) {
        val meta = this?.itemMeta ?: return
        meta.statsToTrack = value
        this.itemMeta = meta
    }

var ItemMeta?.statsToTrack: Collection<Stat>
    get() {
        val container = this?.persistentDataContainer ?: return emptyList()

        return container.get(statsToTrackKey, PersistentDataType.TAG_CONTAINER_ARRAY)
            ?.filterNotNull()?.mapNotNull { it.getStat() } ?: emptyList()
    }
    set(value) {
        val container = this?.persistentDataContainer ?: return

        this.trackedStats = this.trackedStats.filter { it.stat in value }
        val trackedStats = this.trackedStats
        val missing = value.toMutableList().apply { removeIf { stat -> trackedStats.any { it.stat == stat } } }
        for (stat in missing) {
            this.addTrackedStat(TrackedStat(stat, 0.0))
        }

        container.set(
            statsToTrackKey,
            PersistentDataType.TAG_CONTAINER_ARRAY,
            value.map { it.toNBTTag(container.adapterContext) }.toTypedArray()
        )
    }

fun ItemStack?.getStatValue(stat: Stat): Double =
    this?.itemMeta?.getStatValue(stat) ?: 0.0

fun ItemMeta?.getStatValue(stat: Stat): Double {
    val active = this.trackedStats
    return active.firstOrNull { it.stat == stat }?.value ?: 0.0
}

fun ItemStack?.incrementIfToTrack(stat: Stat, amount: Double) {
    if (!this.statsToTrack.contains(stat)) {
        return
    }

    this.setStatValue(stat, this.getStatValue(stat) + amount)
}

fun ItemStack?.setStatValue(stat: Stat, value: Double) {
    val meta = this?.itemMeta ?: return
    meta.setStatValue(stat, value)
    this.itemMeta = meta
}

fun ItemMeta?.setStatValue(stat: Stat, value: Double) =
    this.addTrackedStat(TrackedStat(stat, value))

fun ItemStack?.addTrackedStat(stat: TrackedStat) {
    val meta = this?.itemMeta ?: return
    meta.addTrackedStat(stat)
    this.itemMeta = meta
}

fun ItemMeta?.addTrackedStat(stat: TrackedStat) {
    this.trackedStats = this.trackedStats.toMutableList().apply {
        removeIf { it.stat == stat.stat }
        add(stat)
    }
}

var ItemStack?.statTracker: Stat?
    get() = this?.itemMeta?.statTracker
    set(value) {
        val meta = this?.itemMeta ?: return
        meta.statTracker = value
        this.itemMeta = meta
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
    val container = this.persistentDataContainer
    val statKey = container.get(legacyActiveKey, PersistentDataType.STRING) ?: return
    val key = safeNamespacedKeyOf(statKey) ?: return
    val stat = Stats.getByID(key.key) ?: return
    val value = container.get(key, PersistentDataType.DOUBLE) ?: return
    container.remove(legacyActiveKey)
    container.remove(key)

    this.addTrackedStat(TrackedStat(stat, value))
}

fun Entity?.tryAsPlayer(): Player? {
    return when (this) {
        is Projectile -> this.shooter as? Player
        is Player -> this
        is Tameable -> this.owner as? Player
        else -> null
    }
}
