package io.github.rafayshhh.shhhprinter.client.gui;

import io.github.rafayshhh.shhhbuilder.client.gui.UiStyle;
import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import io.github.rafayshhh.shhhprinter.client.engine.PrinterEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Modern cyan/dark configuration screen for Shhh Printer matching the Shhh Builder UI style.
 */
public class ShhhPrinterConfigScreen extends Screen {
	private static final Component TITLE = Component.translatable("title.shhh-printer.config");

	private final Screen parent;
	private final ShhhPrinterConfig config;

	private UiStyle.StyledButton masterToggleButton;
	private UiStyle.StyledButton speedButton;
	private UiStyle.StyledButton delayButton;
	private UiStyle.StyledButton modeButton;
	private UiStyle.StyledButton autoHotbarButton;
	private UiStyle.StyledButton inventorySwapButton;
	private UiStyle.StyledButton autoOrientButton;
	private UiStyle.StyledButton hudToggleButton;
	private UiStyle.StyledButton resetButton;
	private UiStyle.StyledButton doneButton;

	public ShhhPrinterConfigScreen(Screen parent) {
		super(TITLE);
		this.parent = parent;
		this.config = ShhhPrinterConfig.getInstance();
	}

	@Override
	protected void init() {
		int cardW = Math.min(320, this.width - 32);
		int btnW = (cardW - 8) / 2;
		int btnH = 22;
		int centerX = this.width / 2;
		int leftX = centerX - (cardW / 2);
		int rightX = leftX + btnW + 8;
		int startY = Math.max(48, (this.height - 210) / 2);

		// Row 1: Master Toggle (Full Width)
		masterToggleButton = new UiStyle.StyledButton(
			leftX, startY, cardW, btnH,
			getMasterToggleText(),
			btn -> {
				config.toggleEnabled();
				config.save();
				btn.setMessage(getMasterToggleText());
			},
			config.isEnabled() ? UiStyle.StyledButton.ButtonStyle.PRIMARY : UiStyle.StyledButton.ButtonStyle.NORMAL
		);
		this.addRenderableWidget(masterToggleButton);

		// Row 2: Speed (Blocks/Tick) & Placement Delay
		speedButton = new UiStyle.StyledButton(
			leftX, startY + 28, btnW, btnH,
			getSpeedText(),
			btn -> {
				int next = (config.getBlocksPerTick() % 5) + 1;
				config.setBlocksPerTick(next);
				config.save();
				btn.setMessage(getSpeedText());
			}
		);
		this.addRenderableWidget(speedButton);

		delayButton = new UiStyle.StyledButton(
			rightX, startY + 28, btnW, btnH,
			getDelayText(),
			btn -> {
				int next = (config.getPlacementDelayTicks() + 1) % 6; // 0 to 5
				config.setPlacementDelayTicks(next);
				config.save();
				btn.setMessage(getDelayText());
			}
		);
		this.addRenderableWidget(delayButton);

		// Row 3: Preset Section
		addRenderableWidget(new UiStyle.StyledButton(leftX, startY + 56, 120, btnH,
			Component.literal("Preset: Creative"), btn -> {
				config.applyPreset("creative");
				refreshButtonLabels();
			}));
		addRenderableWidget(new UiStyle.StyledButton(rightX, startY + 56, 120, btnH,
			Component.literal("Preset: Survival"), btn -> {
				config.applyPreset("survival");
				refreshButtonLabels();
			}));
		addRenderableWidget(new UiStyle.StyledButton(leftX, startY + 84, 120, btnH,
			Component.literal("Preset: Public"), btn -> {
				config.applyPreset("public");
				refreshButtonLabels();
			}));

		// Row 4: Toggles
		modeButton = new UiStyle.StyledButton(
			rightX, startY + 84, btnW, btnH,
			getModeText(),
			btn -> {
				boolean air = !config.isAirPlacement();
				config.setAirPlacement(air);
				config.setStrictAdjacent(!air);
				config.save();
				btn.setMessage(getModeText());
			}
		);
		this.addRenderableWidget(modeButton);

		// Row 4: Auto-Select Hotbar & Inventory Swap
		autoHotbarButton = new UiStyle.StyledButton(
			leftX, startY + 84, btnW, btnH,
			getHotbarText(),
			btn -> {
				config.setAutoSwitchHotbar(!config.isAutoSwitchHotbar());
				config.save();
				btn.setMessage(getHotbarText());
			}
		);
		this.addRenderableWidget(autoHotbarButton);

		inventorySwapButton = new UiStyle.StyledButton(
			rightX, startY + 84, btnW, btnH,
			getInventorySwapText(),
			btn -> {
				config.setInventorySwap(!config.isInventorySwap());
				config.save();
				btn.setMessage(getInventorySwapText());
			}
		);
		this.addRenderableWidget(inventorySwapButton);

		// Row 5: HUD Display & Reset Stats
		hudToggleButton = new UiStyle.StyledButton(
			leftX, startY + 112, btnW, btnH,
			getHudText(),
			btn -> {
				config.setHudEnabled(!config.isHudEnabled());
				config.save();
				btn.setMessage(getHudText());
			}
		);
		this.addRenderableWidget(hudToggleButton);

		UiStyle.StyledButton clearStatsButton = new UiStyle.StyledButton(
			rightX, startY + 112, btnW, btnH,
			Component.literal("Reset Stats"),
			btn -> {
				PrinterEngine.resetStats();
				if (this.minecraft != null && this.minecraft.player != null) {
					this.minecraft.player.sendOverlayMessage(Component.literal("Printer stats cleared"));
				}
			}
		);
		this.addRenderableWidget(clearStatsButton);

		// Bottom row: Reset Defaults & Done
		int bottomY = startY + 152;
		resetButton = new UiStyle.StyledButton(
			leftX, bottomY, btnW, btnH,
			Component.literal("Reset Defaults"),
			btn -> {
				config.setEnabled(false);
				config.setBlocksPerTick(1);
				config.setPlacementDelayTicks(0);
				config.setReachDistance(4.5);
				config.setStrictAdjacent(false);
				config.setAirPlacement(true);
				config.setAutoSwitchHotbar(true);
				config.setInventorySwap(true);
				config.setAutoOrientFacing(true);
				config.setHudEnabled(true);
				config.setSoundEffects(true);
				config.save();
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

	private void refreshButtonLabels() {
		masterToggleButton.setMessage(getMasterToggleText());
		speedButton.setMessage(getSpeedText());
		delayButton.setMessage(getDelayText());
		modeButton.setMessage(getModeText());
		autoOrientButton.setMessage(getOrientText());
		autoHotbarButton.setMessage(getHotbarText());
		inventorySwapButton.setMessage(getInventorySwapText());
		hudToggleButton.setMessage(getHudText());
	}

	private Component getMasterToggleText() {
		return Component.literal("Auto-Printer: " + (config.isEnabled() ? "ENABLED" : "DISABLED"));
	}

	private Component getSpeedText() {
		return Component.literal("Speed: " + config.getBlocksPerTick() + " blk/tick");
	}

	private Component getDelayText() {
		return Component.literal("Delay: " + config.getPlacementDelayTicks() + " ticks");
	}

	private Component getModeText() {
		return Component.literal("Mode: " + (config.isAirPlacement() ? "Air Place" : "Strict Adjacent"));
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

	@Override
	public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
		// Background tint
		extractor.fill(0, 0, this.width, this.height, UiStyle.BG_SCREEN_OVERLAY);

		Font font = this.font;
		if (font != null) {
			// Title
			int titleW = font.width(TITLE);
			extractor.text(font, TITLE, (this.width - titleW) / 2, 20, UiStyle.TEXT_TITLE);

			// Subtitle / helper note
			Component sub = Component.literal("Supports Shhh Builder schematics with automatic placement");
			int subW = font.width(sub);
			extractor.text(font, sub, (this.width - subW) / 2, 32, UiStyle.TEXT_MUTED);
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
