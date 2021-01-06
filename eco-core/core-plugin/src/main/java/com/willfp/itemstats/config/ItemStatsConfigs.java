package com.willfp.itemstats.config;

import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.itemstats.config.configs.StatConfig;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class ItemStatsConfigs {
    /**
     * All talisman-specific configs.
     */
    @Getter
    private static final Set<StatConfig> STAT_CONFIGS = new HashSet<>();

    /**
     * Update all configs.
     */
    @ConfigUpdater
    public void updateConfigs() {
        STAT_CONFIGS.forEach(StatYamlConfig::update);
    }

    /**
     * Get StatConfig matching config name.
     *
     * @param configName The config name to match.
     * @return The matching {@link StatConfig}.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public StatConfig getStatConfig(@NotNull final String configName) {
        return STAT_CONFIGS.stream().filter(config -> config.getName().equalsIgnoreCase(configName)).findFirst().get();
    }

    /**
     * Adds new stat config yml.
     *
     * @param config The config to add.
     */
    public void addStatConfig(@NotNull final StatConfig config) {
        STAT_CONFIGS.add(config);
    }
}
