---
title: "How to Make a Stat Tracker"
sidebar_position: 1
---

A **stat tracker** is a craftable item that, once dropped onto a tool, weapon, or armor piece, counts a stat on it and shows the running total in the item's lore. Each tracker is one config file built from a **display**, a set of **counters**, and a **tracker item**. This page takes you from an empty file to a working, craftable tracker.

## Quick start

1. Open `/plugins/StatTrackers/stats/` and copy `_example.yml` to a new file, e.g. `nether_ores_mined.yml`. The file name is the tracker's ID.
2. Set `display` to the lore line you want, using `%value%` where the count should appear.
3. List the items the tracker can attach to under `applicable-to`.
4. Add one or more `counters` to define what gets counted.
5. Fill in the `tracker` block: the item, name, lore, and (optionally) a crafting recipe.
6. Run `/stattrackers reload`, then run `/stattrackers give <player> nether_ores_mined`, drop the tracker onto a matching item, and check the lore updates as you play.

:::tip
`_example.yml` is included as a reference and is **never loaded**, so copy or rename it to make a real tracker. You can also organise trackers into subfolders inside `stats/`, and they'll still load.
:::

## Naming and IDs

The file name without `.yml` is the tracker's ID. That ID is what you pass to commands and use in the [Item Lookup System](https://plugins.auxilor.io/the-item-lookup-system).

:::warning ID rules
IDs may only contain lowercase letters, numbers, and underscores (a-z, 0-9, _). No spaces, capitals, or hyphens, or the tracker will not load.
:::

## The structure of a tracker

A tracker config has three parts:

| Part | What it controls |
| --- | --- |
| **Display** | The lore line shown on the item and which items the tracker can attach to. |
| **Counters** | What stat is counted and how, using triggers, filters, and conditions. |
| **Tracker item** | The physical tracker item, its appearance, and its crafting recipe. |

Here is a complete tracker with every part in place:

```yaml
# === Display: what the player sees ===
display: "&bNether Ores Mined: %value%" # Lore line on the item; %value% is the running count
applicable-to: # Item groups this tracker can attach to, defined in targets.yml
  - sword
  - bow
  - trident
  - axe

# === Counters: what gets counted ===
counters:
  - trigger: mine_block # The event that adds to the count
    value: 1 # Add a flat 1 per trigger; use 'multiplier' instead to scale the trigger's own value
    filters:
      blocks: # Only count these blocks
        - nether_gold_ore
        - ancient_debris
        - nether_quartz_ore
    conditions:
      - id: in_world # Only count while this condition holds
        args:
          world: world_nether

# === Tracker item: the physical item ===
tracker:
  item: compass max_stack_size:1 # The base item; see the Item Lookup System for options
  name: "&eTracker - Nether Ores Mined" # Display name of the tracker item
  lore:
    - "&8Drop this onto an item with /stattrackers"
    - "&8to display the nether ores mined"
  craftable: true # Whether the tracker can be crafted
  recipe-permission: stattrackers.craft.nether_ores_mined # Optional; permission needed to craft it
  shapeless: false # Optional; whether the recipe is shapeless, default false
  recipe: # The crafting grid, read top-left to bottom-right
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

### Display

The `display` line is the lore added to the item, and `applicable-to` controls which items the tracker can attach to.

```yaml
display: "&bNether Ores Mined: %value%" # %value% is replaced with the current count
applicable-to: # Item groups defined in targets.yml (sword, bow, armor, pickaxe, etc.)
  - sword
  - bow
  - trident
  - axe
```

:::info
Item groups like `sword` and `armor` are defined in `targets.yml`, which also sets the slot each group is tracked in (hand, offhand, armor, or any). Edit that file to change what an existing group matches or to add your own.
:::

### Counters

A counter defines what stat is tracked: a `trigger` fires on an event, then `value` (a flat amount) or `multiplier` (a scale on the trigger's own value) decides how much to add. Optional `filters` and `conditions` narrow when it counts.

```yaml
counters:
  - trigger: mine_block # Event that fires the counter
    value: 1 # Flat amount added per trigger; swap for 'multiplier' to scale the trigger's value
    filters:
      blocks: # Restrict to specific blocks
        - nether_gold_ore
        - ancient_debris
    conditions:
      - id: in_world # Only count while the condition is met
        args:
          world: world_nether
```

:::danger Effects are their own system
Triggers, filters, and conditions are a shared libreforge system used across every eco plugin, documented in full elsewhere.

- [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect)
- [Configuring an Effect Chain](https://plugins.auxilor.io/effects/configuring-a-chain)
:::

### Tracker item

The `tracker` block defines the physical item players drop onto their gear, plus its optional crafting recipe.

```yaml
tracker:
  item: compass max_stack_size:1 # Base item; see the Item Lookup System for options
  name: "&eTracker - Nether Ores Mined" # Display name
  lore:
    - "&8Drop this onto an item with /stattrackers"
    - "&8to display the nether ores mined"
  craftable: true # Whether it can be crafted
  recipe-permission: stattrackers.craft.nether_ores_mined # Optional; permission to craft
  shapeless: false # Optional; whether the recipe is shapeless, default false
  recipe: # Crafting grid, read top-left to bottom-right
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
We support both shaped and shapeless recipes. See [Recipes](https://plugins.auxilor.io/the-item-lookup-system/recipes) for how to configure them.
:::

## Internal placeholders

These placeholders are available inside this config:

| Placeholder | What it returns |
| --- | --- |
| `%value%` | The current tracked count, used in the `display` line. |

:::tip Troubleshooting
- **Tracker won't load?** Check the file name is lowercase letters, numbers, and underscores only, then run `/stattrackers reload`.
- **Nothing counts when I play?** Make sure the tracker is on an item listed in `applicable-to`, and that the item is in the slot its target group tracks (see `targets.yml`).
- **The count never goes up?** Check your `filters` and `conditions` aren't excluding the action, e.g. the wrong block or world.
- **Can't craft the tracker?** Confirm `craftable: true` and that the player has the `recipe-permission`, if you set one.
:::

<hr/>

## Where to go next

- **Default trackers:** the shipped configs live [here](https://github.com/Auxilor/StatTrackers/tree/master/eco-core/core-plugin/src/main/resources/stats) and make good starting points.
- **Community configs:** browse user-made trackers on [lrcdb](https://lrcdb.auxilor.io/).
- **Triggers and conditions:** [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect) covers the full counter system.