# CurseForge Project Metadata and Setup Guide

- Mod Name: Shhh Printer
- Author: RafayShhh (https://curseforge.com/members/rafayshhh)
- Target Version: Minecraft 26.3 (Fabric)
- Companion Mod: [Shhh Builder](https://curseforge.com/minecraft/mc-mods/shhh-builder) (Required Dependency)

---

## Project Details

| Setting Field | Value to Enter | Notes |
| :--- | :--- | :--- |
| Project Name | Shhh Printer | Display name on CurseForge. |
| Category | Primary: Utility & QoL<br>Secondary: Map and Information, Creative Mode | Select these in the project creation form. |
| Short Description / Summary | Automated schematic block placement companion mod for Shhh Builder on Minecraft 26.3 Fabric. Features aim-assisted Point and Place crosshair targeting, anti-cheat safe defaults, and speed presets. | 199 characters (CurseForge requires between 100 and 250 characters). |
| Project Visibility | Public | Standard public release. |
| Environment | Client | Pure client-side mod. |
| License | MIT License | Choose MIT from dropdown. |
| Source Code Link | https://github.com/rafayshhh/shhh-printer | GitHub repository link. |
| Issues / Bug Tracker Link | https://github.com/rafayshhh/shhh-printer/issues | GitHub issues link. |
| Project Logo / Avatar | Upload Documentation/showcase/icon.png | 1024x1024 PNG (CurseForge requires at least 400x400). |

---

## Screenshots (Images Tab)

Upload the showcase files located in `Documentation/showcase/` (or `Assets/`):

1. `showcase-banner.jpg` (or `Assets/banner.jpg`)
   - Title: Shhh Printer Official Banner
   - Description: Official project banner showcasing the holographic 3D printer building schematic structures.

2. `showcase-config.jpg` (or `Assets/showcase-config.jpg`)
   - Title: Shhh Printer In-Game Configuration Screen
   - Description: Grouped settings screen managing Behavior & Targets, Speed & Orientation, and HUD display.

3. `icon.png` (or `Assets/icon.png`)
   - Title: Shhh Printer Official Emblem
   - Description: High-resolution modern neon blue 3D printer logo.

---

## File Upload (Files Tab)

| Field | Value |
| :--- | :--- |
| Upload Binary | `26.3/build/libs/shhh-printer-1.0.0.jar` |
| Display Name | Shhh Printer 1.0.0 (Fabric 26.3) |
| Release Type | Release |
| Supported Minecraft Version | Select 26.3 |
| Mod Loader | Select Fabric |
| Java Version | Java 25 |

### Dependencies
Under the **Dependencies** section for the uploaded file:
- **Shhh Builder**: Set as **Required Dependency** (https://curseforge.com/minecraft/mc-mods/shhh-builder)
- **Fabric API**: Set as **Required Dependency**
- **Mod Menu**: Set as **Optional Dependency**

### File Changelog
```markdown
### Shhh Printer 1.0.0 for Minecraft 26.3 (Fabric)

Debut release for Minecraft 26.3 on Fabric. Client-side automated placement companion for Shhh Builder.

- Point & Place Crosshair Mode: Places only the specific schematic block your crosshair is aimed at.
- Fair-Play Defaults: Air placement disabled by default; strict adjacent support required.
- Operational Presets: 1-click Survival (10 blk/s), Public Server (5 blk/s), and Creative speed profiles.
- Smart Inventory: Automatically switches hotbar slots and restocks blocks from your main inventory.
- Collision & Safety Guards: Never places blocks inside the player hitbox; pauses during mining, death, or sleep.
- Client Commands: Complete /shhhprinter command suite operating purely on the client side.
```

---

## CurseForge Description (Copy and Paste into CurseForge)

```markdown
# Shhh Printer

**An automated schematic block-placement companion mod for [Shhh Builder](https://curseforge.com/minecraft/mc-mods/shhh-builder).**  
*Aim-assisted Point & Place crosshair targeting • Built-in speed presets • Fair-play defaults • Zero aura cheating*

---

### Overview

**Shhh Printer** is a client-side automated building companion designed specifically for **[Shhh Builder](https://curseforge.com/minecraft/mc-mods/shhh-builder)** on Minecraft 26.3 (Fabric).

Unlike conventional schematic printers that deploy full 360-degree aura placement around the player, Shhh Printer defaults to an intuitive **Point & Place** targeting system: it places only the specific ghost block your crosshair is aimed at. Point your crosshair at an unplaced schematic block within reach, and Shhh Printer handles the placement, orientation, and hotbar selection with legitimate human-like timing. Look away, and placement immediately stops.

---

### Requirements

**Shhh Printer requires [Shhh Builder](https://curseforge.com/minecraft/mc-mods/shhh-builder) (1.0.0 or newer).**

Shhh Builder provides the holographic schematic rendering engine, file parsing (`.litematic` and `.schem`), layer slicing, and materials tracking. Shhh Printer reads these active ghost block placements and assists you in placing the physical blocks.

---

### Key Features

#### 1. Point & Place Targeting (Default)
- **Aim-Assisted Placement**: The printer inspects the ghost block in your crosshair and places only that block when you are looking at it. If you look away, no blocks are placed.
- **Fair-Play Default**: Air placement is disabled by default. Blocks require valid adjacent block support, mirroring vanilla placement rules.
- **Optional Area Mode**: For creative or singleplayer testing, Area 360 mode can be toggled to automatically place all missing blocks within reach (`/shhhprinter target all`).

#### 2. Built-in Speed & Safety Presets
Switch between pre-configured operational profiles via chat (`/shhhprinter preset <survival|public|creative>`) or the graphical settings menu:
- **Survival (Default)**: 1 block/tick, 2-tick delay (~10 blocks/sec legit cadence), air placement disabled, strict adjacent support enabled, auto hotbar selection enabled.
- **Public Server (Safe)**: 1 block/tick, 4-tick delay (~5 blocks/sec anti-cheat safe cadence), air placement disabled, strict adjacent support enabled, inventory swaps disabled to avoid rapid packet rates.
- **Creative (Fast)**: 3 blocks/tick, 0-tick delay, air placement enabled, strict adjacent support disabled.

#### 3. Intelligent Inventory Management
- **Hotbar Auto-Switching**: Automatically selects the matching block from your hotbar when pointing at a ghost block.
- **Inventory Swapping**: Automatically moves blocks from main inventory to hotbar when running out.
- **Auto-Facing Calculation**: Calculates placement vectors for slabs, stairs, logs, and directional blocks based on crosshair hit angle.

#### 4. Player Safety & Collision Checks
- **Collision Protection**: Strictly refuses to place blocks inside player bounding boxes to prevent suffocation.
- **Player State Safety**: Automatically halts placement when you are dead, in spectator mode, sleeping, or actively mining blocks.

#### 5. Configurable Status HUD
- Clean on-screen overlay showing printer status, speed, target mode, and block counts.
- Relocatable to any screen corner (defaults to Top-Right to avoid colliding with Shhh Builder's Top-Left HUD).

---

### Controls

Configure keybindings in **Options -> Controls -> Key Binds -> Shhh Printer**:

| Action | Default Key | Category | What it does |
| :--- | :---: | :--- | :--- |
| **Toggle Auto-Printer** | **P** | Shhh Printer | Turn automated block placement on or off |
| **Open Configuration** | **O** | Shhh Printer | Open the graphical configuration screen |

---

### Client-Side Commands (`/shhhprinter`)

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

### Installation

1. Install **Fabric Loader** (`0.20.1` or newer) for **Minecraft 26.3**.
2. Install **Fabric API** for 26.3.
3. Install **[Shhh Builder](https://curseforge.com/minecraft/mc-mods/shhh-builder)**.
4. Download `shhh-printer-1.0.0.jar` and place it in your `.minecraft/mods` directory.
5. Launch Minecraft.

---

### Server Compliance

Shhh Printer runs entirely on the client side. In its default **Survival** and **Public Server** presets:
- Blocks are placed one at a time using legitimate client interaction rays.
- Placements require a physical supporting block adjacent to the target position.
- Blocks are placed exclusively where the player is looking, respecting reach distance and line of sight.
- No server-side mod installation or custom server packets are required.

Always verify the rules of your multiplayer server before enabling automated placement tools.

---

### License & Source

Licensed under the [MIT License](https://github.com/rafayshhh/shhh-printer/blob/main/LICENSE).  
Source code and issue tracker: https://github.com/rafayshhh/shhh-printer
```
