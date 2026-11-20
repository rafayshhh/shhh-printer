# Changelog

All notable changes to the "Shhh Printer" project will be documented in this file.

## [1.0.0] - Official Release - Automated Companion for Shhh Builder

**Shhh Printer v1.0.0** is the official release for Minecraft 26.3 on Fabric (Client-Only). It provides an automated, aim-assisted block placement assistant that seamlessly integrates with [Shhh Builder](https://github.com/rafayshhh/shhh-builder) schematics.

### Summary of Features

- **Hard Integration with Shhh Builder**:
  - Directly couples with Shhh Builder's `PlacementManager`, `SchematicManager`, `LayerMode`, and `WorldBlockValidator`.
  - Automatically respects active build positions, layer slicing modes (All Layers, Single Layer, Up To), rotations, and mirroring.

- **Point & Place Crosshair Targeting (Default Mode)**:
  - Uses `GhostBlockRaycastHelper.raycast(mc)` to detect the exact ghost block targeted by the player's crosshair.
  - Places only the specific block the player is actively pointing at. Looking away stops placement immediately, preventing aura-style automated placement.
  - Includes an optional secondary Area 360 mode (`/shhhprinter target all`) for singleplayer or creative testing.

- **Safe Fair-Play Defaults**:
  - Air placement is disabled by default (`airPlacement = false`).
  - Strict adjacent support is enabled by default (`strictAdjacent = true`), requiring a solid supporting block face mirroring vanilla Minecraft mechanics.
  - Default placement cadence set to 1 block per tick with a 2-tick delay (~10 blocks/sec), matching legit human building rhythms.

- **Tiered Speed & Safety Presets**:
  - **Survival (Default)**: Point & Place targeting, 1 blk/tick, 2-tick delay (~10 blk/s), air placement OFF, strict support ON, hotbar selection ON, inventory swap ON.
  - **Public Server (Safe)**: Point & Place targeting, 1 blk/tick, 4-tick delay (~5 blk/s), air placement OFF, strict support ON, hotbar selection ON, inventory swap OFF (prevents rapid container swap packets).
  - **Creative (Fast)**: Point & Place targeting, 3 blk/tick, 0-tick delay, air placement ON, strict support OFF, auto-equip 64-item stacks ON.

- **Physical Adjacency & Raycasting Engine**:
  - Evaluates 6 neighbor faces (DOWN, NORTH, SOUTH, WEST, EAST, UP) to locate solid block support.
  - Computes exact sub-block hit vectors for slabs (bottom vs top) and stairs (normal vs upside-down).
  - Sends look rotation packets (`ServerboundMovePlayerPacket.Rot`) for directional blocks (pistons, observers, repeaters, stairs) without snapping the player's camera view.

- **Intelligent Inventory Management**:
  - Uses `MaterialCountingEngine.evaluate(state)` to identify required carried items.
  - Automatically selects matching items from hotbar slots 0-8.
  - Automatically swaps items from main inventory (slots 9-35) into hotbar when running low.
  - Cleanly skips blocks missing from inventory in Survival mode without jamming or halting the printer.

- **Player Collision Guard & Anti-Spam**:
  - Disallows placing solid blocks intersecting the player's own bounding box, preventing suffocation or rubberbanding.
  - Implements a 350ms per-block confirmation cache to eliminate duplicate placement packets.

- **User Interface & In-Game Controls**:
  - Simplified settings screen (`O`) built on Shhh Builder's dark neutral design system with 1-click preset buttons and zero technical jargon.
  - On-screen status HUD element (`PrinterHudElement`) displaying target mode, speed, placed block count, or missing item alerts.
  - Keybindings for Toggle (`P`) and Config (`O`).

- **Full Client-Side Command Suite (`/shhhprinter`)**:
  - `/shhhprinter toggle`: Toggle automated placement ON / OFF.
  - `/shhhprinter on` / `off`: Enable or disable printer.
  - `/shhhprinter target <crosshair|all>`: Switch between Point & Place and Area 360 mode.
  - `/shhhprinter preset <survival|public|creative>`: Apply configuration presets.
  - `/shhhprinter speed <1-5>`: Set blocks placed per tick.
  - `/shhhprinter delay <0-10>`: Set tick delay between placements.
  - `/shhhprinter mode <strict|air>`: Switch between Strict Adjacent and Air Placement.
  - `/shhhprinter config`: Open the graphical settings screen.
  - `/shhhprinter stats`: Display session statistics.
  - `/shhhprinter help`: Display commands reference.
