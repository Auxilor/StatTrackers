package com.willfp.stattrackers;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.stattrackers.commands.CommandActivestat;
import com.willfp.stattrackers.commands.CommandStgive;
import com.willfp.stattrackers.commands.CommandStreload;
import com.willfp.stattrackers.display.StatTrackersDisplay;
import com.willfp.stattrackers.stats.Stats;
import com.willfp.stattrackers.tracker.TrackerListener;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class StatTrackersPlugin extends EcoPlugin {
    /**
     * Instance of the plugin.
     */
    @Getter
    private static StatTrackersPlugin instance;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public StatTrackersPlugin() {
        super(623, 10261, "&d");
        instance = this;
    }

    @Override
    protected void handleEnable() {
        this.getLogger().info(Stats.values().size() + " Stats Loaded");
    }

    @Override
    protected void handleReload() {
        Stats.values().forEach(stat -> {
            HandlerList.unregisterAll(stat);

            this.getScheduler().runLater(() -> {
                this.getEventManager().registerListener(stat);
            }, 1);
        });
    }

    @Override
    protected List<PluginCommand> loadPluginCommands() {
        return Arrays.asList(
                new CommandStreload(this),
                new CommandActivestat(this),
                new CommandStgive(this)
        );
    }

    @Override
    protected List<Listener> loadListeners() {
        return Arrays.asList(
                new TrackerListener(this)
        );
    }

    @Override
    protected @Nullable DisplayModule createDisplayModule() {
        return new StatTrackersDisplay(this);
    }
}
