---
title: "Plugin Config"
sidebar_position: 3
---

The global settings for StatTrackers live in `/plugins/StatTrackers/config.yml`. It controls the apply GUI and how stat lines are shown on items. Edit it, then run `/stattrackers reload` to apply your changes.

## Default config.yml

```yaml
gui:
  rows: 1 # Number of rows in the /stattrackers apply GUI
  title: "Stats" # Title shown at the top of the GUI

discover-recipes: true # Whether players auto-unlock tracker recipes in their recipe book
display-at-top: false # Show tracker stats at the top of the item's lore instead of the bottom
```

<hr/>

## Where to go next

- **Make a tracker:** [How to make a Stat Tracker](how-to-make-a-tracker) covers building individual trackers.
- **Commands:** [Commands and Permissions](commands-and-permissions) lists the reload and give commands.