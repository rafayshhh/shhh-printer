package io.github.rafayshhh.shhhprinter.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.rafayshhh.shhhbuilder.client.PlacementManager;
import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import io.github.rafayshhh.shhhprinter.client.engine.PrinterEngine;
import io.github.rafayshhh.shhhprinter.client.gui.ShhhPrinterConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Locale;

/**
 * Client-side commands for Shhh Printer (/shhhprinter).
 */
public class ShhhPrinterCommand {

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		var root = ClientCommands.literal("shhhprinter")
			.executes(ctx -> executeHelp(ctx.getSource()));

		// /shhhprinter help
		root.then(ClientCommands.literal("help")
			.executes(ctx -> executeHelp(ctx.getSource())));

		// /shhhprinter toggle
		root.then(ClientCommands.literal("toggle")
			.executes(ctx -> executeToggle(ctx.getSource())));

		// /shhhprinter on
		root.then(ClientCommands.literal("on")
			.executes(ctx -> executeSetEnabled(ctx.getSource(), true)));

		// /shhhprinter off
		root.then(ClientCommands.literal("off")
			.executes(ctx -> executeSetEnabled(ctx.getSource(), false)));

		// /shhhprinter speed <1-5>
		root.then(ClientCommands.literal("speed")
			.then(ClientCommands.argument("blocks", IntegerArgumentType.integer(1, 5))
				.executes(ctx -> executeSpeed(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "blocks")))));

		// /shhhprinter delay <0-10>
		root.then(ClientCommands.literal("delay")
			.then(ClientCommands.argument("ticks", IntegerArgumentType.integer(0, 10))
				.executes(ctx -> executeDelay(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "ticks")))));

		// /shhhprinter mode <strict|air>
		root.then(ClientCommands.literal("mode")
			.then(ClientCommands.argument("placementMode", StringArgumentType.word())
				.suggests((ctx, builder) -> {
					builder.suggest("strict");
					builder.suggest("air");
					return builder.buildFuture();
				})
				.executes(ctx -> executeMode(ctx.getSource(), StringArgumentType.getString(ctx, "placementMode")))));

		// /shhhprinter target <crosshair|all>
		root.then(ClientCommands.literal("target")
			.then(ClientCommands.argument("targetMode", StringArgumentType.word())
				.suggests((ctx, builder) -> {
					builder.suggest("crosshair");
					builder.suggest("all");
					return builder.buildFuture();
				})
				.executes(ctx -> executeTarget(ctx.getSource(), StringArgumentType.getString(ctx, "targetMode")))));

		// /shhhprinter stats
		root.then(ClientCommands.literal("stats")
			.executes(ctx -> executeStats(ctx.getSource())));

		// /shhhprinter config
		root.then(ClientCommands.literal("config")
			.executes(ctx -> executeOpenConfig(ctx.getSource())));

		// /shhhprinter preset <survival|public|creative>
		root.then(ClientCommands.literal("preset")
			.then(ClientCommands.argument("name", StringArgumentType.word())
				.suggests((ctx, builder) -> {
					builder.suggest("survival");
					builder.suggest("public");
					builder.suggest("creative");
					return builder.buildFuture();
				})
				.executes(ctx -> executePreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))));

		dispatcher.register(root);
	}

	private static int executeHelp(FabricClientCommandSource source) {
		source.sendFeedback(Component.literal("=== Shhh Printer Commands ===").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
		source.sendFeedback(Component.literal("/shhhprinter toggle").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Toggle automated block placement [P]").withStyle(ChatFormatting.GRAY)));
		source.sendFeedback(Component.literal("/shhhprinter target <crosshair|all>").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Set Point & Place or Area 360 mode").withStyle(ChatFormatting.GRAY)));
		source.sendFeedback(Component.literal("/shhhprinter preset <survival|public|creative>").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Apply optimized configuration preset").withStyle(ChatFormatting.GRAY)));
		source.sendFeedback(Component.literal("/shhhprinter speed <1-5>").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Set placement speed (blocks per tick)").withStyle(ChatFormatting.GRAY)));
		source.sendFeedback(Component.literal("/shhhprinter delay <0-10>").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Set delay between placements in ticks").withStyle(ChatFormatting.GRAY)));
		source.sendFeedback(Component.literal("/shhhprinter mode <strict|air>").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Set strict adjacent or air placement").withStyle(ChatFormatting.GRAY)));
		source.sendFeedback(Component.literal("/shhhprinter config").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Open settings screen [O]").withStyle(ChatFormatting.GRAY)));
		source.sendFeedback(Component.literal("/shhhprinter stats").withStyle(ChatFormatting.YELLOW)
			.append(Component.literal(" - Display printer status and statistics").withStyle(ChatFormatting.GRAY)));
		return 1;
	}

	private static int executeToggle(FabricClientCommandSource source) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		boolean enabled = config.toggleEnabled();
		config.save();

		sendToggleFeedback(source, enabled);
		return 1;
	}

	private static int executeSetEnabled(FabricClientCommandSource source, boolean state) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		config.setEnabled(state);
		config.save();

		sendToggleFeedback(source, state);
		return 1;
	}

	private static void sendToggleFeedback(FabricClientCommandSource source, boolean enabled) {
		if (enabled) {
			if (!PlacementManager.hasPlacement()) {
				source.sendFeedback(Component.literal("Shhh Printer: Enabled (Warning: no schematic placed in Shhh Builder. Press V to place one).")
					.withStyle(ChatFormatting.YELLOW));
			} else {
				source.sendFeedback(Component.literal("Shhh Printer: Enabled").withStyle(ChatFormatting.GREEN));
			}
		} else {
			source.sendFeedback(Component.literal("Shhh Printer: Disabled").withStyle(ChatFormatting.RED));
		}
	}

	private static int executeSpeed(FabricClientCommandSource source, int blocksPerTick) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		config.setBlocksPerTick(blocksPerTick);
		config.save();

		source.sendFeedback(Component.literal("Shhh Printer: Speed set to " + blocksPerTick + " block(s) per tick.")
			.withStyle(ChatFormatting.AQUA));
		return 1;
	}

	private static int executeDelay(FabricClientCommandSource source, int delay) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		config.setPlacementDelayTicks(delay);
		config.save();

		source.sendFeedback(Component.literal("Shhh Printer: Delay set to " + delay + " tick(s) between placements.")
			.withStyle(ChatFormatting.AQUA));
		return 1;
	}

	private static int executeMode(FabricClientCommandSource source, String modeArg) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		String lower = modeArg.toLowerCase(Locale.ROOT);

		if (lower.equals("air")) {
			config.setAirPlacement(true);
			config.setStrictAdjacent(false);
			config.save();
			source.sendFeedback(Component.literal("Shhh Printer: Mode set to Air Placement (Warning: only use on servers that permit air placement).")
				.withStyle(ChatFormatting.YELLOW));
		} else if (lower.equals("strict")) {
			config.setAirPlacement(false);
			config.setStrictAdjacent(true);
			config.save();
			source.sendFeedback(Component.literal("Shhh Printer: Mode set to Strict Adjacent (Safe for all vanilla and anti-cheat servers).")
				.withStyle(ChatFormatting.GREEN));
		} else {
			source.sendError(Component.literal("Invalid mode. Use '/shhhprinter mode strict' or '/shhhprinter mode air'.")
				.withStyle(ChatFormatting.RED));
			return 0;
		}
		return 1;
	}

	private static int executeOpenConfig(FabricClientCommandSource source) {
		Minecraft mc = Minecraft.getInstance();
		mc.execute(() -> {
			if (mc.gui != null) {
				mc.gui.setScreen(new ShhhPrinterConfigScreen(mc.gui.screen()));
			}
		});
		return 1;
	}

	private static int executeTarget(FabricClientCommandSource source, String targetArg) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		String lower = targetArg.toLowerCase(Locale.ROOT);

		if (lower.equals("crosshair") || lower.equals("point")) {
			config.setTargetMode(ShhhPrinterConfig.TargetMode.CROSSHAIR);
			config.save();
			source.sendFeedback(Component.literal("Shhh Printer: Target mode set to Point & Place (Crosshair only, fair play safe).")
				.withStyle(ChatFormatting.GREEN));
		} else if (lower.equals("all") || lower.equals("area")) {
			config.setTargetMode(ShhhPrinterConfig.TargetMode.ALL_IN_REACH);
			config.save();
			source.sendFeedback(Component.literal("Shhh Printer: Target mode set to Area 360° (Warning: places all blocks in reach sphere, for singleplayer only).")
				.withStyle(ChatFormatting.YELLOW));
		} else {
			source.sendError(Component.literal("Invalid target mode. Use '/shhhprinter target crosshair' or '/shhhprinter target all'.")
				.withStyle(ChatFormatting.RED));
			return 0;
		}
		return 1;
	}

	private static int executePreset(FabricClientCommandSource source, String name) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		String lower = name.toLowerCase(Locale.ROOT);
		config.applyPreset(lower);

		String desc = switch (lower) {
			case "creative", "fast" -> "Creative (Instant, Air-Place Allowed)";
			case "public", "safe" -> "Public Server (Anti-Cheat Safe, 5 blk/s)";
			default -> "Survival (Point & Place, 10 blk/s, Strict Support)";
		};

		source.sendFeedback(Component.literal("Shhh Printer: Applied preset: " + desc)
			.withStyle(ChatFormatting.GREEN));
		return 1;
	}

	private static int executeStats(FabricClientCommandSource source) {
		ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
		boolean enabled = config.isEnabled();
		boolean hasPlacement = PlacementManager.hasPlacement();
		int placed = PrinterEngine.getTotalBlocksPlaced();
		String status = PrinterEngine.getLastStatusMessage();

		source.sendFeedback(Component.literal("=== Shhh Printer Status ===").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
		source.sendFeedback(Component.literal("Status: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(enabled ? "ACTIVE" : "PAUSED").withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED)));
		source.sendFeedback(Component.literal("Target Mode: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(config.getTargetMode() == ShhhPrinterConfig.TargetMode.CROSSHAIR ? "Point & Place (Crosshair)" : "Area 360°").withStyle(ChatFormatting.WHITE)));
		source.sendFeedback(Component.literal("Shhh Builder Placement: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(hasPlacement ? "ACTIVE" : "NONE").withStyle(hasPlacement ? ChatFormatting.GREEN : ChatFormatting.YELLOW)));
		source.sendFeedback(Component.literal("Total Blocks Placed: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(String.valueOf(placed)).withStyle(ChatFormatting.WHITE)));
		source.sendFeedback(Component.literal("Engine State: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(status).withStyle(ChatFormatting.YELLOW)));
		source.sendFeedback(Component.literal("Speed: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(config.getBlocksPerTick() + " blk/tick (delay " + config.getPlacementDelayTicks() + "t)").withStyle(ChatFormatting.WHITE)));
		source.sendFeedback(Component.literal("Air Placement: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(config.isAirPlacement() ? "Enabled (Mid-air)" : "Disabled (Strict Support)").withStyle(config.isAirPlacement() ? ChatFormatting.YELLOW : ChatFormatting.GREEN)));
		source.sendFeedback(Component.literal("HUD Position: ").withStyle(ChatFormatting.GRAY)
			.append(Component.literal(config.getHudPosition().getDisplayName()).withStyle(ChatFormatting.WHITE)));
		return 1;
	}
}
