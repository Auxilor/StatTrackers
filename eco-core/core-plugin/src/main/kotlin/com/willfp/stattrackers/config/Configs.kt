package com.willfp.stattrackers.config

import com.willfp.eco.core.config.BaseConfig
import com.willfp.eco.core.config.ConfigType
import com.willfp.stattrackers.plugin

object TargetsYml : BaseConfig("targets", plugin, true, ConfigType.YAML)
