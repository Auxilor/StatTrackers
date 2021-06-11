package com.willfp.stattrackers;

import com.willfp.eco.core.AbstractPacketAdapter;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.stattrackers.commands.CommandActivestat;
import com.willfp.stattrackers.commands.CommandStgive;
import com.willfp.stattrackers.commands.CommandStreload;
import com.willfp.stattrackers.commands.TabCompleterActivestat;
import com.willfp.stattrackers.commands.TabCompleterStgive;
import com.willfp.stattrackers.display.StatTrackersDisplay;
import com.willfp.stattrackers.stats.Stats;
import com.willfp.stattrackers.tracker.TrackerListener;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        super("StatTrackers", 88247, 10261, "com.willfp.stattrackers.proxy", "&d");
        instance = this;
    }

    /**
     * Code executed on plugin enable.
     */
    @Override
    public void enable() {
        this.getLogger().info(Stats.values().size() + " Stats Loaded");
    }

    /**
     * Code executed on plugin disable.
     */
    @Override
    public void disable() {
        // Nothing needs to be called on disable
    }

    /**
     * Nothing is called on plugin load.
     */
    @Override
    public void load() {
        // Nothing needs to be called on load
    }

    /**
     * Code executed on reload.
     */
    @Override
    public void onReload() {
        Stats.values().forEach(stat -> {
            HandlerList.unregisterAll(stat);

            this.getScheduler().runLater(() -> {
                this.getEventManager().registerListener(stat);
            }, 1);
        });
    }

    /**
     * Code executed after server is up.
     */
    @Override
    public void postLoad() {
        // Nothing needs to be called after load.
    }

    /**
     * StatTrackers-specific integrations.
     *
     * @return A list of all integrations.
     */
    @Override
    public List<IntegrationLoader> getIntegrationLoaders() {
        return new ArrayList<>();
    }

    /**
     * StatTrackers-specific commands.
     *
     * @return A list of all commands.
     */
    @Override
    public List<AbstractCommand> getCommands() {
        return Arrays.asList(
                new CommandStreload(this),
                new CommandActivestat(this),
                new CommandStgive(this)
        );
    }

    /**
     * Packet Adapters for stat display.
     *
     * @return A list of packet adapters.
     */
    @Override
    public List<AbstractPacketAdapter> getPacketAdapters() {
        return new ArrayList<>();
    }

    /**
     * StatTrackers-specific listeners.
     *
     * @return A list of all listeners.
     */
    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(
                new TrackerListener(this)
        );
    }

    @Override
    public List<Class<?>> getUpdatableClasses() {
        return Arrays.asList(
                Stats.class,
                TabCompleterActivestat.class,
                TabCompleterStgive.class
        );
    }

    @Override
    @Nullable
    protected DisplayModule createDisplayModule() {
        return new StatTrackersDisplay(this);
    }
}
