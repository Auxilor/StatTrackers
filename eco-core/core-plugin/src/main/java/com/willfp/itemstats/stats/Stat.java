package com.willfp.itemstats.stats;

import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class Stat implements Listener {
    /**
     * Instance of ItemStats for stats to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final AbstractEcoPlugin plugin = AbstractEcoPlugin.getInstance();

    /**
     * The key to store stats in meta.
     */
    @Getter
    private final NamespacedKey key;

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
     * Create a new Stat.
     *
     * @param key           The key name of the stat.
     * @param prerequisites Optional {@link Prerequisite}s that must be met.
     */
    protected Stat(@NotNull final String key,
                   @NotNull final Prerequisite... prerequisites) {
        this.key = this.getPlugin().getNamespacedKeyFactory().create(key);

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
        description = Configs.LANG.getString("stat." + this.getKey().getKey() + ".description");
        color = Configs.LANG.getString("stat." + this.getKey().getKey() + ".color");

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
