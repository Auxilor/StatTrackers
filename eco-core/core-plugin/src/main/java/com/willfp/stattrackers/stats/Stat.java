package com.willfp.stattrackers.stats;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.Prerequisite;
import com.willfp.stattrackers.StatTrackersPlugin;
import com.willfp.stattrackers.tracker.StatTracker;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"DeprecatedIsStillUsed", "deprecation"})
public abstract class Stat implements Listener {
    /**
     * Instance of StatTrackers for stats to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final EcoPlugin plugin = StatTrackersPlugin.getInstance();

    /**
     * The key to store stats in meta.
     */
    @Getter
    private final NamespacedKey key;

    /**
     * The legacy key to store stats in meta.
     */
    @Getter
    @Deprecated
    private final NamespacedKey legacyKey;

    /**
     * The description of the stat.
     */
    @Getter
    private String description;

    /**
     * The color of the stat.
     */
    @Getter
    private String color;

    /**
     * The stat tracker item.
     */
    @Getter
    private final StatTracker tracker;

    /**
     * Create a new Stat.
     *
     * @param key           The key name of the stat.
     * @param prerequisites Optional {@link Prerequisite}s that must be met.
     */
    protected Stat(@NotNull final String key,
                   @NotNull final Prerequisite... prerequisites) {
        this.key = this.getPlugin().getNamespacedKeyFactory().create(key);
        this.tracker = new StatTracker(this);
        this.legacyKey = new NamespacedKey("itemstats", key);

        if (!Prerequisite.areMet(prerequisites)) {
            return;
        }

        Stats.addNewStat(this);
        this.update();
    }

    /**
     * Update the stat based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    public void update() {
        description = this.getPlugin().getConfigYml().getString("stat." + this.getKey().getKey() + ".name");
        color = this.getPlugin().getConfigYml().getString("stat." + this.getKey().getKey() + ".color");

        tracker.update();

        postUpdate();
    }

    protected void postUpdate() {
        // Unused as some stats may have postUpdate tasks, however most won't.
    }

    @Override
    public String toString() {
        return "Stat{"
                + this.getKey()
                + "}";
    }
}
