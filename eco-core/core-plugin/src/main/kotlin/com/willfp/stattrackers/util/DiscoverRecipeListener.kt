package com.willfp.stattrackers.util

import com.willfp.stattrackers.plugin
import com.willfp.stattrackers.stats.Stats
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object DiscoverRecipeListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!plugin.configYml.getBool("discover-recipes")) {
            return
        }

        for (stat in Stats.values()) {
            event.player.discoverRecipe(stat.recipe?.key ?: continue)
        }
    }
}
