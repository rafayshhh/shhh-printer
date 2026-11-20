# Shhh Printer - In-Game Testing Guide (Release 1.0.0)

Welcome to the beginner-friendly testing guide for **Shhh Printer v1.0.0**! This guide is written for players and testers to verify every feature of the mod directly in the Minecraft client.

**Required Companion Mod**: [Shhh Builder](https://github.com/rafayshhh/shhh-builder) (`shhh-builder-1.0.0.jar`) must be installed in your `mods` folder.

---

## 1. Setup & Installation

1. Locate your Minecraft installation folder:
   - **Windows**: `%APPDATA%\.minecraft` (typically `C:\Users\<YourUsername>\AppData\Roaming\.minecraft`)
   - **macOS**: `~/Library/Application Support/minecraft`
   - **Linux**: `~/.minecraft`
2. Open the `mods` folder inside `.minecraft`:
   - Ensure **`shhh-builder-1.0.0.jar`** is present.
   - Copy **`shhh-printer-1.0.0.jar`** into your `mods` folder.
3. Launch Minecraft 26.3 with Fabric Loader (0.20.1 or newer).
4. On the title screen, open **Mods** (via Mod Menu) and confirm:
   - **Shhh Printer** is listed.
   - Version reads **`1.0.0`**.
   - **Shhh Builder** is listed as a loaded dependency.

---

## 2. Test 1: Hard Dependency Verification

1. Temporarily remove `shhh-builder-1.0.0.jar` from your `mods` folder.
2. Attempt to launch Minecraft with only `shhh-printer-1.0.0.jar`.
3. Verify that Fabric Loader halts during startup with an explicit dependency error stating that `shhh-builder` is missing.
4. Move `shhh-builder-1.0.0.jar` back into your `mods` folder and launch Minecraft successfully.

---

## 3. Test 2: Startup & Safe Default State

1. Join a single-player Creative or Survival world.
2. Look at the top-left corner of the screen:
   - An on-screen HUD pill reads: `PRINTER: OFF [P]` in muted gray.
3. Press **O** to open the Shhh Printer Settings screen:
   - Verify the master toggle button reads: `Auto-Printer: PAUSED [P]`.
   - Verify `Target: Point & Place` is selected.
   - Verify `Air Place: OFF (Needs Support)` is set.
   - Verify `Speed: Normal (10 blk/s)` is set.
   - Verify `Auto-Orient: ON` is set.
   - Verify `Auto-Hotbar: ON` is set.
   - Verify `Inv Swap: ON` is set.
   - Verify `Status HUD: SHOWN` is set.
4. Click **Done** or press **Esc** to return to the game.

---

## 4. Test 3: Point & Place Crosshair Placement (Fair Play Default)

1. Press **V** to open Shhh Builder and place a schematic (for example, `sample_house`).
2. Press **P** to enable the printer:
   - An action bar message appears: `Shhh Printer: Enabled` in green.
   - The HUD pill updates to: `PRINTER: Point & Place (0 placed)` in bright cyan.
3. Stand a few blocks away and look away into the empty sky:
   - Confirm that **zero blocks are placed**.
4. Equip cobblestone or oak planks in your hotbar or inventory.
5. Aim your crosshair directly at a missing ghost block on the ground level:
   - The printer immediately selects the matching block and places that specific block with an arm swing animation.
   - The HUD counter increments: `PRINTER: Point & Place (1 placed)`.
6. Slowly sweep your crosshair across adjacent ghost blocks on the floor:
   - Blocks are placed one-by-one as your crosshair touches each ghost block.
7. Aim your crosshair into the sky or at an already-placed block:
   - Placement stops immediately.

---

## 5. Test 4: Strict Adjacency vs Air Placement

1. Locate a floating ghost block (such as an upper roof block with no supporting blocks underneath or beside it).
2. Aim your crosshair at the floating ghost block:
   - Confirm that the block is **NOT** placed, because strict adjacent support is enabled by default.
3. Open chat and type:
   `/shhhprinter mode air`
   - A feedback message appears: `Shhh Printer: Mode set to Air Placement`.
4. Aim your crosshair at the floating ghost block:
   - The printer immediately places the floating block mid-air.
5. Re-enable safe mode by typing:
   `/shhhprinter mode strict`
   - A feedback message confirms strict adjacent placement is restored.

---

## 6. Test 5: Speed & Safety Presets

1. Open the settings screen by pressing **O** (or type `/shhhprinter config`).
2. Click **[Preset: Public]**:
   - Overlay message appears: `Applied Public Server Preset (Anti-Cheat Safe, 5 blk/s)`.
   - The speed button displays `Speed: Safe (5 blk/s)`.
   - `Inv Swap: OFF` is set to prevent rapid container clicks.
3. Click **[Preset: Creative]**:
   - Overlay message appears: `Applied Creative Preset (Fast, Air-Place On)`.
   - The speed button displays `Speed: Instant (Creative)`.
   - `Air Place: ON (Mid-air)` is enabled.
4. Click **[Preset: Survival]**:
   - Restores Point & Place targeting, 10 blk/s cadence, air placement OFF, strict support ON, and inventory swap ON.
5. Click **Done** to close the screen.
6. Alternatively, test the command equivalents in chat:
   - `/shhhprinter preset public`
   - `/shhhprinter preset creative`
   - `/shhhprinter preset survival`

---

## 7. Test 6: Inventory Auto-Switch & Inventory Swapping

1. Switch your game mode to Survival (`/gamemode survival`).
2. Clear your hotbar except for an empty slot in slot 1.
3. Place a stack of Oak Planks in your main inventory (slot 15).
4. Aim your crosshair at an oak plank ghost block:
   - The printer automatically moves the oak planks from main inventory into slot 1 and places the block.
5. Empty your inventory completely of oak planks and aim at an oak plank ghost block:
   - Placement does not jam.
   - The HUD updates with an amber warning: `PRINTER: Missing Oak Planks`.
   - Once oak planks are collected back into inventory, placement resumes automatically.

---

## 8. Test 7: Directional Facing (Slabs & Stairs)

1. Aim at a top-slab ghost block:
   - Confirm the hit vector targets the upper half (`y + 0.8`), placing a top slab correctly.
2. Aim at a stair ghost block:
   - Confirm the block is placed with the exact orientation matching the schematic without manually rotating your player view.

---

## 9. Test 8: Client Commands Verification

Test all subcommands in chat:
- `/shhhprinter toggle`: Toggles printer state between ACTIVE and PAUSED.
- `/shhhprinter off`: Explicitly disables printer.
- `/shhhprinter on`: Explicitly enables printer.
- `/shhhprinter target crosshair`: Selects Point & Place mode.
- `/shhhprinter target all`: Selects Area 360 mode.
- `/shhhprinter speed 2`: Sets speed to 2 blocks per tick.
- `/shhhprinter delay 4`: Sets placement delay to 4 ticks.
- `/shhhprinter stats`: Outputs full status report in chat (Status, Target Mode, Placement State, Placed Count, Engine State, Speed, Air Placement).
- `/shhhprinter help`: Lists all available commands with descriptions.
