package com.willfp.stattrackers.stats;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.stattrackers.stats.stats.StatArrowsShot;
import com.willfp.stattrackers.stats.stats.StatBlocksBroken;
import com.willfp.stattrackers.stats.stats.StatCriticalsDealt;
import com.willfp.stattrackers.stats.stats.StatDamageBlocked;
import com.willfp.stattrackers.stats.stats.StatDamageDealt;
import com.willfp.stattrackers.stats.stats.StatDamageTaken;
import com.willfp.stattrackers.stats.stats.StatDistanceFlown;
import com.willfp.stattrackers.stats.stats.StatDistanceJumped;
import com.willfp.stattrackers.stats.stats.StatDistanceSneaked;
import com.willfp.stattrackers.stats.stats.StatDistanceSprinted;
import com.willfp.stattrackers.stats.stats.StatDistanceTravelled;
import com.willfp.stattrackers.stats.stats.StatExperienceAbsorbed;
import com.willfp.stattrackers.stats.stats.StatHeadshots;
import com.willfp.stattrackers.stats.stats.StatItemDamage;
import com.willfp.stattrackers.stats.stats.StatMobsKilled;
import com.willfp.stattrackers.stats.stats.StatPlayersKilled;
import com.willfp.stattrackers.stats.stats.StatTimesJumped;
import com.willfp.stattrackers.stats.stats.StatBossesKilled;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

@UtilityClass
@SuppressWarnings({"unused", "checkstyle:JavadocVariable"})
public class Stats {
    public static final String CONFIG_LOCATION = "config.";
    public static final String GENERAL_LOCATION = "general-config.";

    private static final BiMap<NamespacedKey, Stat> BY_KEY = HashBiMap.create();

    public static final Stat DAMAGE_DEALT = new StatDamageDealt();
    public static final Stat ARROWS_SHOT = new StatArrowsShot();
    public static final Stat BLOCKS_BROKEN = new StatBlocksBroken();
    public static final Stat DAMAGE_BLOCKED = new StatDamageBlocked();
    public static final Stat DAMAGE_TAKEN = new StatDamageTaken();
    public static final Stat DISTANCE_FLOWN = new StatDistanceFlown();
    public static final Stat DISTANCE_SNEAKED = new StatDistanceSneaked();
    public static final Stat DISTANCE_SPRINTED = new StatDistanceSprinted();
    public static final Stat ITEM_DAMAGE = new StatItemDamage();
    public static final Stat MOBS_KILLED = new StatMobsKilled();
    public static final Stat PLAYERS_KILLED = new StatPlayersKilled();
    public static final Stat TIMES_JUMPED = new StatTimesJumped();
    public static final Stat EXPERIENCE_ABSORBED = new StatExperienceAbsorbed();
    public static final Stat BOSSES_KILLED = new StatBossesKilled();
    public static final Stat DISTANCE_TRAVELLED = new StatDistanceTravelled();
    public static final Stat HEADSHOTS = new StatHeadshots();
    public static final Stat DISTANCE_JUMPED = new StatDistanceJumped();
    public static final Stat CRITICALS_DEALT = new StatCriticalsDealt();

    /**
     * Get all registered {@link Stat}s.
     *
     * @return A list of all {@link Stat}s.
     */
    public static List<Stat> values() {
        return ImmutableList.copyOf(BY_KEY.values());
    }

    /**
     * Get {@link Stat} matching key.
     *
     * @param key The NamespacedKey to search for.
     * @return The matching {@link Stat}, or null if not found.
     */
    public static Stat getByKey(@Nullable final NamespacedKey key) {
        if (key == null) {
            return null;
        }
        return BY_KEY.get(key);
    }

    /**
     * Update all {@link Stat}s.
     */
    @ConfigUpdater
    public static void update() {
        for (Stat stat : new HashSet<>(values())) {
            stat.update();
        }
    }

    /**
     * Add new {@link Stat} to StatTrackers.
     * <p>
     * Only for internal use, stats are automatically added in the constructor.
     *
     * @param stat The {@link Stat} to add.
     */
    public static void addNewStat(@NotNull final Stat stat) {
        BY_KEY.remove(stat.getKey());
        BY_KEY.put(stat.getKey(), stat);
    }

    /**
     * Remove {@link Stat} from StatTrackers.
     *
     * @param stat The {@link Stat} to remove.
     */
    public static void removeStat(@NotNull final Stat stat) {
        BY_KEY.remove(stat.getKey());
    }
}
