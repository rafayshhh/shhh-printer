package io.github.rafayshhh.shhhprinter.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.rafayshhh.shhhbuilder.client.PlacementManager;
import io.github.rafayshhh.shhhprinter.ShhhPrinter;
import io.github.rafayshhh.shhhprinter.client.command.ShhhPrinterCommand;
import io.github.rafayshhh.shhhprinter.client.config.ShhhPrinterConfig;
import io.github.rafayshhh.shhhprinter.client.engine.PrinterEngine;
import io.github.rafayshhh.shhhprinter.client.gui.ShhhPrinterConfigScreen;
import io.github.rafayshhh.shhhprinter.client.hud.PrinterHudElement;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Main client-side entrypoint for Shhh Printer.
 * Requires Shhh Builder to be loaded, registers keybinds, HUD element, and ticks the PrinterEngine.
 */
public class ShhhPrinterClient implements ClientModInitializer {
	public static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(
		Identifier.fromNamespaceAndPath(ShhhPrinter.MOD_ID, "main")
	);

	public static final KeyMapping TOGGLE_PRINTER_KEY = KeyMappingHelper.registerKeyMapping(
		new KeyMapping(
			"key.shhh-printer.toggle",
			InputConstants.KEY_P,
			KEY_CATEGORY
		)
	);

	public static final KeyMapping OPEN_CONFIG_KEY = KeyMappingHelper.registerKeyMapping(
		new KeyMapping(
			"key.shhh-printer.config",
			InputConstants.KEY_O,
			KEY_CATEGORY
		)
	);

	@Override
	public void onInitializeClient() {
		// Strict hard-dependency verification
		if (!FabricLoader.getInstance().isModLoaded("shhh-builder")) {
			throw new IllegalStateException("Shhh Printer requires Shhh Builder (shhh-builder) to be installed and loaded!");
		}

		// Load config
		ShhhPrinterConfig.getInstance().load();

		// Register client commands
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			ShhhPrinterCommand.register(dispatcher);
		});

		// Register on-screen HUD element
		HudElementRegistry.addLast(
			Identifier.fromNamespaceAndPath(ShhhPrinter.MOD_ID, "printer_hud"),
			new PrinterHudElement()
		);

		// Register client tick listener for keybinds & automated placement engine
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.gui != null && client.gui.screen() != null) {
				drainKeyMappings();
				return;
			}

			// Key P: Toggle Printer
			while (TOGGLE_PRINTER_KEY.consumeClick()) {
				ShhhPrinterConfig config = ShhhPrinterConfig.getInstance();
				boolean newState = config.toggleEnabled();
				config.save();

				if (client.player != null) {
					if (newState && !PlacementManager.hasPlacement()) {
						client.player.sendOverlayMessage(
							Component.literal("Printer Enabled (No build placed in Shhh Builder. Press V)")
								.withStyle(ChatFormatting.YELLOW)
						);
					} else {
						client.player.sendOverlayMessage(
							Component.literal("Shhh Printer: " + (newState ? "Enabled" : "Disabled"))
								.withStyle(newState ? ChatFormatting.GREEN : ChatFormatting.RED)
						);
					}
				}
			}

			// Key O: Open Config Screen
			while (OPEN_CONFIG_KEY.consumeClick()) {
				if (client.gui != null && client.gui.screen() == null) {
					client.gui.setScreen(new ShhhPrinterConfigScreen(null));
				}
			}

			// Tick placement engine
			PrinterEngine.tick(client);
		});

		// Clean up state on disconnect
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			PrinterEngine.resetStats();
		});

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			PrinterEngine.resetStats();
		});

		ShhhPrinter.LOGGER.info("Shhh Printer initialized successfully (supporting Shhh Builder).");
	}

	private void drainKeyMappings() {
		while (TOGGLE_PRINTER_KEY.consumeClick()) {}
		while (OPEN_CONFIG_KEY.consumeClick()) {}
	}
}
