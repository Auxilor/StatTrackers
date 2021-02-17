package com.willfp.stattrackers.tracker;

import com.willfp.eco.util.display.Display;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.recipe.RecipeParts;
import com.willfp.eco.util.recipe.recipes.EcoShapedRecipe;
import com.willfp.stattrackers.StatTrackersPlugin;
import com.willfp.stattrackers.stats.Stat;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StatTracker {
    /**
     * Instance of StatTrackers to create keys for.
     */
    @Getter
    private final AbstractEcoPlugin plugin = StatTrackersPlugin.getInstance();

    /**
     * The stat to track.
     */
    @Getter
    private final Stat stat;

    /**
     * The ItemStack of the tracker.
     */
    @Getter
    private ItemStack itemStack;

    /**
     * The crafting recipe to make the tracker.
     */
    @Getter
    private EcoShapedRecipe recipe;

    /**
     * If the recipe is enabled.
     */
    @Getter
    private boolean enabled;

    /**
     * Create a new Stat tracker.
     *
     * @param stat The stat to track.
     */
    public StatTracker(@NotNull final Stat stat) {
        this.stat = stat;
        this.update();
    }

    /**
     * Update the tracker's crafting recipe.
     */
    public void update() {
        enabled = this.getPlugin().getConfigYml().getBool("stat." + stat.getKey().getKey() + ".enabled");

        NamespacedKey key = this.getPlugin().getNamespacedKeyFactory().create("stat_tracker");

        ItemStack out = new ItemStack(Material.COMPASS);
        ItemMeta outMeta = out.getItemMeta();
        assert outMeta != null;
        PersistentDataContainer container = outMeta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, stat.getKey().getKey());
        out.setItemMeta(outMeta);
        Display.display(out);
        this.itemStack = out;

        if (this.isEnabled()) {
            EcoShapedRecipe.Builder builder = EcoShapedRecipe.builder(this.getPlugin(), stat.getKey().getKey())
                    .setOutput(out);

            List<String> recipeStrings = plugin.getConfigYml().getStrings("stat." + stat.getKey().getKey() + ".tracker-recipe");

            for (int i = 0; i < 9; i++) {
                builder.setRecipePart(i, RecipeParts.lookup(recipeStrings.get(i)));
            }

            this.recipe = builder.build();
            this.recipe.register();
        }
    }
}
