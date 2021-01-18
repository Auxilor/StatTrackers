package com.willfp.itemstats.stats.stats;

import com.google.common.collect.Sets;
import com.willfp.itemstats.stats.Stat;
import com.willfp.itemstats.stats.util.StatChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class StatTimesJumped extends Stat {
    private static final Set<UUID> PREVIOUS_PLAYERS_ON_GROUND = Sets.newHashSet();
    private static final DecimalFormat FORMAT = new DecimalFormat("0.00");

    public StatTimesJumped() {
        super("times_jumped");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void statListener(@NotNull final PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getVelocity().getY() > 0) {
            float jumpVelocity = 0.42f;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += ((float) player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
            }
            jumpVelocity = Float.parseFloat(FORMAT.format(jumpVelocity).replace(',', '.'));
            if (event.getPlayer().getLocation().getBlock().getType() != Material.LADDER
                    && PREVIOUS_PLAYERS_ON_GROUND.contains(player.getUniqueId())
                    && !player.isOnGround()
                    && Float.compare((float) player.getVelocity().getY(), jumpVelocity) == 0) {
                ItemStack itemStack = player.getInventory().getBoots();

                if (itemStack == null) {
                    return;
                }

                if (itemStack.getType() == Material.AIR) {
                    return;
                }

                if (itemStack.getType().getMaxStackSize() > 1) {
                    return;
                }

                double value = StatChecks.getStatOnItem(itemStack, this);
                value += 1;
                StatChecks.setStatOnItem(itemStack, this, value);
            }
        }
        if (player.isOnGround()) {
            PREVIOUS_PLAYERS_ON_GROUND.add(player.getUniqueId());
        } else {
            PREVIOUS_PLAYERS_ON_GROUND.remove(player.getUniqueId());
        }
    }
}
