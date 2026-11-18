package io.github.rafayshhh.shhhprinter.client;

import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrinterConfigTest {

	@Test
	void testClampingAndDefaults() {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();

		// Default values
		assertFalse(config.isEnabled());
		assertEquals(1, config.getBlocksPerTick());
		assertEquals(0, config.getPlacementDelayTicks());
		assertEquals(4.5, config.getReachDistance(), 0.001);
		assertFalse(config.isStrictAdjacent());
		assertTrue(config.isAirPlacement());
		assertTrue(config.isAutoSwitchHotbar());
		assertTrue(config.isInventorySwap());

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
		config.setBlocksPerTick(1);
		config.setPlacementDelayTicks(0);
		config.setReachDistance(4.5);
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
}
