<div align="center">

# Shhh Printer

**An automated schematic block-placement companion mod for [Shhh Builder](https://github.com/RafayShhh/shhh-builder).**  
*Place schematic blocks right where you point, with fair-play defaults and no aura hacking.*

</div>

---

## Overview

Shhh Printer is a client-side building companion for [Shhh Builder](https://github.com/RafayShhh/shhh-builder) on Fabric 26.3. Rather than placing blocks automatically all around the player, Shhh Printer operates primarily in **Point & Place** crosshair mode: it only places the schematic block you are actively pointing at.

It requires no server-side modding and runs with safe, anti-cheat friendly defaults.

---

## Features

### Point & Place Targeting (Default)
- **Aim-Assisted Placement**: The printer inspects the ghost block in your crosshair and places only that block when you are looking at it. If you look away, no blocks are placed.
- **Fair-Play Default**: Air placement is disabled by default. Blocks require valid adjacent block support, mirroring vanilla placement rules.
- **Optional Area Mode**: For creative or singleplayer testing, Area 360 mode can be toggled to automatically place all missing blocks within reach.

### Built-in Configuration Presets (`/shhhprinter preset`)
Quickly switch between pre-configured operational profiles:
- **Survival (Default)**: Point & Place targeting, 1 block/tick, 2-tick delay (~10 blocks/sec legit cadence), air placement disabled, strict adjacent support enabled, auto hotbar selection enabled.
- **Public Server (Safe)**: Point & Place targeting, 1 block/tick, 4-tick delay (~5 blocks/sec anti-cheat cadence), air placement disabled, strict adjacent support enabled, inventory swaps disabled to avoid suspicious packet rates.
- **Creative (Fast)**: Point & Place targeting, 3 blocks/tick, 0-tick delay, air placement enabled, strict adjacent support disabled.

### Intelligent Inventory Management
- **Auto-Switching**: Automatically selects the matching block from your hotbar when pointing at a ghost block.
- **Inventory Swapping**: Automatically moves blocks from main inventory to hotbar when running out.
- **Auto-Facing Calculation**: Calculates placement vectors for slabs, stairs, logs, and directional blocks based on crosshair hit angle.

### Client-Side Commands (`/shhhprinter`)
Control all settings directly from chat:

| Command | Description |
| :--- | :--- |
| `/shhhprinter toggle` | Toggle automated block placement on or off |
| `/shhhprinter on / off` | Explicitly enable or disable the printer |
| `/shhhprinter target <crosshair\|all>` | Switch between Point & Place (crosshair) and Area 360 |
| `/shhhprinter preset <survival\|public\|creative>` | Apply a pre-configured speed and safety preset |
| `/shhhprinter speed <1-5>` | Set maximum blocks placed per tick |
| `/shhhprinter delay <0-10>` | Set delay in ticks between placement actions |
| `/shhhprinter mode <strict\|air>` | Toggle strict adjacent support requirement vs air placement |
| `/shhhprinter config` | Open the graphical configuration screen |
| `/shhhprinter stats` | View current target mode, speed, and placed block count |

---

## Keybindings

Configure keybindings in **Options -> Controls -> Key Binds -> Shhh Printer**:

| Key | Default Action | Description |
| :---: | :--- | :--- |
| **P** | Toggle Printer | Enable or disable automated block placement |
| **O** | Open Config | Open the settings screen |

---

## Installation

1. Install **Fabric Loader** (`0.20.1` or newer) for **Minecraft 26.3**.
2. Install **Fabric API** for 26.3.
3. Install **[Shhh Builder](https://github.com/RafayShhh/shhh-builder)** (Required companion mod).
4. Download **`shhh-printer-1.0.0.jar`** and place it in your `.minecraft/mods` directory.
5. Launch Minecraft.

---

## Documentation

Full architectural documentation, audit reports, and user guides are available in the [Documentation](Documentation/) directory:
- [System Architecture](Documentation/ARCHITECTURE.md)
- [Audit Report](Documentation/AUDIT.md)
- [Testing Guide](Documentation/TESTING.md)
- [Changelog](Documentation/CHANGELOG.md)
- [UI Style Guide](Documentation/UI_STYLE.md)
- [CurseForge Metadata](Documentation/CURSEFORGE_METADATA.md)
- [Modrinth Metadata](Documentation/MODRINTH_METADATA.md)
- [Socials & Branding](Documentation/SOCIALS_AND_BRANDING.md)

---

## License & Source

Licensed under the [MIT License](LICENSE).  
Source code and issue tracker are hosted on [GitHub](https://github.com/rafayshhh/shhh-printer).
