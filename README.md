# Shhh Printer

An automated schematic block placement companion mod for [Shhh Builder](https://github.com/RafayShhh/shhh-builder).

## Overview
Shhh Printer automates the placement of blocks from a schematic loaded in Shhh Builder.

## Usage

### Commands
All commands start with `/shhhprinter`.

| Command | Description |
| :--- | :--- |
| `/shhhprinter toggle` | Toggle automated block placement |
| `/shhhprinter on` | Enable the printer |
| `/shhhprinter off` | Disable the printer |
| `/shhhprinter speed <1-5>` | Set placement speed (blocks per tick) |
| `/shhhprinter delay <0-10>` | Set delay between placements in ticks |
| `/shhhprinter mode <strict|air>` | Set placement mode (Strict Adjacent / Air) |
| `/shhhprinter preset <creative|survival|public>` | Apply predefined configuration presets |
| `/shhhprinter config` | Open settings screen |
| `/shhhprinter stats` | Display printer status and statistics |

### Keybinds
- **P**: Toggle Printer
- **O**: Open Config Screen

## Configuration
Use the `/shhhprinter config` command or the **O** key to open the settings screen for fine-grained control over printer behavior.

---
## Dependencies
- **Shhh Builder** (Hard dependency)
- **Fabric API**
- **Minecraft** (v26.3)
