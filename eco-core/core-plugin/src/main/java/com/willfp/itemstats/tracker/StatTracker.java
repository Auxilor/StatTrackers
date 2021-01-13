package com.willfp.itemstats.tracker;

import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.itemstats.stats.Stat;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StatTracker {
    /**
     * Instance of ItemStats to create keys for.
     */
    @Getter
    private final AbstractEcoPlugin plugin = AbstractEcoPlugin.getInstance();

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
    private ShapedRecipe recipe;

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
        enabled = Configs.CONFIG.getBool("stat." + stat.getKey().getKey() + ".enabled");

        NamespacedKey key = this.getPlugin().getNamespacedKeyFactory().create("stat_tracker");

        ItemStack out = new ItemStack(Material.COMPASS);
        ItemMeta outMeta = out.getItemMeta();
        assert outMeta != null;
        PersistentDataContainer container = outMeta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, stat.getKey().getKey());
        out.setItemMeta(outMeta);
        this.itemStack = out;

        Bukkit.getServer().removeRecipe(stat.getKey());

        if (this.isEnabled()) {
            ShapedRecipe recipe = new ShapedRecipe(stat.getKey(), itemStack);

            List<String> recipeStrings = Configs.CONFIG.getStrings("stat." + stat.getKey().getKey() + ".tracker-recipe");

            recipe.shape("012", "345", "678");

            for (int i = 0; i < 9; i++) {
                char ingredientChar = String.valueOf(i).toCharArray()[0];
                recipe.setIngredient(ingredientChar, Material.valueOf(recipeStrings.get(i).toUpperCase()));
            }

            this.recipe = recipe;
            Bukkit.getServer().addRecipe(recipe);
        }
    }
}
