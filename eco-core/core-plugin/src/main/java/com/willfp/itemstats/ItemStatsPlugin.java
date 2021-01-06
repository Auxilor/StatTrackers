package com.willfp.itemstats;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.integrations.IntegrationLoader;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.itemstats.commands.CommandActivestat;
import com.willfp.itemstats.commands.CommandIstatsdebug;
import com.willfp.itemstats.commands.CommandIstatsreload;
import com.willfp.itemstats.commands.TabCompleterActivestat;
import com.willfp.itemstats.display.packets.PacketSetCreativeSlot;
import com.willfp.itemstats.display.packets.PacketSetSlot;
import com.willfp.itemstats.display.packets.PacketWindowItems;
import com.willfp.itemstats.stats.Stats;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class ItemStatsPlugin extends AbstractEcoPlugin {
    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public ItemStatsPlugin() {
        super("ItemStats", 0, 0, "com.willfp.itemstats.proxy", "&d");
    }

    /**
     * Code executed on plugin enable.
     */
    @Override
    public void enable() {
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
                new CommandIstatsdebug(this),
                new CommandIstatsreload(this),
                new CommandActivestat(this)
        );
    }

    /**
     * Packet Adapters for stat display.
     *
     * @return A list of packet adapters.
     */
    @Override
    public List<AbstractPacketAdapter> getPacketAdapters() {
        return Arrays.asList(
                new PacketSetCreativeSlot(this),
                new PacketSetSlot(this),
                new PacketWindowItems(this)
        );
    }

    /**
     * ItemStats-specific listeners.
     *
     * @return A list of all listeners.
     */
    @Override
    public List<Listener> getListeners() {
        return new ArrayList<>();
    }

    @Override
    public List<Class<?>> getUpdatableClasses() {
        return Arrays.asList(
                Stats.class,
                TabCompleterActivestat.class
        );
    }
}
