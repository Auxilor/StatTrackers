package com.willfp.stattrackers.display;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.display.Display;
import com.willfp.eco.util.display.DisplayModule;
import com.willfp.eco.util.display.DisplayPriority;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import com.willfp.stattrackers.tracker.util.TrackerUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StatTrackersDisplay extends DisplayModule {
    /**
     * Create new stat trackers display.
     *
     * @param plugin Instance of StatTrackers.
     */
    public StatTrackersDisplay(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, DisplayPriority.HIGHEST);
    }

    @Override
    protected void display(@NotNull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        assert meta != null;

        List<String> itemLore = new ArrayList<>();

        if (meta.hasLore()) {
            itemLore = meta.getLore();
        }

        if (itemLore == null) {
            itemLore = new ArrayList<>();
        }

        Stat stat = StatChecks.getActiveStat(itemStack);

        if (stat == null) {
            Stat trackerStat = TrackerUtils.getTrackedStat(itemStack);
            if (trackerStat == null) {
                return;
            }

            meta.setDisplayName(this.getPlugin().getLangYml().getString("tracker"));
            List<String> lore = this.getPlugin().getLangYml().getStrings("tracker-description");

            for (int i = 0; i < lore.size(); i++) {
                String string = lore.get(i);
                string = StringUtils.translate(string);
                string = string.replace("%stat%", trackerStat.getColor() + trackerStat.getDescription());
                string = Display.PREFIX + string;
                lore.set(i, string);
            }

            meta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            meta.setLore(lore);
            itemStack.setItemMeta(meta);

            return;
        }

        itemLore.add(Display.PREFIX + "§f" + stat.getColor() + stat.getDescription() + this.getPlugin().getLangYml().getString("delimiter") + StringUtils.internalToString(StatChecks.getStatOnItem(itemStack, stat)));
        meta.setLore(itemLore);
        itemStack.setItemMeta(meta);
    }

    @Override
    protected void revert(@NotNull final ItemStack itemStack) {

    }
}
