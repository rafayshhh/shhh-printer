# Shhh Printer UI Style Guide & Design System

This document specifies the design system, color palette, component specifications, and responsive layout rules implemented in **Shhh Printer** for Minecraft 26.3.

Shhh Printer directly inherits and shares the design language of [Shhh Builder](https://github.com/rafayshhh/shhh-builder) through its `UiStyle` class.

---

## 1. Design Philosophy

- **Dark Neutral Panels with 1px Borders**: All settings menus and status pills use dark neutral panel fills (`#141619`) with crisp 1px borders (`#30363D`) instead of noisy vanilla textures.
- **Electric Cyan Accent**: Vibrant Cyan (`#00E5FF`, `0xFF00E5FF`) is used intentionally for active elements, selected buttons, and active status indicators.
- **Full 32-bit ARGB Everywhere**: Every color token explicitly defines its full 8-bit alpha channel (`0xFF` for opaque elements, `0xE6` / `0xC0` for deliberate translucency).
- **Zero Jargon Terminology**:
  - Use **"Point & Place"**, never **"crosshair raycast"**.
  - Use **"Needs Support"**, never **"strict adjacency requirement"**.
  - Use **"Mid-air"**, never **"air placement packet injection"**.

---

## 2. Color Palette (32-bit ARGB)

### 2.1 Surfaces & Panels
| Token | Hex ARGB | CSS / Hex | Purpose |
| :--- | :--- | :--- | :--- |
| `BG_SCREEN_OVERLAY` | `0xC0101216` | `#101216` @ 75% | Screen backdrop dimming overlay |
| `BG_PANEL` | `0xE6141619` | `#141619` @ 90% | Main dark container and HUD pill background |
| `BG_CARD` | `0xE61C2024` | `#1C2024` @ 90% | Button background (unhovered) |
| `BG_CARD_HOVER` | `0xE6262B32` | `#262B32` @ 90% | Button background on mouse hover |

### 2.2 Borders (1px)
| Token | Hex ARGB | CSS / Hex | Purpose |
| :--- | :--- | :--- | :--- |
| `BORDER_NORMAL` | `0xFF30363D` | `#30363D` | Standard 1px panel/card border |
| `BORDER_HOVER` | `0xFF58A6FF` | `#58A6FF` | Soft blue border on hovered buttons |
| `BORDER_ACCENT` | `0xFF00E5FF` | `#00E5FF` | Cyan border for active HUD and primary buttons |
| `BORDER_WARNING` | `0xFFFFB300` | `#FFB300` | Warning amber border for missing item state |
| `BORDER_ERROR` | `0xFFFF5252` | `#FF5252` | Destructive or error borders |

### 2.3 Semantic & Accent Colors
| Token | Hex ARGB | CSS / Hex | Purpose |
| :--- | :--- | :--- | :--- |
| `COLOR_ACCENT` | `0xFF00E5FF` | `#00E5FF` | Primary Cyan accent |
| `COLOR_WARNING` | `0xFFFFB300` | `#FFB300` | Missing item warnings, no-placement alert |
| `COLOR_ERROR` | `0xFFFF5252` | `#FF5252` | Disabled state or error text |

### 2.4 Typography Hierarchy
| Token | Hex ARGB | Role | Formatting |
| :--- | :--- | :--- | :--- |
| `TEXT_TITLE` | `0xFFFFFFFF` | Screen header title | Bold, pure white |
| `TEXT_HEADER` | `0xFFF0F6FC` | Section headers and active labels | Crisp off-white |
| `TEXT_BODY` | `0xFFE6EDF3` | Button labels, descriptive text | High-contrast light neutral |
| `TEXT_MUTED` | `0xFF8B949E` | Secondary subtitles and inactive status | Cool gray |

---

## 3. Configuration Screen Specifications (`ShhhPrinterConfigScreen`)

- **Screen Dimensions**: Responsive down to 640x360 window size.
- **Card Width**: `cardW = Math.min(330, width - 24)`.
- **Button Row Heights**: 22px height with 6px vertical spacing.
- **Button Grid Layout**:
  - Row 1: Master Toggle Button (`Auto-Printer: ACTIVE [P]` / `PAUSED [P]`).
  - Row 2: 1-Click Presets (3 columns: `Preset: Survival`, `Preset: Public`, `Preset: Creative`).
  - Row 3: Target Mode & Air Placement (2 columns).
  - Row 4: Speed Profile & Auto-Orient (2 columns).
  - Row 5: Auto-Hotbar & Inventory Swap (2 columns).
  - Row 6: Status HUD & Clear Placed Counter (2 columns).
  - Row 7: Reset to Safe Defaults & Done (2 columns).

---

## 4. Status HUD Element Specifications (`PrinterHudElement`)

- **Position**: Top-left corner (`x = 8, y = 8`).
- **Dimensions**: Dynamic text width + 12px horizontal padding, 9px text height + 8px vertical padding.
- **Styling**: `BG_PANEL` fill with 1px border.
- **States**:
  - Disabled: Gray border and text (`PRINTER: OFF [P]`).
  - No Placement: Amber border and text (`PRINTER: NO BUILD [V]`).
  - Missing Item: Amber border and text (`PRINTER: Missing <Item>`).
  - Active: Cyan accent border and text (`PRINTER: Point & Place (<N> placed)`).
