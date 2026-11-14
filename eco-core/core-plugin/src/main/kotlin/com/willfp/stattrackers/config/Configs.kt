package com.willfp.stattrackers.config

import com.willfp.stattrackers.StatTrackersPlugin
import wiki.ecohub.config.ConfigType
import wiki.ecohub.config.file.BaseConfig

class TargetsYml(plugin: StatTrackersPlugin) : BaseConfig("targets", plugin, true, ConfigType.YAML)
