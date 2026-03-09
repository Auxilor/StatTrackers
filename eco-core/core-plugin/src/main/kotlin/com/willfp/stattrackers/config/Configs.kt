package com.willfp.stattrackers.config

import com.willfp.eco.core.config.BaseConfig
import com.willfp.eco.core.config.ConfigType
import com.willfp.stattrackers.StatTrackersPlugin

class TargetsYml(plugin: StatTrackersPlugin) : BaseConfig("targets", plugin, true, ConfigType.YAML)
