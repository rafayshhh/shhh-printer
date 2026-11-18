<div align="center">

# Shhh Printer

**An automated schematic block-placement companion mod for [Shhh Builder](https://github.com/RafayShhh/shhh-builder).**  
*Automate your Minecraft builds with zero packet-transmission botting.*

</div>

---

## Overview

Shhh Printer is an automation companion for [Shhh Builder](https://github.com/RafayShhh/shhh-builder). It takes the schematics you have placed in the world and automatically bridges, builds, and manages your inventory to place blocks exactly where they need to go, following the visual guide provided by Shhh Builder.

---

## Features

### Smart Automated Placement
- **Dynamic Speed Control**: Configure placement speed (1-5 blocks/tick) to match server performance and lag.
- **Air Placement & Strict Modes**: Choose between fast, unconstrained air placement (for creative/lenient servers) or strict adjacent-block-face placement (for safe/anti-cheat survival servers).
- **Intelligent Inventory Management**:
    - **Auto-Switching**: Automatically selects the correct block from your hotbar.
    - **Inventory Swapping**: Automatically swaps items from your main inventory to your hotbar if needed.

### Built-in Configuration Presets (`/shhhprinter preset`)
Quickly adapt the printer to your current environment:
- **Creative**: Maximum speed, air placement enabled, no restrictions.
- **Survival**: Safe speed, strict adjacent placement required, smart inventory management.
- **Public Server**: Slowed speed, strict adjacent placement, disabled auto-inventory swaps to comply with server anti-cheat.

### Client-Side Commands (`/shhhprinter`)
Control the printer entirely from chat with zero server communication:

| Command | Description |
| :--- | :--- |
| `/shhhprinter toggle` | Toggle automated block placement |
| `/shhhprinter on / off` | Explicitly enable or disable the printer |
| `/shhhprinter speed <1-5>` | Set placement speed (blocks per tick) |
| `/shhhprinter delay <0-10>` | Set delay between placements in ticks |
| `/shhhprinter mode <strict|air>` | Set placement mode (Strict Adjacent / Air) |
| `/shhhprinter preset <name>` | Apply predefined configuration presets |
| `/shhhprinter config` | Open graphical settings screen |
| `/shhhprinter stats` | Display printer status and statistics |

---

## Usage & Keybinds

All keybindings can be customized in **Options → Controls → Key Binds → Shhh Printer**:

| Key | Default Action | Description |
| :---: | :--- | :--- |
| **P** | Toggle Printer | Toggle automated block placement on/off |
| **O** | Open Config | Open the simplified printer settings screen |

---

## Installation

1. Install **Fabric Loader** (`0.20.1` or newer) for **Minecraft 26.3**.
2. Install **Fabric API** for 26.3.
3. Install **[Shhh Builder](https://github.com/RafayShhh/shhh-builder)** (Required dependency).
4. Download **`shhh-printer-1.0.0.jar`** and place it in your `.minecraft/mods` folder.
5. Launch Minecraft!

---

## License & Source

Licensed under the permissive [MIT License](LICENSE).  
Source code and issue tracker are hosted on [GitHub](https://github.com/rafayshhh/shhh-printer).
