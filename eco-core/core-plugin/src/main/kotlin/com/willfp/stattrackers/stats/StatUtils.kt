@file:JvmName("StatUtils")

package com.willfp.stattrackers.stats

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.FastItemStack
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
    return Stats.getByID(id) ?: return null
}

var ItemStack?.trackedStats: Collection<TrackedStat>
    get() {
        this ?: return emptyList()
        return FastItemStack.wrap(this).persistentDataContainer.trackedStats
    }
    set(value) {
        this ?: return
        FastItemStack.wrap(this).persistentDataContainer.trackedStats = value
    }

var ItemMeta?.trackedStats: Collection<TrackedStat>
    get() {
        val container = this?.persistentDataContainer ?: return emptyList()

        return container.trackedStats
    }
    set(value) {
        val container = this?.persistentDataContainer ?: return

        container.trackedStats = value
    }

var PersistentDataContainer.trackedStats: Collection<TrackedStat>
    get() {
        this.migrateFromLegacy()

        return this.get(trackedStatsKey, PersistentDataType.TAG_CONTAINER_ARRAY)
            ?.filterNotNull()?.mapNotNull { it.toTrackedStat() } ?: emptyList()
    }
    set(value) {
        this.migrateFromLegacy()

        val filtered = value.distinctBy { it.stat }

        this.set(
            trackedStatsKey,
            PersistentDataType.TAG_CONTAINER_ARRAY,
            filtered.map { it.toNBTTag(this.adapterContext) }.toTypedArray()
        )
    }

var ItemStack?.statsToTrack: Collection<Stat>
    get() {
        this ?: return emptyList()
        return FastItemStack.wrap(this).persistentDataContainer.statsToTrack
    }
    set(value) {
        this ?: return
        FastItemStack.wrap(this).persistentDataContainer.statsToTrack = value
    }

var ItemMeta?.statsToTrack: Collection<Stat>
    get() {
        val container = this?.persistentDataContainer ?: return emptyList()

        return container.statsToTrack
    }
    set(value) {
        val container = this?.persistentDataContainer ?: return

        container.statsToTrack = value
    }

var PersistentDataContainer.statsToTrack: Collection<Stat>
    get() {
        return this.get(statsToTrackKey, PersistentDataType.TAG_CONTAINER_ARRAY)
            ?.filterNotNull()?.mapNotNull { it.getStat() } ?: emptyList()
    }
    set(value) {
        this.trackedStats = this.trackedStats.filter { it.stat in value }
        val trackedStats = this.trackedStats
        val missing = value.toMutableList().apply { removeIf { stat -> trackedStats.any { it.stat == stat } } }
        for (stat in missing) {
            this.addTrackedStat(TrackedStat(stat, 0.0))
        }

        this.set(
            statsToTrackKey,
            PersistentDataType.TAG_CONTAINER_ARRAY,
            value.map { it.toNBTTag(this.adapterContext) }.toTypedArray()
        )
    }

fun ItemStack?.getStatValue(stat: Stat): Double =
    this?.let { FastItemStack.wrap(it).persistentDataContainer.getStatValue(stat) } ?: 0.0

fun ItemMeta?.getStatValue(stat: Stat): Double {
    val active = this.trackedStats
    return active.firstOrNull { it.stat == stat }?.value ?: 0.0
}
fun PersistentDataContainer.getStatValue(stat: Stat): Double {
    val active = this.trackedStats
    return active.firstOrNull { it.stat == stat }?.value ?: 0.0
}

fun ItemStack?.incrementIfToTrack(stat: Stat, amount: Double) {
    this ?: return
    FastItemStack.wrap(this).persistentDataContainer.incrementIfToTrack(stat, amount)
}

fun ItemMeta?.incrementIfToTrack(stat: Stat, amount: Double) {
    if (!this.statsToTrack.contains(stat)) {
        return
    }

    this.setStatValue(stat, this.getStatValue(stat) + amount)
}

fun PersistentDataContainer.incrementIfToTrack(stat: Stat, amount: Double) {
    if (!this.statsToTrack.contains(stat)) {
        return
    }

    this.setStatValue(stat, this.getStatValue(stat) + amount)
}

fun ItemStack?.setStatValue(stat: Stat, value: Double) {
    this ?: return
    FastItemStack.wrap(this).persistentDataContainer.setStatValue(stat, value)
}

fun ItemMeta?.setStatValue(stat: Stat, value: Double) =
    this.addTrackedStat(TrackedStat(stat, value))

fun PersistentDataContainer.setStatValue(stat: Stat, value: Double) =
    this.addTrackedStat(TrackedStat(stat, value))

fun ItemStack?.addTrackedStat(stat: TrackedStat) {
    this ?: return
    FastItemStack.wrap(this).persistentDataContainer.addTrackedStat(stat)
}

fun ItemMeta?.addTrackedStat(stat: TrackedStat) {
    this.trackedStats = this.trackedStats.toMutableList().apply {
        removeIf { it.stat == stat.stat }
        add(stat)
    }
}

fun PersistentDataContainer.addTrackedStat(stat: TrackedStat) {
    this.trackedStats = this.trackedStats.toMutableList().apply {
        removeIf { it.stat == stat.stat }
        add(stat)
    }
}

var ItemStack?.statTracker: Stat?
    get() {
        this ?: return null
        return FastItemStack.wrap(this).persistentDataContainer.statTracker
    }
    set(value) {
        this ?: return
        FastItemStack.wrap(this).persistentDataContainer.statTracker = value
    }

var ItemMeta?.statTracker: Stat?
    get() {
        val container = this?.persistentDataContainer ?: return null
        return container.statTracker
    }
    set(value) {
        val container = this?.persistentDataContainer ?: return
        container.statTracker = value
    }

var PersistentDataContainer.statTracker: Stat?
    get() {
        val tracker = this.get(trackerKey, PersistentDataType.STRING) ?: return null
        return Stats.getByID(tracker)
    }
    set(value) {
        if (value == null) {
            this.remove(trackerKey)
        } else {
            this.set(trackerKey, PersistentDataType.STRING, value.id)
        }
    }

fun PersistentDataContainer.migrateFromLegacy() {
    val statKey = this.get(legacyActiveKey, PersistentDataType.STRING) ?: return
    val key = safeNamespacedKeyOf(statKey) ?: return
    val stat = Stats.getByID(key.key) ?: return
    val value = this.get(key, PersistentDataType.DOUBLE) ?: return
    this.remove(legacyActiveKey)
    this.remove(key)

    this.statsToTrack = listOf(stat)
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
