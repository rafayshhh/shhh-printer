# Modrinth Project Metadata and Setup Guide

- Mod Name: Shhh Printer
- Mod Slug: shhh-printer
- Author: RafayShhh (https://modrinth.com/user/RafayShhh)
- Target Version: Minecraft 26.3 (Fabric)
- Companion Mod: [Shhh Builder](https://modrinth.com/mod/shhh-builder) (Required Dependency)

---

## Project Details

| Field | Value to Enter | Notes |
| :--- | :--- | :--- |
| Project Name | Shhh Printer | Public display name. |
| Project Slug / ID | shhh-printer | URL: https://modrinth.com/mod/shhh-printer |
| Project Type | Mod | Standard client mod. |
| Short Summary | Automated block placement companion for Shhh Builder with aim-assisted crosshair placement. | 93 characters (under 100-character limit). |
| Categories | Utility, Creative | Check both categories in dashboard. |
| Client / Server Side | Client: Required, Server: Unsupported | Pure client-side companion mod. |
| License | MIT | Select MIT from dropdown. |
| Source Repository | https://github.com/rafayshhh/shhh-printer | GitHub repository link. |
| Issue Tracker | https://github.com/rafayshhh/shhh-printer/issues | GitHub issues link. |
| Project Icon | Upload Documentation/showcase/icon.png | 1024x1024 square PNG. |

---

## Gallery Images

Upload the showcase files located in `Documentation/showcase/` (or `Assets/`):

### Image 1: Main Project Banner
- File: `Documentation/showcase/showcase-banner.jpg` (or `Assets/banner.jpg`)
- Title: Shhh Printer Official Banner
- Description: Holographic 3D printer building schematic structures block by block in Minecraft.

### Image 2: Configuration Screen
- File: `Documentation/showcase/showcase-config.jpg` (or `Assets/showcase-config.jpg`)
- Title: In-Game Settings Screen
- Description: 3-column grouped settings menu managing Behavior & Targets, Speed & Orientation, and HUD display.

### Image 3: Project Logo Icon
- File: `Documentation/showcase/icon.png` (or `Assets/icon.png`)
- Title: Shhh Printer Official Icon
- Description: 1024x1024 high-resolution modern blue neon 3D printer emblem.

---

## Dependencies Configuration

Under the project dependencies settings:
- **Shhh Builder**: Required Dependency (Slug: `shhh-builder`, URL: https://modrinth.com/mod/shhh-builder)
- **Fabric API**: Required Dependency
- **Mod Menu**: Optional Dependency

---

## Version Description (v1.0.0)

```markdown
Shhh Printer 1.0.0 is the official companion mod for Shhh Builder on Minecraft 26.3 Fabric.

Key Highlights:
- Point & Place Crosshair Aiming: Targets and places only the ghost block you are actively pointing at.
- Fair-Play Defaults: Air placement disabled by default; strict adjacent support required.
- Speed & Safety Presets: 1-click profiles for Survival (10 blk/s), Public Server (5 blk/s), and Creative fast placement.
- Smart Inventory: Automatically switches hotbar slots and restocks materials from your main inventory.
- Collision & State Safety: Halts during mining, death, sleeping, or spectator mode; never suffocates player.
- Multi-Corner Status HUD: Real-time status display customizable to any screen corner.
```

---

## Modrinth Project Description (Copy and Paste into Modrinth)

```markdown
# Shhh Printer

**An automated schematic block-placement companion mod for [Shhh Builder](https://modrinth.com/mod/shhh-builder).**  
*Aim-assisted Point & Place crosshair targeting • Built-in speed presets • Fair-play defaults • Zero aura cheating*

---

## Overview

**Shhh Printer** is a client-side automated building companion for Minecraft 26.3 (Fabric), designed specifically to work alongside **[Shhh Builder](https://modrinth.com/mod/shhh-builder)**.

Traditional schematic printers often function like blatant cheat auras: they instantly place dozens of blocks in a 360-degree sphere around the player regardless of where you look, frequently triggering server anti-cheat alerts and breaking immersion.

Shhh Printer changes this paradigm by introducing **Point & Place targeting**:
- Blocks are placed **only when your crosshair is aimed directly at an unplaced ghost block**.
- Look away, and placement immediately pauses.
- Move your crosshair along the ghost hologram, and blocks snap into the world with natural, human-like placement timing.

---

## Requirements

Shhh Printer requires **[Shhh Builder](https://modrinth.com/mod/shhh-builder)** (`>= 1.0.0`).

Shhh Builder provides the holographic schematic rendering engine, file parsing (`.litematic` / `.schem`), layer slicing, and materials tracking. Shhh Printer reads these active ghost block placements and assists you in placing the physical blocks.

---

## Key Features

### 1. Point & Place Targeting (Default)
- **Aim-Assisted Placement**: Evaluates the ghost block currently targeted by your crosshair. If you are pointing at an unplaced schematic block within reach, it places that exact block.
- **Fair-Play Defaults**: Air placement is disabled by default. Blocks require valid adjacent block support, respecting vanilla Minecraft building logic.
- **Optional Area Mode**: For creative mode or singleplayer testing, full 360-degree Area mode can be toggled on demand (`/shhhprinter target all`).

### 2. Built-in Speed & Safety Presets
Switch between pre-configured operational profiles instantly via chat or the configuration screen:
- **Survival (Default)**: 1 block/tick with a 2-tick delay (~10 blocks/sec legitimate cadence). Air placement disabled, strict adjacent support required, automatic hotbar selection enabled.
- **Public Server (Safe)**: 1 block/tick with a 4-tick delay (~5 blocks/sec anti-cheat safe cadence). Air placement disabled, strict adjacent support required, inventory swapping disabled to eliminate rapid inventory packet spikes.
- **Creative (Fast)**: 3 blocks/tick with 0-tick delay. Air placement enabled, strict adjacent support disabled for rapid structure generation.

### 3. Smart Inventory Management
- **Hotbar Auto-Switching**: Automatically detects the needed block and equips it from your hotbar with proper client-to-server synchronization packets (`ServerboundSetCarriedItemPacket`).
- **Main Inventory Swapping**: Intelligently restocks your hotbar with matching stacks from your main inventory when an active stack runs out.
- **Directional Orientation**: Automatically calculates hit vectors and pitch angles for slabs, stairs, logs, and directional blocks to ensure correct placement orientation.

### 4. Player State & Collision Safety
- **Collision Protection**: Strictly refuses to place blocks inside player bounding boxes, eliminating glitching and suffocation hazards.
- **State Guards**: Automatically pauses placement when you are dead, in spectator mode, sleeping, or actively mining blocks with your tools.

### 5. Multi-Corner Status HUD
- Displays a clean on-screen indicator showing printer state, active target mode, speed, and placed block count.
- Position can be configured to Top-Left, Top-Right (default), Bottom-Left, or Bottom-Right to prevent overlaps with other mod interfaces.

---

## Controls

All keybindings can be configured in **Options -> Controls -> Key Binds -> Shhh Printer**:

| Action | Default Key | Category | What it does |
| :--- | :---: | :--- | :--- |
| **Toggle Auto-Printer** | **P** | Shhh Printer | Turn automated block placement on or off |
| **Open Configuration** | **O** | Shhh Printer | Open the graphical configuration screen |

---

## Chat Commands (`/shhhprinter`)

You can control all printer features directly through client-side chat commands:

| Command | Description |
| :--- | :--- |
| `/shhhprinter toggle` | Toggle automated block placement on or off |
| `/shhhprinter on` | Explicitly activate the printer |
| `/shhhprinter off` | Explicitly deactivate the printer |
| `/shhhprinter target <crosshair\|all>` | Switch between Point & Place (crosshair) and Area 360 mode |
| `/shhhprinter preset <survival\|public\|creative>` | Apply an operational speed and safety preset |
| `/shhhprinter speed <1-5>` | Set maximum blocks placed per placement tick |
| `/shhhprinter delay <0-10>` | Set delay in ticks between placement actions |
| `/shhhprinter mode <strict\|air>` | Toggle strict adjacent support requirement vs air placement |
| `/shhhprinter config` | Open the graphical configuration screen |
| `/shhhprinter stats` | View current status, target mode, speed, and placement statistics |

---

## Installation

1. Install **Fabric Loader** (`0.20.1` or newer) for **Minecraft 26.3**.
2. Install **Fabric API** for 26.3.
3. Install **[Shhh Builder](https://modrinth.com/mod/shhh-builder)** (Required).
4. Download `shhh-printer-1.0.0.jar` and drop it into your `.minecraft/mods` folder.
5. Launch Minecraft.

---

## Server Compliance

Shhh Printer runs strictly on your local game client. In its default **Survival** and **Public Server** presets:
- Blocks are placed individually using legitimate client-side interaction rays.
- Placements require a physical supporting block adjacent to the target position.
- Blocks are placed exclusively where the player is looking, respecting reach distance and line of sight.
- No server-side mod installation or custom server packets are required.

Always verify the rules of your multiplayer server before enabling automated placement tools.

---

## License & Source

Licensed under the [MIT License](https://github.com/rafayshhh/shhh-printer/blob/main/LICENSE).  
Source code and issue tracker: https://github.com/rafayshhh/shhh-printer
```
