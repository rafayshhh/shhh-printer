package io.github.rafayshhh.shhhprinter.client.gui;

import io.github.rafayshhh.shhhbuilder.client.gui.UiStyle;
import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import io.github.rafayshhh.shhhprinter.client.engine.PrinterEngine;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Modern, beginner-friendly configuration screen for Shhh Printer.
 */
public class ShhhPrinterConfigScreen extends Screen {
	private static final Component TITLE = Component.literal("Shhh Printer - Settings");

	private final Screen parent;
	private final ShhhPrinterConfig config;

	private UiStyle.StyledButton masterToggleButton;
	private UiStyle.StyledButton targetModeButton;
	private UiStyle.StyledButton airPlaceButton;
	private UiStyle.StyledButton speedButton;
	private UiStyle.StyledButton autoOrientButton;
	private UiStyle.StyledButton autoHotbarButton;
	private UiStyle.StyledButton inventorySwapButton;
	private UiStyle.StyledButton hudToggleButton;
	private UiStyle.StyledButton hudPositionButton;
	private UiStyle.StyledButton presetSurvivalButton;
	private UiStyle.StyledButton presetPublicButton;
	private UiStyle.StyledButton presetCreativeButton;
	private UiStyle.StyledButton resetButton;
	private UiStyle.StyledButton doneButton;

	public ShhhPrinterConfigScreen(Screen parent) {
		super(TITLE);
		this.parent = parent;
		this.config = ShhhPrinterConfig.getInstance();
	}

	@Override
	protected void init() {
		int cardW = Math.min(330, this.width - 24);
		int btnW = (cardW - 8) / 2;
		int presetW = (cardW - 12) / 3;
		int btnH = 22;
		int centerX = this.width / 2;
		int leftX = centerX - (cardW / 2);
		int rightX = leftX + btnW + 8;
		int startY = Math.max(44, (this.height - 230) / 2);

		// Row 1: Master Toggle (Full Width)
		masterToggleButton = new UiStyle.StyledButton(
			leftX, startY, cardW, btnH,
			getMasterToggleText(),
			btn -> {
				config.toggleEnabled();
				config.save();
				refreshButtonLabels();
			},
			config.isEnabled() ? UiStyle.StyledButton.ButtonStyle.PRIMARY : UiStyle.StyledButton.ButtonStyle.NORMAL
		);
		this.addRenderableWidget(masterToggleButton);

		// Row 2: 1-Click Quick Presets (3 buttons)
		int p2X = leftX + presetW + 6;
		int p3X = p2X + presetW + 6;

		presetSurvivalButton = new UiStyle.StyledButton(
			leftX, startY + 28, presetW, btnH,
			Component.literal("Preset: Survival"),
			btn -> {
				config.applyPreset("survival");
				refreshButtonLabels();
				if (this.minecraft != null && this.minecraft.player != null) {
					this.minecraft.player.sendOverlayMessage(Component.literal("Applied Survival Preset (Point & Place, 10 blk/s)"));
				}
			}
		);
		this.addRenderableWidget(presetSurvivalButton);

		presetPublicButton = new UiStyle.StyledButton(
			p2X, startY + 28, presetW, btnH,
			Component.literal("Preset: Public"),
			btn -> {
				config.applyPreset("public");
				refreshButtonLabels();
				if (this.minecraft != null && this.minecraft.player != null) {
					this.minecraft.player.sendOverlayMessage(Component.literal("Applied Public Server Preset (Anti-Cheat Safe, 5 blk/s)"));
				}
			}
		);
		this.addRenderableWidget(presetPublicButton);

		presetCreativeButton = new UiStyle.StyledButton(
			p3X, startY + 28, presetW, btnH,
			Component.literal("Preset: Creative"),
			btn -> {
				config.applyPreset("creative");
				refreshButtonLabels();
				if (this.minecraft != null && this.minecraft.player != null) {
					this.minecraft.player.sendOverlayMessage(Component.literal("Applied Creative Preset (Fast, Air-Place On)"));
				}
			}
		);
		this.addRenderableWidget(presetCreativeButton);

		// Row 3: Target Mode (Crosshair vs Area) & Air Placement
		targetModeButton = new UiStyle.StyledButton(
			leftX, startY + 56, btnW, btnH,
			getTargetModeText(),
			btn -> {
				if (config.getTargetMode() == ShhhPrinterConfig.TargetMode.CROSSHAIR) {
					config.setTargetMode(ShhhPrinterConfig.TargetMode.ALL_IN_REACH);
				} else {
					config.setTargetMode(ShhhPrinterConfig.TargetMode.CROSSHAIR);
				}
				config.save();
				btn.setMessage(getTargetModeText());
			}
		);
		this.addRenderableWidget(targetModeButton);

		airPlaceButton = new UiStyle.StyledButton(
			rightX, startY + 56, btnW, btnH,
			getAirPlaceText(),
			btn -> {
				boolean air = !config.isAirPlacement();
				config.setAirPlacement(air);
				config.setStrictAdjacent(!air);
				config.save();
				btn.setMessage(getAirPlaceText());
			}
		);
		this.addRenderableWidget(airPlaceButton);

		// Row 4: Speed Profile & Auto-Orient
		speedButton = new UiStyle.StyledButton(
			leftX, startY + 84, btnW, btnH,
			getSpeedText(),
			btn -> {
				cycleSpeedProfile();
				config.save();
				btn.setMessage(getSpeedText());
			}
		);
		this.addRenderableWidget(speedButton);

		autoOrientButton = new UiStyle.StyledButton(
			rightX, startY + 84, btnW, btnH,
			getOrientText(),
			btn -> {
				config.setAutoOrientFacing(!config.isAutoOrientFacing());
				config.save();
				btn.setMessage(getOrientText());
			}
		);
		this.addRenderableWidget(autoOrientButton);

		// Row 5: Auto-Select Hotbar & Inventory Swap
		autoHotbarButton = new UiStyle.StyledButton(
			leftX, startY + 112, btnW, btnH,
			getHotbarText(),
			btn -> {
				config.setAutoSwitchHotbar(!config.isAutoSwitchHotbar());
				config.save();
				btn.setMessage(getHotbarText());
			}
		);
		this.addRenderableWidget(autoHotbarButton);

		inventorySwapButton = new UiStyle.StyledButton(
			rightX, startY + 112, btnW, btnH,
			getInventorySwapText(),
			btn -> {
				config.setInventorySwap(!config.isInventorySwap());
				config.save();
				btn.setMessage(getInventorySwapText());
			}
		);
		this.addRenderableWidget(inventorySwapButton);

		// Row 6: Status HUD & Position
		hudToggleButton = new UiStyle.StyledButton(
			leftX, startY + 140, btnW, btnH,
			getHudText(),
			btn -> {
				config.setHudEnabled(!config.isHudEnabled());
				config.save();
				btn.setMessage(getHudText());
			}
		);
		this.addRenderableWidget(hudToggleButton);

		hudPositionButton = new UiStyle.StyledButton(
			rightX, startY + 140, btnW, btnH,
			getHudPosText(),
			btn -> {
				config.setHudPosition(config.getHudPosition().next());
				config.save();
				btn.setMessage(getHudPosText());
			}
		);
		this.addRenderableWidget(hudPositionButton);

		// Row 7: Reset Defaults & Done
		int bottomY = startY + 172;
		resetButton = new UiStyle.StyledButton(
			leftX, bottomY, btnW, btnH,
			Component.literal("Reset to Safe Defaults"),
			btn -> {
				config.applyPreset("survival");
				config.setEnabled(false);
				config.save();
				PrinterEngine.resetStats();
				refreshButtonLabels();
			}
		);
		this.addRenderableWidget(resetButton);

		doneButton = new UiStyle.StyledButton(
			rightX, bottomY, btnW, btnH,
			Component.literal("Done"),
			btn -> onClose(),
			UiStyle.StyledButton.ButtonStyle.PRIMARY
		);
		this.addRenderableWidget(doneButton);
	}

	private void cycleSpeedProfile() {
		int delay = config.getPlacementDelayTicks();
		int blocks = config.getBlocksPerTick();

		if (delay >= 4) {
			// Safe (5 blk/s) -> Normal (10 blk/s)
			config.setBlocksPerTick(1);
			config.setPlacementDelayTicks(2);
		} else if (delay == 2) {
			// Normal (10 blk/s) -> Fast (20 blk/s)
			config.setBlocksPerTick(1);
			config.setPlacementDelayTicks(0);
		} else if (delay == 0 && blocks < 3) {
			// Fast (20 blk/s) -> Instant (Creative)
			config.setBlocksPerTick(3);
			config.setPlacementDelayTicks(0);
		} else {
			// Instant -> Safe (5 blk/s)
			config.setBlocksPerTick(1);
			config.setPlacementDelayTicks(4);
		}
	}

	private void refreshButtonLabels() {
		masterToggleButton.setMessage(getMasterToggleText());
		targetModeButton.setMessage(getTargetModeText());
		airPlaceButton.setMessage(getAirPlaceText());
		speedButton.setMessage(getSpeedText());
		autoOrientButton.setMessage(getOrientText());
		autoHotbarButton.setMessage(getHotbarText());
		inventorySwapButton.setMessage(getInventorySwapText());
		hudToggleButton.setMessage(getHudText());
		hudPositionButton.setMessage(getHudPosText());
	}

	private Component getMasterToggleText() {
		return Component.literal("Auto-Printer: " + (config.isEnabled() ? "ACTIVE [P]" : "PAUSED [P]"));
	}

	private Component getTargetModeText() {
		boolean isCrosshair = (config.getTargetMode() == ShhhPrinterConfig.TargetMode.CROSSHAIR);
		return Component.literal(isCrosshair ? "Target: Point & Place" : "Target: Area 360° (Cheats)");
	}

	private Component getAirPlaceText() {
		return Component.literal(config.isAirPlacement() ? "Air Place: ON (Mid-air)" : "Air Place: OFF (Needs Support)");
	}

	private Component getSpeedText() {
		int delay = config.getPlacementDelayTicks();
		int blocks = config.getBlocksPerTick();
		if (delay == 0 && blocks >= 3) {
			return Component.literal("Speed: Instant (Creative)");
		} else if (delay == 0) {
			return Component.literal("Speed: Fast (20 blk/s)");
		} else if (delay <= 2) {
			return Component.literal("Speed: Normal (10 blk/s)");
		} else {
			return Component.literal("Speed: Safe (5 blk/s)");
		}
	}

	private Component getOrientText() {
		return Component.literal("Auto-Orient: " + (config.isAutoOrientFacing() ? "ON" : "OFF"));
	}

	private Component getHotbarText() {
		return Component.literal("Auto-Hotbar: " + (config.isAutoSwitchHotbar() ? "ON" : "OFF"));
	}

	private Component getInventorySwapText() {
		return Component.literal("Inv Swap: " + (config.isInventorySwap() ? "ON" : "OFF"));
	}

	private Component getHudText() {
		return Component.literal("Status HUD: " + (config.isHudEnabled() ? "SHOWN" : "HIDDEN"));
	}

	private Component getHudPosText() {
		return Component.literal("HUD Pos: " + config.getHudPosition().getDisplayName());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
		// Background tint
		extractor.fill(0, 0, this.width, this.height, UiStyle.BG_SCREEN_OVERLAY);

		Font font = this.font;
		if (font != null) {
			// Title
			int titleW = font.width(TITLE);
			extractor.text(font, TITLE, (this.width - titleW) / 2, 16, UiStyle.TEXT_TITLE);

			// Subtitle / helper note
			Component sub = Component.literal("Point your crosshair at ghost blocks to place them automatically.");
			int subW = font.width(sub);
			extractor.text(font, sub, (this.width - subW) / 2, 28, UiStyle.TEXT_MUTED);
		}

		super.extractRenderState(extractor, mouseX, mouseY, delta);
	}

	@Override
	public void onClose() {
		if (this.minecraft != null && this.minecraft.gui != null) {
			this.minecraft.gui.setScreen(this.parent);
		}
	}
}
