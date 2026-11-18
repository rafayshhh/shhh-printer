package io.github.rafayshhh.shhhprinter.client;

import io.github.rafayshhh.shhhbuilder.client.LayerMode;
import io.github.rafayshhh.shhhbuilder.client.PlacementManager;
import io.github.rafayshhh.shhhbuilder.client.gui.UiStyle;
import io.github.rafayshhh.shhhbuilder.client.schematic.WorldBlockValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyCheckTest {

	@Test
	void testShhhBuilderClassesAreAccessible() {
		// Verify PlacementManager methods
		assertFalse(PlacementManager.hasPlacement());
		assertEquals(LayerMode.ALL, PlacementManager.getLayerMode());

		// Verify WorldBlockValidator
		assertEquals(WorldBlockValidator.MatchStatus.EXACT_MATCH, WorldBlockValidator.check(null, null));

		// Verify UiStyle constants
		assertEquals(0xFF00E5FF, UiStyle.COLOR_ACCENT);
		assertEquals(0xE6141619, UiStyle.BG_PANEL);
	}
}
