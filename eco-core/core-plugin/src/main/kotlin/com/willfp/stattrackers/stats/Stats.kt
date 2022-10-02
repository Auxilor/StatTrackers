package com.willfp.stattrackers.stats

import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableList
import com.willfp.stattrackers.stats.stats.*

@Suppress("UNUSED")
object Stats {
    private val BY_ID = HashBiMap.create<String, Stat>()

    val ARROWS_SHOT: Stat = StatArrowsShot()
    val BLOCKS_BROKEN: Stat = StatBlocksBroken()
    val BOSSES_KILLED: Stat = StatBossesKilled()
    val CRITICALS_DEALT: Stat = StatCriticalsDealt()
    val DAMAGE_BLOCKED: Stat = StatDamageBlocked()
    val DAMAGE_DEALT: Stat = StatDamageDealt()
    val DAMAGE_TAKEN: Stat = StatDamageTaken()
    val DISTANCE_FLOWN: Stat = StatDistanceFlown()
    val DISTANCE_JUMPED: Stat = StatDistanceJumped()
    val DISTANCE_SNEAKED: Stat = StatDistanceSneaked()
    val DISTANCE_SPRINTED: Stat = StatDistanceSprinted()
    val DISTANCE_TRAVELLED: Stat = StatDistanceTravelled()
    val EXPERIENCE_ABSORBED: Stat = StatExperienceAbsorbed()
    val HEADSHOTS: Stat = StatHeadshots()
    val ITEM_DAMAGE: Stat = StatItemDamage()
    val MOBS_KILLED: Stat = StatMobsKilled()
    val PLAYERS_KILLED: Stat = StatPlayersKilled()
    val TIMES_JUMPED: Stat = StatTimesJumped()
    val FISH_CAUGHT: Stat = StatFishCaught()

    /**
     * Get stat matching id.
     *
     * @param id The id to query.
     * @return The matching stat, or null if not found.
     */
    @JvmStatic
    fun getByID(id: String): Stat? {
        return BY_ID[id]
    }

    /**
     * List of all registered stats.
     *
     * @return The stats.
     */
    @JvmStatic
    fun values(): List<Stat> {
        return ImmutableList.copyOf(BY_ID.values)
    }

    /**
     * Add new stat.
     *
     * @param stat The stat to add.
     */
    @JvmStatic
    fun addNewStat(stat: Stat) {
        BY_ID.remove(stat.id)
        BY_ID[stat.id] = stat
    }
}
