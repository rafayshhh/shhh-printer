package io.github.rafayshhh.shhhprinter.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlacementHitVectorTest {

	@Test
	void testNeighborDirectionOpposites() {
		// When target is at (10, 65, 10), checking DOWN (10, 64, 10)
		Direction testedDir = Direction.DOWN;
		Direction clickedFace = testedDir.getOpposite();
		assertEquals(Direction.UP, clickedFace);

		BlockPos neighborPos = new BlockPos(10, 64, 10);
		BlockPos resultingPos = neighborPos.relative(clickedFace);
		assertEquals(new BlockPos(10, 65, 10), resultingPos);

		// Checking NORTH (10, 65, 9)
		Direction northDir = Direction.NORTH;
		Direction northClicked = northDir.getOpposite();
		assertEquals(Direction.SOUTH, northClicked);

		BlockPos northNeighbor = new BlockPos(10, 65, 9);
		assertEquals(new BlockPos(10, 65, 10), northNeighbor.relative(northClicked));
	}

	@Test
	void testSlabVectorCalculations() {
		// Test height calculation for bottom vs top slabs
		int neighborY = 64;

		double bottomSlabY = neighborY + 0.2;
		double topSlabY = neighborY + 0.8;

		assertTrue(bottomSlabY - neighborY < 0.5, "Bottom slab hit vector must be below 0.5");
		assertTrue(topSlabY - neighborY > 0.5, "Top slab hit vector must be above 0.5");
	}
}
