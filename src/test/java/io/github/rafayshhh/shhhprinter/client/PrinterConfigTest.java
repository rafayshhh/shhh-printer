package io.github.rafayshhh.shhhprinter.client;

import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrinterConfigTest {

	@Test
	void testClampingAndDefaults() {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		config.applyPreset("survival");
		config.setEnabled(false);

		// Safe Fair-Play Default values
		assertFalse(config.isEnabled());
		assertEquals(ShhhPrinterConfig.TargetMode.CROSSHAIR, config.getTargetMode());
		assertEquals(1, config.getBlocksPerTick());
		assertEquals(2, config.getPlacementDelayTicks());
		assertEquals(4.5, config.getReachDistance(), 0.001);
		assertTrue(config.isStrictAdjacent());
		assertFalse(config.isAirPlacement());
		assertTrue(config.isAutoSwitchHotbar());
		assertTrue(config.isInventorySwap());
		assertEquals(ShhhPrinterConfig.HudPosition.TOP_RIGHT, config.getHudPosition());

		// Test clamping
		config.setBlocksPerTick(100);
		assertEquals(5, config.getBlocksPerTick());

		config.setBlocksPerTick(-10);
		assertEquals(1, config.getBlocksPerTick());

		config.setPlacementDelayTicks(50);
		assertEquals(10, config.getPlacementDelayTicks());

		config.setPlacementDelayTicks(-5);
		assertEquals(0, config.getPlacementDelayTicks());

		config.setReachDistance(100.0);
		assertEquals(6.0, config.getReachDistance(), 0.001);

		config.setReachDistance(0.5);
		assertEquals(2.0, config.getReachDistance(), 0.001);

		// Reset to default
		config.applyPreset("survival");
	}

	@Test
	void testToggle() {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		config.setEnabled(false);

		boolean t1 = config.toggleEnabled();
		assertTrue(t1);
		assertTrue(config.isEnabled());

		boolean t2 = config.toggleEnabled();
		assertFalse(t2);
		assertFalse(config.isEnabled());
	}

	@Test
	void testPresets() {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();

		// Survival Preset
		config.applyPreset("survival");
		assertEquals(ShhhPrinterConfig.TargetMode.CROSSHAIR, config.getTargetMode());
		assertEquals(1, config.getBlocksPerTick());
		assertEquals(2, config.getPlacementDelayTicks());
		assertFalse(config.isAirPlacement());
		assertTrue(config.isStrictAdjacent());
		assertTrue(config.isAutoSwitchHotbar());
		assertTrue(config.isInventorySwap());
		assertEquals(ShhhPrinterConfig.HudPosition.TOP_RIGHT, config.getHudPosition());

		// Public Server Preset (Safe)
		config.applyPreset("public");
		assertEquals(ShhhPrinterConfig.TargetMode.CROSSHAIR, config.getTargetMode());
		assertEquals(1, config.getBlocksPerTick());
		assertEquals(4, config.getPlacementDelayTicks());
		assertFalse(config.isAirPlacement());
		assertTrue(config.isStrictAdjacent());
		assertTrue(config.isAutoSwitchHotbar());
		assertFalse(config.isInventorySwap());

		// Creative Preset (Fast)
		config.applyPreset("creative");
		assertEquals(ShhhPrinterConfig.TargetMode.CROSSHAIR, config.getTargetMode());
		assertEquals(3, config.getBlocksPerTick());
		assertEquals(0, config.getPlacementDelayTicks());
		assertTrue(config.isAirPlacement());
		assertFalse(config.isStrictAdjacent());
		assertTrue(config.isAutoSwitchHotbar());
		assertTrue(config.isInventorySwap());
		assertEquals(ShhhPrinterConfig.HudPosition.TOP_RIGHT, config.getHudPosition());
	}

	@Test
	void testHudPositionCycling() {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		config.setHudPosition(ShhhPrinterConfig.HudPosition.TOP_LEFT);
		assertEquals(ShhhPrinterConfig.HudPosition.TOP_LEFT, config.getHudPosition());
		assertEquals("Top-Left", config.getHudPosition().getDisplayName());

		assertEquals(ShhhPrinterConfig.HudPosition.TOP_RIGHT, config.getHudPosition().next());
		assertEquals(ShhhPrinterConfig.HudPosition.BOTTOM_LEFT, ShhhPrinterConfig.HudPosition.TOP_RIGHT.next());
		assertEquals(ShhhPrinterConfig.HudPosition.BOTTOM_RIGHT, ShhhPrinterConfig.HudPosition.BOTTOM_LEFT.next());
		assertEquals(ShhhPrinterConfig.HudPosition.TOP_LEFT, ShhhPrinterConfig.HudPosition.BOTTOM_RIGHT.next());

		config.setHudPosition(ShhhPrinterConfig.HudPosition.TOP_RIGHT);
	}
}
