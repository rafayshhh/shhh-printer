package io.github.rafayshhh.shhhprinter.client.engine;

import io.github.rafayshhh.shhhbuilder.client.LayerMode;
import io.github.rafayshhh.shhhbuilder.client.PlacementManager;
import io.github.rafayshhh.shhhbuilder.client.SchematicManager;
import io.github.rafayshhh.shhhbuilder.client.hud.GhostBlockRaycastHelper;
import io.github.rafayshhh.shhhbuilder.client.material.MaterialCountingEngine;
import io.github.rafayshhh.shhhbuilder.client.schematic.LitematicData;
import io.github.rafayshhh.shhhbuilder.client.schematic.WorldBlockValidator;
import io.github.rafayshhh.shhhprinter.ShhhPrinter;
import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Core automated placement engine for Shhh Printer.
 * Intercepts client ticks, resolves candidates within reach from Shhh Builder's placement,
 * ensures strict physical adjacency or air placement, manages hotbar slots, and dispatches useItemOn.
 */
public class PrinterEngine {
	private static int delayCounter = 0;
	private static final Map<Long, Long> PLACED_CONFIRMATION_CACHE = new ConcurrentHashMap<>();
	private static final long CONFIRMATION_COOLDOWN_MS = 350;

	private static int totalBlocksPlaced = 0;
	private static String lastStatusMessage = "Idle";
	private static String currentMissingItem = null;
	private static int reachableMissingCount = 0;

	public record Candidate(BlockPos worldPos, PlacementManager.TransformedBlock tb, double distanceSq) {}

	public static void tick(Minecraft mc) {
		if (mc == null || mc.player == null || mc.level == null || mc.gameMode == null) {
			return;
		}

		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		if (!config.isEnabled()) {
			lastStatusMessage = "Disabled";
			currentMissingItem = null;
			reachableMissingCount = 0;
			return;
		}

		if (!PlacementManager.hasPlacement()) {
			lastStatusMessage = "No Build in Shhh Builder";
			currentMissingItem = null;
			reachableMissingCount = 0;
			return;
		}

		if (PlacementManager.getDimension() != null && !mc.level.dimension().equals(PlacementManager.getDimension())) {
			lastStatusMessage = "Wrong Dimension";
			return;
		}

		// Prune confirmation cache periodically
		long now = System.currentTimeMillis();
		PLACED_CONFIRMATION_CACHE.entrySet().removeIf(e -> (now - e.getValue()) > 2000);

		// Handle placement delay ticks
		if (config.getPlacementDelayTicks() > 0) {
			if (delayCounter++ < config.getPlacementDelayTicks()) {
				return;
			}
			delayCounter = 0;
		}

		LitematicData data = SchematicManager.getSelectedSchematicData();
		if (data == null) {
			data = SchematicManager.getLoadedData();
		}
		if (data == null || data.blockData() == null || !data.blockData().canPreview()) {
			lastStatusMessage = "No Schematic Loaded";
			return;
		}

		LayerMode layerMode = PlacementManager.getLayerMode();
		int currentLayer = PlacementManager.getCurrentLayer();
		int layerFilter = (layerMode == LayerMode.SINGLE) ? (currentLayer - 1) : -1;

		List<PlacementManager.TransformedBlock> transformedBlocks = PlacementManager.getTransformedBlocks(data, layerFilter);
		if (transformedBlocks.isEmpty()) {
			lastStatusMessage = "No Blocks in Active Layer";
			return;
		}

		BlockPos origin = PlacementManager.getPosition();
		Vec3 eyePos = mc.player.getEyePosition(1.0f);
		AABB playerBox = mc.player.getBoundingBox();
		double reach = config.getReachDistance();
		double reachSq = reach * reach;

		List<Candidate> candidates = new ArrayList<>();

		if (config.getTargetMode() == ShhhPrinterConfig.TargetMode.CROSSHAIR) {
			// Crosshair Point & Place: only place the block the player is actively pointing at
			// 1. Raycast ghost block directly under the crosshair
			GhostBlockRaycastHelper.GhostHit ghostHit = GhostBlockRaycastHelper.raycast(mc);
			if (ghostHit != null && ghostHit.block() != null && ghostHit.block().state() != null) {
				BlockPos worldPos = ghostHit.worldPos();
				PlacementManager.TransformedBlock tb = ghostHit.block();

				boolean layerAllowed = true;
				if (layerMode == LayerMode.SINGLE && tb.relY() != (currentLayer - 1)) {
					layerAllowed = false;
				} else if (layerMode == LayerMode.UP_TO && tb.relY() >= currentLayer) {
					layerAllowed = false;
				}

				if (layerAllowed && !tb.state().isAir()) {
					double distSq = eyePos.distanceToSqr(Vec3.atCenterOf(worldPos));
					if (distSq <= reachSq) {
						Long lastPlaced = PLACED_CONFIRMATION_CACHE.get(worldPos.asLong());
						if (lastPlaced == null || (now - lastPlaced) >= CONFIRMATION_COOLDOWN_MS) {
							BlockState worldState = mc.level.getBlockState(worldPos);
							WorldBlockValidator.MatchStatus match = WorldBlockValidator.check(worldState, tb.state());
							if (match == WorldBlockValidator.MatchStatus.MISSING) {
								AABB blockBox = new AABB(worldPos);
								if (!playerBox.intersects(blockBox)) {
									candidates.add(new Candidate(worldPos, tb, distSq));
								}
							}
						}
					}
				}
			}

			// 2. If no ghost block directly intersected, check if crosshair is aimed at a physical world block face
			if (candidates.isEmpty() && mc.hitResult instanceof BlockHitResult bhr && bhr.getType() == HitResult.Type.BLOCK) {
				BlockPos targetPos = bhr.getBlockPos().relative(bhr.getDirection());
				int relX = targetPos.getX() - origin.getX();
				int relY = targetPos.getY() - origin.getY();
				int relZ = targetPos.getZ() - origin.getZ();

				PlacementManager.TransformedBlock tb = PlacementManager.getBlockAt(relX, relY, relZ);
				if (tb == null) {
					PlacementManager.getCachedBlocks(data);
					tb = PlacementManager.getBlockAt(relX, relY, relZ);
				}
				if (tb != null && tb.state() != null && !tb.state().isAir()) {
					boolean layerAllowed = true;
					if (layerMode == LayerMode.SINGLE && tb.relY() != (currentLayer - 1)) {
						layerAllowed = false;
					} else if (layerMode == LayerMode.UP_TO && tb.relY() >= currentLayer) {
						layerAllowed = false;
					}

					if (layerAllowed) {
						double distSq = eyePos.distanceToSqr(Vec3.atCenterOf(targetPos));
						if (distSq <= reachSq) {
							Long lastPlaced = PLACED_CONFIRMATION_CACHE.get(targetPos.asLong());
							if (lastPlaced == null || (now - lastPlaced) >= CONFIRMATION_COOLDOWN_MS) {
								BlockState worldState = mc.level.getBlockState(targetPos);
								WorldBlockValidator.MatchStatus match = WorldBlockValidator.check(worldState, tb.state());
								if (match == WorldBlockValidator.MatchStatus.MISSING) {
									AABB blockBox = new AABB(targetPos);
									if (!playerBox.intersects(blockBox)) {
										candidates.add(new Candidate(targetPos, tb, distSq));
									}
								}
							}
						}
					}
				}
			}

			reachableMissingCount = candidates.size();
			if (candidates.isEmpty()) {
				lastStatusMessage = "Point at a ghost block";
				currentMissingItem = null;
				return;
			}
		} else {
			// ALL_IN_REACH: Area 360 mode
			for (PlacementManager.TransformedBlock tb : transformedBlocks) {
				if (tb == null || tb.state() == null || tb.state().isAir()) {
					continue;
				}

				// Respect UP_TO layer filter
				if (layerMode == LayerMode.UP_TO && tb.relY() >= currentLayer) {
					continue;
				}

				BlockPos worldPos = origin.offset(tb.relX(), tb.relY(), tb.relZ());
				double distSq = eyePos.distanceToSqr(Vec3.atCenterOf(worldPos));
				if (distSq > reachSq) {
					continue;
				}

				// Check confirmation cooldown
				Long lastPlaced = PLACED_CONFIRMATION_CACHE.get(worldPos.asLong());
				if (lastPlaced != null && (now - lastPlaced) < CONFIRMATION_COOLDOWN_MS) {
					continue;
				}

				BlockState worldState = mc.level.getBlockState(worldPos);
				WorldBlockValidator.MatchStatus match = WorldBlockValidator.check(worldState, tb.state());

				if (match == WorldBlockValidator.MatchStatus.EXACT_MATCH) {
					continue; // Already built!
				}

				// Player collision safety: never place solid blocks inside the player's bounding box
				AABB blockBox = new AABB(worldPos);
				if (playerBox.intersects(blockBox)) {
					continue;
				}

				if (match == WorldBlockValidator.MatchStatus.MISSING) {
					candidates.add(new Candidate(worldPos, tb, distSq));
				}
			}

			reachableMissingCount = candidates.size();
			if (candidates.isEmpty()) {
				lastStatusMessage = "In Reach: 100% Done";
				currentMissingItem = null;
				return;
			}

			// Sort candidates:
			// 1. Y-level ascending (bottom-to-top ground support)
			// 2. Distance squared to eye position (closest first)
			candidates.sort((c1, c2) -> {
				int yComp = Integer.compare(c1.tb().relY(), c2.tb().relY());
				if (yComp != 0) return yComp;
				return Double.compare(c1.distanceSq(), c2.distanceSq());
			});
		}

		int maxBlocksThisTick = config.getBlocksPerTick();
		int placedCount = 0;
		boolean isCreative = mc.player.isCreative() || mc.player.getAbilities().instabuild;

		for (Candidate candidate : candidates) {
			if (placedCount >= maxBlocksThisTick) {
				break;
			}

			BlockState expectedState = candidate.tb().state();
			Set<String> itemIds = resolveRequiredItemIds(expectedState);
			if (itemIds.isEmpty()) {
				continue;
			}

			// Survival item check and hotbar selection
			if (isCreative) {
				Inventory inv = mc.player.getInventory();
				int hotbarSlot = findInHotbar(inv, itemIds);
				if (hotbarSlot == -1) {
					// Auto-equip first available in creative
					String itemId = itemIds.iterator().next();
					String[] parts = itemId.split(":", 2);
					Identifier id = Identifier.fromNamespaceAndPath(parts.length > 1 ? parts[0] : "minecraft", parts.length > 1 ? parts[1] : parts[0]);
					Item item = BuiltInRegistries.ITEM.getValue(id);
					if (item != null) {
						ItemStack stack = new ItemStack(item, 64);
						int currentSlot = inv.getSelectedSlot();
						inv.setItem(currentSlot, stack);
						mc.gameMode.handleCreativeModeItemAdd(stack, currentSlot + 36);
						selectHotbarSlot(mc, currentSlot);
					}
				} else if (config.isAutoSwitchHotbar()) {
					selectHotbarSlot(mc, hotbarSlot);
				}
			} else {
				Inventory inv = mc.player.getInventory();
				int hotbarSlot = findInHotbar(inv, itemIds);

				if (hotbarSlot != -1) {
					if (config.isAutoSwitchHotbar()) {
						selectHotbarSlot(mc, hotbarSlot);
					}
				} else {
					int mainInvSlot = findInMainInventory(inv, itemIds);
					if (mainInvSlot != -1 && config.isInventorySwap()) {
						int currentSlot = inv.getSelectedSlot();
						mc.gameMode.handleContainerInput(
							mc.player.inventoryMenu.containerId,
							mainInvSlot,
							currentSlot,
							ContainerInput.SWAP,
							mc.player
						);
						selectHotbarSlot(mc, currentSlot);
					} else {
						// Item not in inventory, skip block
						currentMissingItem = expectedState.getBlock().getName().getString();
						lastStatusMessage = "Missing " + currentMissingItem;
						continue;
					}
				}
			}

			// Resolve physical placement hit result
			BlockHitResult hit = resolvePlacementHit(
				mc,
				candidate.worldPos(),
				expectedState,
				config.isStrictAdjacent(),
				config.isAirPlacement()
			);

			if (hit == null) {
				continue; // No valid adjacent support block found in strict mode
			}

			// Orient directional blocks if enabled
			if (config.isAutoOrientFacing()) {
				adjustOrientation(mc, candidate.worldPos(), expectedState, hit);
			}

			// Dispatch placement interaction
			InteractionResult result = mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
			if (result != null && result.consumesAction()) {
				mc.player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, true);
				PLACED_CONFIRMATION_CACHE.put(candidate.worldPos().asLong(), System.currentTimeMillis());
				totalBlocksPlaced++;
				placedCount++;
				lastStatusMessage = "Active (" + totalBlocksPlaced + " placed)";
				currentMissingItem = null;
			}
		}
	}

	/**
	 * Resolves the BlockHitResult for placing the expected block at targetPos.
	 * In strict mode: searches 6 neighbors (DOWN, NORTH, SOUTH, EAST, WEST, UP) for an existing solid face.
	 * In air mode: clicks directly on targetPos.
	 */
	public static BlockHitResult resolvePlacementHit(
		Minecraft mc,
		BlockPos targetPos,
		BlockState expectedState,
		boolean strictAdjacent,
		boolean airPlacement
	) {
		if (mc == null || mc.level == null) {
			return null;
		}

		// Prefer DOWN support first (floor), then horizontal sides, then UP
		Direction[] testDirs = new Direction[] {
			Direction.DOWN,
			Direction.NORTH,
			Direction.SOUTH,
			Direction.WEST,
			Direction.EAST,
			Direction.UP
		};

		for (Direction dir : testDirs) {
			BlockPos neighborPos = targetPos.relative(dir);
			BlockState neighborState = mc.level.getBlockState(neighborPos);

			if (neighborState != null && !neighborState.isAir() && !neighborState.canBeReplaced()) {
				// The face of the neighbor touching targetPos
				Direction clickedFace = dir.getOpposite();

				// Base center of clicked face
				double hx = neighborPos.getX() + 0.5 + (clickedFace.getStepX() * 0.5);
				double hy = neighborPos.getY() + 0.5 + (clickedFace.getStepY() * 0.5);
				double hz = neighborPos.getZ() + 0.5 + (clickedFace.getStepZ() * 0.5);

				// Adjust height vector for slabs and stairs if clicking horizontal side
				if (clickedFace.getAxis().isHorizontal()) {
					if (expectedState.hasProperty(BlockStateProperties.SLAB_TYPE)) {
						SlabType slabType = expectedState.getValue(BlockStateProperties.SLAB_TYPE);
						if (slabType == SlabType.TOP) {
							hy = neighborPos.getY() + 0.8;
						} else if (slabType == SlabType.BOTTOM) {
							hy = neighborPos.getY() + 0.2;
						}
					} else if (expectedState.hasProperty(BlockStateProperties.HALF)) {
						Half half = expectedState.getValue(BlockStateProperties.HALF);
						if (half == Half.TOP) {
							hy = neighborPos.getY() + 0.8;
						} else {
							hy = neighborPos.getY() + 0.2;
						}
					}
				}

				return new BlockHitResult(new Vec3(hx, hy, hz), clickedFace, neighborPos, false);
			}
		}

		// If no adjacent solid support is found, check if air placement is permitted
		if (airPlacement) {
			Vec3 center = Vec3.atCenterOf(targetPos);
			return new BlockHitResult(center, Direction.UP, targetPos, false);
		}

		return null;
	}

	/**
	 * Adjusts the client player's look rotation packets for blocks that orient based on player view.
	 */
	public static void adjustOrientation(Minecraft mc, BlockPos targetPos, BlockState state, BlockHitResult hit) {
		if (mc == null || mc.player == null || state == null) {
			return;
		}

		Direction facing = null;
		if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
			facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		} else if (state.hasProperty(BlockStateProperties.FACING)) {
			facing = state.getValue(BlockStateProperties.FACING);
		}

		if (facing != null && facing.getAxis().isHorizontal()) {
			float targetYaw = facing.toYRot();
			// Only send rotation packet if player look yaw differs significantly to avoid rate-limiting
			if (Math.abs(mc.player.getYRot() - targetYaw) > 1.0f && mc.getConnection() != null) {
				mc.getConnection().send(new ServerboundMovePlayerPacket.Rot(
					targetYaw,
					mc.player.getXRot(),
					mc.player.onGround(),
					false
				));
			}
		}
	}

	private static void selectHotbarSlot(Minecraft mc, int slot) {
		if (mc == null || mc.player == null) {
			return;
		}
		Inventory inv = mc.player.getInventory();
		if (inv.getSelectedSlot() != slot) {
			inv.setSelectedSlot(slot);
			if (mc.getConnection() != null) {
				mc.getConnection().send(new ServerboundSetCarriedItemPacket(slot));
			}
		}
	}

	public static Set<String> resolveRequiredItemIds(BlockState state) {
		Set<String> itemIds = new HashSet<>();
		if (state == null || state.isAir()) {
			return itemIds;
		}

		try {
			MaterialCountingEngine.BlockEvaluation eval = MaterialCountingEngine.evaluate(state);
			if (eval != null && eval.items() != null) {
				for (MaterialCountingEngine.ItemRequirement req : eval.items()) {
					if (req.itemIdentifier() != null) {
						itemIds.add(req.itemIdentifier());
					}
				}
			}
		} catch (Throwable ignored) {
		}

		if (itemIds.isEmpty()) {
			Identifier blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
			if (blockId != null) {
				itemIds.add(blockId.toString());
			}
			try {
				var asItem = state.getBlock().asItem();
				if (asItem != null) {
					Identifier itemId = BuiltInRegistries.ITEM.getKey(asItem);
					if (itemId != null) {
						itemIds.add(itemId.toString());
					}
				}
			} catch (Throwable ignored) {
			}
		}

		return itemIds;
	}

	public static int findInHotbar(Inventory inv, Set<String> targetIds) {
		if (inv == null || targetIds == null || targetIds.isEmpty()) {
			return -1;
		}
		for (int slot = 0; slot < 9; slot++) {
			ItemStack stack = inv.getItem(slot);
			if (stack != null && !stack.isEmpty()) {
				Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
				if (id != null && targetIds.contains(id.toString())) {
					return slot;
				}
			}
		}
		return -1;
	}

	public static int findInMainInventory(Inventory inv, Set<String> targetIds) {
		if (inv == null || targetIds == null || targetIds.isEmpty()) {
			return -1;
		}
		for (int slot = 9; slot < 36; slot++) {
			ItemStack stack = inv.getItem(slot);
			if (stack != null && !stack.isEmpty()) {
				Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
				if (id != null && targetIds.contains(id.toString())) {
					return slot;
				}
			}
		}
		return -1;
	}

	public static void resetStats() {
		totalBlocksPlaced = 0;
		lastStatusMessage = "Idle";
		currentMissingItem = null;
		reachableMissingCount = 0;
		PLACED_CONFIRMATION_CACHE.clear();
	}

	public static int getTotalBlocksPlaced() { return totalBlocksPlaced; }
	public static String getLastStatusMessage() { return lastStatusMessage; }
	public static String getCurrentMissingItem() { return currentMissingItem; }
	public static int getReachableMissingCount() { return reachableMissingCount; }
}
