# Release & Version Details - Shhh Printer v1.0.0

This document contains technical specifications, binary verification checksums, dependency requirements, and release notes for the official v1.0.0 debut of Shhh Printer.

---

## Technical Specifications

| Field | Value | Notes |
| :--- | :--- | :--- |
| **Mod Name** | Shhh Printer | Public display name |
| **Mod ID / Namespace** | `shhh-printer` | Internal mod identifier |
| **Release Version** | `1.0.0` | Initial production debut |
| **Release Channel** | Stable Release | Production-ready |
| **Minecraft Compatibility** | `26.3` | Targeted Minecraft release |
| **Mod Loader** | `Fabric Loader` | Version `>= 0.20.1` |
| **Fabric API** | Compatible with 26.3 | Required dependency |
| **Java Runtime** | `Java 25` | 64-bit JDK/JRE 25 (LTS) |
| **Environment** | Client-Side Only | `Client: Required`, `Server: Unsupported` |
| **License** | MIT License | Open source |
| **Source Repository** | https://github.com/rafayshhh/shhh-printer | Official GitHub repository |
| **Issue Tracker** | https://github.com/rafayshhh/shhh-printer/issues | Bug reporting and feature requests |
| **Git Commit** | `9db4f92` | Verified clean tree commit |
| **Git Tag** | `v1.0.0` | Production release tag |

---

## Binary Artifact & Verification

- **Compiled JAR Name**: `shhh-printer-1.0.0.jar`
- **File Size**: `1,690,768 bytes` (`~1.61 MB`)
- **SHA-256 Hash**:
  ```
  57606F726C1CE79F9067E7ED222B62D7967B7994C4A0D264F9E348D48032C306
  ```
- **Local Artifact Locations**:
  - Build Output: `26.3/build/libs/shhh-printer-1.0.0.jar`
  - Active Test Instance: `.minecraft/versions/Custom Mods Testing/mods/shhh-printer-1.0.0.jar`
  - Production Modpack: `.minecraft/versions/RafayShhh 26.3 MODPACK/mods/shhh-printer-1.0.0.jar`

---

## Dependency Matrix

| Dependency | Required / Optional | Minimum Version | Source / Link |
| :--- | :--- | :--- | :--- |
| **Shhh Builder** | **Required** | `>= 1.0.0` | https://github.com/rafayshhh/shhh-builder |
| **Fabric Loader** | **Required** | `>= 0.20.1` | https://fabricmc.net |
| **Fabric API** | **Required** | 26.3 Build | https://modrinth.com/mod/fabric-api |
| **Mod Menu** | Optional | Any 26.3 version | https://modrinth.com/mod/modmenu |
| **Sodium / Iris** | Compatible | Any 26.3 version | Fully compatible; zero mixin conflicts |

---

## Release Highlights (v1.0.0)

### 1. Aim-Assisted Point & Place Architecture
- Operates primarily through crosshair hit-testing on holographic ghost blocks rather than full 360-degree aura cheating.
- Looking away immediately suspends block placement; aiming at ghost blocks resumes placement with human-like timing.

### 2. Fair-Play Defaults
- Air placement is disabled by default (`airPlacement = false`), requiring genuine solid surface contact.
- Strict adjacent support is enforced by default (`strictAdjacent = true`).

### 3. Integrated Speed & Safety Presets
- **Survival (Default)**: 1 blk/tick, 2-tick delay (~10 blocks/sec legit human speed), hotbar auto-switching enabled, main inventory swap enabled.
- **Public Server (Safe)**: 1 blk/tick, 4-tick delay (~5 blocks/sec anti-cheat safe speed), inventory swaps disabled to avoid rapid container packets.
- **Creative (Fast)**: 3 blk/tick, 0-tick delay, air placement enabled, strict support disabled for rapid testing.

### 4. Smart Inventory Engine
- Automatically selects matching blocks from hotbar slots 0-8 and syncs with `ServerboundSetCarriedItemPacket`.
- Automatically restocks depleted hotbar slots from the player's main inventory (slots 9-35).
- Calculates exact sub-block placement vectors for slabs, stairs, logs, and directional blocks.

### 5. Player State & Collision Protection
- Completely prevents placing blocks intersecting the player's bounding box to eliminate suffocation and rubberbanding.
- Automatically halts placement when the player is dead, in spectator mode, sleeping, or actively mining blocks.

### 6. Modern Dark Neutral Configuration Screen
- In-game settings GUI (`O` or `/shhhprinter config`) matching Shhh Builder's dark slate palette and electric cyan border accents.
- 1-click preset switching, speed adjustment, and repositionable status overlay HUD (Top-Right, Top-Left, Bottom-Right, Bottom-Left).
