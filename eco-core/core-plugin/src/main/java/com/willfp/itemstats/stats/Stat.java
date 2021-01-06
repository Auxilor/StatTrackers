package com.willfp.itemstats.stats;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.itemstats.config.ItemStatsConfigs;
import com.willfp.itemstats.config.configs.StatConfig;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
     * The config name of the stat.
     */
    @Getter
    private final String configName;

    /**
     * The stat's config.
     */
    @Getter
    private final StatConfig config;

    /**
     * The items that the stat can track.
     */
    @Getter
    private final Set<Material> target = new HashSet<>();

    /**
     * Create a new Stat.
     *
     * @param key           The key name of the stat.
     * @param prerequisites Optional {@link Prerequisite}s that must be met.
     */
    protected Stat(@NotNull final String key,
                   @NotNull final Prerequisite... prerequisites) {
        this.key = this.getPlugin().getNamespacedKeyFactory().create(key);
        this.configName = key.replace("_", "");
        ItemStatsConfigs.addStatConfig(new StatConfig(this.configName, this.getClass()));
        this.config = ItemStatsConfigs.getStatConfig(this.configName);

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
        description = StringUtils.translate(config.getString("description"));
        target.clear();
        target.addAll(config.getStrings(Stats.GENERAL_LOCATION + "targets").stream().map(s-> Material.valueOf(s.toUpperCase())).collect(Collectors.toList()));

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
