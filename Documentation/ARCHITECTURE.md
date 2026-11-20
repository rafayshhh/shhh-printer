# Shhh Printer - System Architecture

**Target**: Minecraft 26.3, Fabric Loader 0.20.1+, Java 25  
**Version**: `1.0.0`  
**Side**: 100% Client-Side Only (No server components, zero custom network packets)  
**Parent Mod**: [Shhh Builder](https://github.com/rafayshhh/shhh-builder) (`>= 1.0.0`)  

---

## 1. Architectural Overview

Shhh Printer is an automated schematic block placement companion mod designed specifically for **Shhh Builder**. Similar to how community printer extensions (such as *litematica-printer*) augment Litematica, Shhh Printer reads the active in-world schematic placement from Shhh Builder and automates block placement using simulated client player interactions (`useItemOn`).

Rather than placing blocks automatically in a 360-degree sphere around the player, Shhh Printer operates primarily in **Point & Place** crosshair mode: it inspects the ghost block directly targeted by the player's crosshair and places only that specific block.

```
┌────────────────────────────────────────────────────────┐
│                      Shhh Builder                      │
│   (PlacementManager, SchematicManager, LayerMode,      │
│    MaterialCountingEngine, WorldBlockValidator,        │
│          GhostBlockRaycastHelper, UiStyle)             │
└───────────────────────────▲────────────────────────────┘
                            │  (Strict Compile & Runtime Dep)
┌───────────────────────────┴────────────────────────────┐
│                      Shhh Printer                      │
│  ┌───────────────────┐        ┌─────────────────────┐  │
│  │   PrinterEngine   │◄───────┤  ShhhPrinterConfig  │  │
│  │ (Point & Place,   │        │ (Presets, Delay,    │  │
│  │  Sort, useItemOn) │        │  TargetMode, Reach) │  │
│  └─────────┬─────────┘        └─────────────────────┘  │
│            ▼                                           │
│  ┌───────────────────┐        ┌─────────────────────┐  │
│  │ PrinterHudElement │        │ ShhhPrinterConfig   │  │
│  │ (Status, Counts)  │        │        Screen       │  │
│  └───────────────────┘        └─────────────────────┘  │
└────────────────────────────────────────────────────────┘
```

---

## 2. Component Inventory

### A. Lifecycle & Entrypoints
- `ShhhPrinter`: Mod ID declaration (`shhh-printer`) and logging instance.
- `ShhhPrinterClient`: Client entrypoint (`ClientModInitializer`).
  - Verifies that `shhh-builder` is loaded on startup, throwing an explicit `IllegalStateException` if missing.
  - Registers keymappings (`P` for toggle, `O` for settings).
  - Registers client commands (`/shhhprinter`).
  - Registers the HUD element (`PrinterHudElement`) with Fabric's `HudElementRegistry`.
  - Hooks into `ClientTickEvents.END_CLIENT_TICK` to manage placement execution and keybind consumption.
  - Cleans up state and confirmation caches on world disconnect and client stop.
- `ModMenuIntegration`: Implements `ModMenuApi` to provide an in-game configuration screen accessible from Mod Menu.

### B. Core Placement Engine (`PrinterEngine`)
- **Prerequisite Validation**: Operates only when the player is in-world, has an active placement in `Shhh Builder`'s `PlacementManager`, and matches the active dimension.
- **Targeting Modes**:
  - `TargetMode.CROSSHAIR` (Point & Place - Default):
    - Uses `GhostBlockRaycastHelper.raycast(mc)` to detect the exact ghost block targeted by the player's crosshair up to 5.0 blocks away.
    - If no ghost block intersects the ray directly, checks if the player is aiming at a solid world block face whose adjacent position corresponds to a missing schematic block.
    - If the player is looking away or aiming at unrelated blocks, no blocks are queued.
  - `TargetMode.ALL_IN_REACH` (Area 360 - Optional for cheats/creative):
    - Scans all transformed blocks within the reach sphere (`reachDistance`).
    - Sorts candidates primarily by `tb.relY()` ascending (bottom-to-top ground support), and secondarily by distance squared to player eyes.
- **Layer Filtering**:
  - Respects active `LayerMode` (`ALL`, `SINGLE`, `UP_TO`) and `currentLayer` from `Shhh Builder`.
- **Player Collision Guard**:
  - Excludes blocks whose bounding box intersects the player's bounding box (`AABB.intersects`) to prevent suffocation or rubberbanding.
- **World State Verification**:
  - Queries `mc.level.getBlockState(worldPos)` and validates against `WorldBlockValidator.check(...)`. Only targets `MISSING` blocks.
- **Confirmation Cooldown Cache**:
  - Maintains a 350ms per-block cooldown tracking `worldPos.asLong()` to eliminate duplicate placement packet spam before server confirmation.
- **Physical Adjacency & Raycasting (`resolvePlacementHit`)**:
  - *Strict Adjacent Mode (Default)*: Checks 6 neighbors (DOWN, NORTH, SOUTH, EAST, WEST, UP) for an existing solid block. Calculates the touching face and hit vector.
  - *Air Placement Mode*: Allows placing directly into air for servers with Carpet Extra `accurateBlockPlacement` or lenient rules.
- **Item Selection & Inventory Swapping**:
  - Resolves required item IDs via `MaterialCountingEngine.evaluate(expectedState)`.
  - Searches hotbar slots 0–8 and selects the slot (`player.getInventory().setSelectedSlot(slot)`).
  - If absent from hotbar, searches main inventory (slots 9–35) and swaps into the active hotbar slot via `mc.gameMode.handleContainerInput(..., ContainerInput.SWAP)`.
- **Orientation & Facing**:
  - Adjusts hit vectors for slabs (`y + 0.8` vs `y + 0.2`) and stairs.
  - Automatically dispatches `ServerboundMovePlayerPacket.Rot` for horizontal facing blocks (pistons, observers, repeaters).
- **Placement Execution**:
  - Calls `mc.gameMode.useItemOn(...)` and triggers client arm swing (`player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, true)`).

### C. Configuration & Persistence (`ShhhPrinterConfig`)
- File: `config/shhh-printer.json`.
- Atomic persistence using `.tmp` and `StandardCopyOption.ATOMIC_MOVE`.
- Configurable settings:
  - `enabled` (boolean, default: false)
  - `targetMode` (CROSSHAIR / ALL_IN_REACH, default: CROSSHAIR)
  - `blocksPerTick` (int 1–5, default: 1)
  - `placementDelayTicks` (int 0–10, default: 2)
  - `reachDistance` (double 2.0–6.0, default: 4.5)
  - `strictAdjacent` (boolean, default: true)
  - `airPlacement` (boolean, default: false)
  - `autoSwitchHotbar` (boolean, default: true)
  - `inventorySwap` (boolean, default: true)
  - `autoOrientFacing` (boolean, default: true)
  - `hudEnabled` (boolean, default: true)
  - `soundEffects` (boolean, default: true)
  - `breakWrongBlocks` (boolean, default: false)
- **Presets**:
  - `survival` (default): Point & Place targeting, 1 blk/tick, 2 delay ticks (~10 blk/s), air placement OFF, strict support ON, hotbar selection ON, inventory swap ON.
  - `public` (anti-cheat safe): Point & Place targeting, 1 blk/tick, 4 delay ticks (~5 blk/s), air placement OFF, strict support ON, hotbar selection ON, inventory swap OFF.
  - `creative` (fast): Point & Place targeting, 3 blk/tick, 0 delay ticks, air placement ON, strict support OFF, hotbar selection ON, inventory swap ON.

### D. User Interface & HUD
- `PrinterHudElement`: On-screen status pill showing active state, placed count, speed, or missing item alerts.
- `ShhhPrinterConfigScreen`: Graphical configuration screen built with `Shhh Builder`'s `UiStyle` design system.

### E. Client Commands (`/shhhprinter`)
- `/shhhprinter toggle`: Toggle automated placement ON / OFF.
- `/shhhprinter on` / `off`: Enable or disable printer.
- `/shhhprinter target <crosshair|all>`: Switch between Point & Place and Area 360 mode.
- `/shhhprinter preset <survival|public|creative>`: Apply configuration preset profiles.
- `/shhhprinter speed <1-5>`: Set blocks placed per client tick.
- `/shhhprinter delay <0-10>`: Set ticks between placements.
- `/shhhprinter mode <strict|air>`: Switch between Strict Adjacent and Air Placement.
- `/shhhprinter config`: Open the settings screen.
- `/shhhprinter stats`: Display session statistics.
