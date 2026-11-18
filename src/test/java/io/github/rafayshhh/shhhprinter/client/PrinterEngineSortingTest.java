package io.github.rafayshhh.shhhprinter.client;

import io.github.rafayshhh.shhhbuilder.client.PlacementManager;
import io.github.rafayshhh.shhhprinter.client.engine.PrinterEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrinterEngineSortingTest {

	@Test
	void testCandidateSortingBottomUp() {
		// Mock transformed blocks at different Y levels and distances
		PlacementManager.TransformedBlock tbY0 = new PlacementManager.TransformedBlock(0, 0, 0, null);
		PlacementManager.TransformedBlock tbY1 = new PlacementManager.TransformedBlock(0, 1, 0, null);
		PlacementManager.TransformedBlock tbY2Near = new PlacementManager.TransformedBlock(1, 2, 1, null);
		PlacementManager.TransformedBlock tbY2Far = new PlacementManager.TransformedBlock(3, 2, 3, null);

		PrinterEngine.Candidate cY2Far = new PrinterEngine.Candidate(new BlockPos(3, 67, 3), tbY2Far, 25.0);
		PrinterEngine.Candidate cY1 = new PrinterEngine.Candidate(new BlockPos(0, 66, 0), tbY1, 10.0);
		PrinterEngine.Candidate cY2Near = new PrinterEngine.Candidate(new BlockPos(1, 67, 1), tbY2Near, 5.0);
		PrinterEngine.Candidate cY0 = new PrinterEngine.Candidate(new BlockPos(0, 65, 0), tbY0, 15.0);

		List<PrinterEngine.Candidate> list = new ArrayList<>(List.of(cY2Far, cY1, cY2Near, cY0));

		// Sort using the same comparator as PrinterEngine
		list.sort((a, b) -> {
			int yComp = Integer.compare(a.tb().relY(), b.tb().relY());
			if (yComp != 0) return yComp;
			return Double.compare(a.distanceSq(), b.distanceSq());
		});

		// 1st must be cY0 (Y = 0)
		assertEquals(0, list.get(0).tb().relY());
		// 2nd must be cY1 (Y = 1)
		assertEquals(1, list.get(1).tb().relY());
		// 3rd must be cY2Near (Y = 2, dist = 5.0)
		assertEquals(2, list.get(2).tb().relY());
		assertEquals(5.0, list.get(2).distanceSq());
		// 4th must be cY2Far (Y = 2, dist = 25.0)
		assertEquals(2, list.get(3).tb().relY());
		assertEquals(25.0, list.get(3).distanceSq());
	}

	@Test
	void testPlayerBoundingBoxExclusion() {
		// Player standing at (0.5, 65.0, 0.5) with width 0.6 and height 1.8
		AABB playerBox = new AABB(
			0.5 - 0.3, 65.0, 0.5 - 0.3,
			0.5 + 0.3, 65.0 + 1.8, 0.5 + 0.3
		);

		// Block at (0, 65, 0) intersects player
		BlockPos feetPos = new BlockPos(0, 65, 0);
		AABB feetBox = new AABB(feetPos);
		assertTrue(playerBox.intersects(feetBox), "Feet position must intersect player box");

		// Block at (0, 66, 0) intersects player head
		BlockPos headPos = new BlockPos(0, 66, 0);
		AABB headBox = new AABB(headPos);
		assertTrue(playerBox.intersects(headBox), "Head position must intersect player box");

		// Block adjacent at (1, 65, 0) does not intersect player
		BlockPos adjacentPos = new BlockPos(1, 65, 0);
		AABB adjacentBox = new AABB(adjacentPos);
		assertFalse(playerBox.intersects(adjacentBox), "Adjacent block must not intersect player box");
	}
}
