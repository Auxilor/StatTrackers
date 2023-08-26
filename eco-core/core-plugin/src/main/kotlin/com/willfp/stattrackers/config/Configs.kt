package com.willfp.stattrackers.config

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.BaseConfig
import com.willfp.eco.core.config.ConfigType

class TargetsYml(plugin: EcoPlugin) : BaseConfig("targets", plugin, true, ConfigType.YAML)
