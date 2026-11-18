package io.github.rafayshhh.shhhprinter.client.hud;

import io.github.rafayshhh.shhhbuilder.client.PlacementManager;
import io.github.rafayshhh.shhhbuilder.client.gui.UiStyle;
import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import io.github.rafayshhh.shhhprinter.client.engine.PrinterEngine;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

/**
 * On-screen HUD element showing active printer status, placed blocks count, and missing item warnings.
 */
public class PrinterHudElement implements HudElement {

	@Override
	public void extractRenderState(GuiGraphicsExtractor extractor, DeltaTracker deltaTracker) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		if (!config.isHudEnabled()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc == null || mc.player == null) {
			return;
		}

		// Hide when any screen or menu is open
		if (mc.gui != null && mc.gui.screen() != null) {
			return;
		}

		// Hide when F1 hide-HUD key is used
		if (mc.gui != null && mc.gui.hud != null && mc.gui.hud.isHidden()) {
			return;
		}

		Font font = mc.font;
		if (font == null) {
			return;
		}

		boolean enabled = config.isEnabled();
		boolean hasPlacement = PlacementManager.hasPlacement();

		String text;
		int textColor;
		int borderColor;

		if (!enabled) {
			text = "PRINTER: OFF [P]";
			textColor = UiStyle.TEXT_MUTED;
			borderColor = UiStyle.BORDER_NORMAL;
		} else if (!hasPlacement) {
			text = "PRINTER: NO BUILD [V]";
			textColor = UiStyle.COLOR_WARNING;
			borderColor = UiStyle.BORDER_WARNING;
		} else if (PrinterEngine.getCurrentMissingItem() != null) {
			text = "PRINTER: " + PrinterEngine.getLastStatusMessage();
			textColor = UiStyle.COLOR_WARNING;
			borderColor = UiStyle.BORDER_WARNING;
		} else {
			int placed = PrinterEngine.getTotalBlocksPlaced();
			int speed = config.getBlocksPerTick();
			text = "PRINTER: ACTIVE (" + placed + " placed | " + speed + " blk/t)";
			textColor = UiStyle.COLOR_ACCENT;
			borderColor = UiStyle.BORDER_ACCENT;
		}

		int textWidth = font.width(text);
		int paddingX = 6;
		int paddingY = 4;
		int boxWidth = textWidth + (paddingX * 2);
		int boxHeight = font.lineHeight + (paddingY * 2);

		int x = 8;
		int y = 8;

		// Draw card background
		extractor.fill(x, y, x + boxWidth, y + boxHeight, UiStyle.BG_PANEL);

		// Draw 1px border
		extractor.fill(x, y, x + boxWidth, y + 1, borderColor);
		extractor.fill(x, y + boxHeight - 1, x + boxWidth, y + boxHeight, borderColor);
		extractor.fill(x, y, x + 1, y + boxHeight, borderColor);
		extractor.fill(x + boxWidth - 1, y, x + boxWidth, y + boxHeight, borderColor);

		// Draw text
		extractor.text(font, Component.literal(text), x + paddingX, y + paddingY, textColor);
	}
}
