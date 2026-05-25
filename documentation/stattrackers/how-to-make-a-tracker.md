---
title: "How to make a Stat Tracker"
sidebar_position: 1
---

## How to add stat trackers
Each stat tracker is its own config file, placed in the `/stats/` folder, and you can add or remove them as you please. There's an example config called `_example.yml` to help you out!

The ID of the Stat Tracker is the file name. This is what you use in commands and in the [Item Lookup System](https://plugins.auxilor.io/the-item-lookup-system).
ID's must be lowercase letters, numbers, and underscores only.

## Example Tracker Config

```yaml
display: "&bNether Ores Mined: %value%"

applicable-to:
  - sword
  - bow
  - trident
  - axe

counters:
  - trigger: mine_block
    value: 1
    filters:
      blocks:
        - nether_gold_ore
        - ancient_debris
        - nether_quartz_ore
    conditions:
      - id: in_world
        args:
          world: world_nether

tracker:
  item: compass max_stack_size:1
  name: "&eTracker - Damage Dealt"
  lore:
    - "&8Drop this onto an item with /stattrackers"
    - "&8to display the amount of damage dealt"

  craftable: true 
  recipe-permission: stattrackers.craft.example_tracker
  shapeless: false
  recipe:
    - iron_sword
    - iron_sword
    - iron_sword
    - iron_sword
    - compass
    - iron_sword
    - iron_sword
    - iron_sword
    - iron_sword
```

## Understanding all the sections

### The Tracker Info Section

```yaml
# The lore added to items with this tracker
display: "&bNether Ores Mined: %value%"

# Which items the tracker can be applied to, groups are in targets.yml
applicable-to:
  - sword
  - bow
  - trident
  - axe
```

### The Counter Section
This is the section that defines what stat is tracked and how to track it.
```yaml
# A counter takes a trigger, a multiplier, conditions, and filters.
# The 'multiplier' takes the value produced by the trigger and multiplies it
# Alternatively, you can use 'value' to count a specific number and not a multiplier
counters:
  - trigger: mine_block
    value: 1 # You can also use `multiplier` here.
    filters:
      blocks:
        - nether_gold_ore
        - ancient_debris
        - nether_quartz_ore
    conditions:
      - id: in_world
        args:
          world: world_nether
```

### The Tracker Item Section
```yaml
# Options for the physical tracker item
tracker:
  # The item, read here for options: https://plugins.auxilor.io/the-item-lookup-system
  item: compass max_stack_size:1

  # The display name of the tracker
  name: "&eTracker - Damage Dealt"

  # The lore of the tracker
  lore:
    - "&8Drop this onto an item with /stattrackers"
    - "&8to display the amount of damage dealt"

  craftable: true # If the tracker should be craftable
  recipe-permission: stattrackers.craft.example_tracker # (Optional) The permission required to craft the tracker
  shapeless: false # (Optional) Whether the recipe is shapeless, default is false
  recipe: # The tracker recipe, read here: https://plugins.auxilor.io/the-item-lookup-system#crafting-recipes
    - iron_sword
    - iron_sword
    - iron_sword
    - iron_sword
    - compass
    - iron_sword
    - iron_sword
    - iron_sword
    - iron_sword
```

:::tip

We support shaped and shapeless recipes. Check out [Recipes](https://plugins.auxilor.io/the-item-lookup-system/recipes) for more info on how to configure these.

:::

<hr/>

## Default configs
The default configs can be found [here](https://github.com/Auxilor/StatTrackers/tree/master/eco-core/core-plugin/src/main/resources/stats).
You can find additional user-created configs on [lrcdb](https://lrcdb.auxilor.io/).