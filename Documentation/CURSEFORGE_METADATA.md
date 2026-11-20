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
| Short Description / Summary | Automated schematic block placement companion mod for Shhh Builder on Minecraft 26.3 Fabric. Features aim-assisted Point and Place crosshair targeting, anti-cheat safe defaults, and speed presets. | 204 characters (CurseForge requires between 100 and 250 characters). |
| Project Visibility | Public | Standard public release. |
| Environment | Client | Pure client-side mod. |
| License | MIT License | Choose MIT from dropdown. |
| Source Code Link | https://github.com/rafayshhh/shhh-printer | GitHub repository link. |
| Issues / Bug Tracker Link | https://github.com/rafayshhh/shhh-printer/issues | GitHub issues link. |
| Project Logo / Avatar | Upload Assets/icon.png | Square PNG icon. |

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

Official debut release for Minecraft 26.3 on Fabric. Companion mod for Shhh Builder.

- Point & Place Crosshair Mode: Places only the specific schematic block your crosshair is aimed at.
- Fair-Play Defaults: Air placement disabled by default; strict adjacent support required.
- Operational Presets: 1-click Survival, Public Server, and Creative speed presets.
- Smart Inventory: Automatically switches hotbar slots and swaps from main inventory.
- Collision Safety: Never places blocks inside the player hitbox to prevent suffocation.
- Client Commands: Complete /shhhprinter command suite operating purely on the client side.
```
