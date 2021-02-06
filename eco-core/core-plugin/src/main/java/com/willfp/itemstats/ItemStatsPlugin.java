package com.willfp.itemstats;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.display.Display;
import com.willfp.eco.util.display.DisplayModule;
import com.willfp.eco.util.integrations.IntegrationLoader;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.itemstats.commands.CommandActivestat;
import com.willfp.itemstats.commands.CommandIstatsgive;
import com.willfp.itemstats.commands.CommandIstatsreload;
import com.willfp.itemstats.commands.TabcompleterActivestat;
import com.willfp.itemstats.commands.TabcompleterIstatsgive;
import com.willfp.itemstats.display.ItemStatsDisplay;
import com.willfp.itemstats.stats.Stats;
import com.willfp.itemstats.tracker.TrackerListener;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class ItemStatsPlugin extends AbstractEcoPlugin {
    /**
     * Instance of the plugin.
     */
    @Getter
    private static ItemStatsPlugin instance;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public ItemStatsPlugin() {
        super("ItemStats", 88247, 9914, "com.willfp.itemstats.proxy", "&d");
        instance = this;
    }

    /**
     * Code executed on plugin enable.
     */
    @Override
    public void enable() {
        Display.registerDisplayModule(new DisplayModule(ItemStatsDisplay::displayStat, 400, this.getPluginName()));
        Display.registerRevertModule(ItemStatsDisplay::revertDisplay);
        this.getLog().info(Stats.values().size() + " Stats Loaded");
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
     * ItemStats-specific integrations.
     *
     * @return A list of all integrations.
     */
    @Override
    public List<IntegrationLoader> getIntegrationLoaders() {
        return new ArrayList<>();
    }

    /**
     * ItemStats-specific commands.
     *
     * @return A list of all commands.
     */
    @Override
    public List<AbstractCommand> getCommands() {
        return Arrays.asList(
                new CommandIstatsreload(this),
                new CommandActivestat(this),
                new CommandIstatsgive(this)
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
     * ItemStats-specific listeners.
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
                TabcompleterActivestat.class,
                TabcompleterIstatsgive.class
        );
    }
}
