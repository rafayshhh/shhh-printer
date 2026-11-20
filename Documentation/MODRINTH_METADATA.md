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
| Project Icon | Upload Assets/icon.png | Square PNG icon. |

---

## Dependencies Configuration

In the Modrinth project settings:
- **Shhh Builder**: Required Dependency (Slug: `shhh-builder`, URL: https://modrinth.com/mod/shhh-builder)
- **Fabric API**: Required Dependency
- **Mod Menu**: Optional Dependency

---

## Version Description

```markdown
Shhh Printer 1.0.0 is the official companion mod for Shhh Builder on Minecraft 26.3 Fabric.

Key Highlights:
- Point & Place Crosshair Aiming: Targets only the ghost block you are actively pointing at.
- Safe Defaults: Strict adjacent block support required; air placement turned off by default.
- Speed Presets: Easily switch between Survival (10 blk/s), Public Server (5 blk/s), and Creative profiles.
- Inventory Management: Automatically selects hotbar blocks and swaps items from main inventory.
- Clean UI & HUD: Modern dark-neutral settings screen and on-screen status overlay.
- Zero Packets: Client-side simulated player interactions with no server-side mod required.
```
