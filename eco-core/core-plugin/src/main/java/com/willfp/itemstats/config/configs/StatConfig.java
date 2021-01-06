package com.willfp.itemstats.config.configs;

import com.willfp.itemstats.config.StatYamlConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class StatConfig extends StatYamlConfig {
    /**
     * The name of the config.
     */
    @Getter
    private final String name;

    /**
     * Instantiate a new config for a stat.
     *
     * @param name     The name of the config.
     * @param plugin   The provider of the stat.
     */
    public StatConfig(@NotNull final String name,
                      @NotNull final Class<?> plugin) {
        super(name, plugin);
        this.name = name;
    }
}
