package io.github.rafayshhh.shhhprinter.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.rafayshhh.shhhprinter.ShhhPrinter;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Locale;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Configuration manager for Shhh Printer.
 * Persists settings to config/shhh-printer.json using atomic file operations.
 */
public class ShhhPrinterConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final ShhhPrinterConfig INSTANCE = new ShhhPrinterConfig();

	private boolean enabled = false;
	private int blocksPerTick = 1; // 1 to 5
	private int placementDelayTicks = 0; // 0 to 10
	private double reachDistance = 4.5; // 2.0 to 6.0
	private boolean strictAdjacent = false; // Requires adjacent solid block face
	private boolean airPlacement = true; // Places directly in air on lenient servers and mid-air builds
	private boolean autoSwitchHotbar = true; // Automatically selects required item in hotbar
	private boolean inventorySwap = true; // Swaps from inventory to hotbar if not in hotbar
	private boolean autoOrientFacing = true; // Computes precise vectors for slabs, stairs, directional blocks
	private boolean hudEnabled = true; // On-screen HUD overlay
	private boolean soundEffects = true; // Sound feedback on toggle/completion
	private boolean breakWrongBlocks = false; // Auto-mine wrong blocks

	public static ShhhPrinterConfig getInstance() {
		return INSTANCE;
	}

	public void applyPreset(String presetName) {
		switch (presetName.toLowerCase(Locale.ROOT)) {
			case "creative" -> {
				this.blocksPerTick = 5;
				this.airPlacement = true;
				this.strictAdjacent = false;
				this.autoSwitchHotbar = true;
				this.inventorySwap = true;
			}
			case "survival" -> {
				this.blocksPerTick = 1;
				this.airPlacement = false;
				this.strictAdjacent = true;
				this.autoSwitchHotbar = true;
				this.inventorySwap = true;
			}
			case "public" -> {
				this.blocksPerTick = 1;
				this.airPlacement = false;
				this.strictAdjacent = true;
				this.autoSwitchHotbar = false;
				this.inventorySwap = false;
			}
		}
		save();
	}

	public Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir().resolve("shhh-printer.json");
	}

	public synchronized void load() {
		Path path = getConfigPath();
		if (!Files.exists(path)) {
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(path)) {
			JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();

			if (obj.has("enabled")) enabled = obj.get("enabled").getAsBoolean();
			if (obj.has("blocksPerTick")) blocksPerTick = clamp(obj.get("blocksPerTick").getAsInt(), 1, 5);
			if (obj.has("placementDelayTicks")) placementDelayTicks = clamp(obj.get("placementDelayTicks").getAsInt(), 0, 10);
			if (obj.has("reachDistance")) reachDistance = clamp(obj.get("reachDistance").getAsDouble(), 2.0, 6.0);
			if (obj.has("strictAdjacent")) strictAdjacent = obj.get("strictAdjacent").getAsBoolean();
			if (obj.has("airPlacement")) airPlacement = obj.get("airPlacement").getAsBoolean();
			if (obj.has("autoSwitchHotbar")) autoSwitchHotbar = obj.get("autoSwitchHotbar").getAsBoolean();
			if (obj.has("inventorySwap")) inventorySwap = obj.get("inventorySwap").getAsBoolean();
			if (obj.has("autoOrientFacing")) autoOrientFacing = obj.get("autoOrientFacing").getAsBoolean();
			if (obj.has("hudEnabled")) hudEnabled = obj.get("hudEnabled").getAsBoolean();
			if (obj.has("soundEffects")) soundEffects = obj.get("soundEffects").getAsBoolean();
			if (obj.has("breakWrongBlocks")) breakWrongBlocks = obj.get("breakWrongBlocks").getAsBoolean();
		} catch (Throwable t) {
			ShhhPrinter.LOGGER.error("Failed to load shhh-printer.json; falling back to safe defaults.", t);
			save();
		}
	}

	public synchronized void save() {
		Path path = getConfigPath();
		Path tempPath = path.resolveSibling(path.getFileName().toString() + ".tmp");

		JsonObject obj = new JsonObject();
		obj.addProperty("enabled", enabled);
		obj.addProperty("blocksPerTick", blocksPerTick);
		obj.addProperty("placementDelayTicks", placementDelayTicks);
		obj.addProperty("reachDistance", reachDistance);
		obj.addProperty("strictAdjacent", strictAdjacent);
		obj.addProperty("airPlacement", airPlacement);
		obj.addProperty("autoSwitchHotbar", autoSwitchHotbar);
		obj.addProperty("inventorySwap", inventorySwap);
		obj.addProperty("autoOrientFacing", autoOrientFacing);
		obj.addProperty("hudEnabled", hudEnabled);
		obj.addProperty("soundEffects", soundEffects);
		obj.addProperty("breakWrongBlocks", breakWrongBlocks);

		try {
			Path parent = path.getParent();
			if (parent != null && !Files.exists(parent)) {
				Files.createDirectories(parent);
			}

			try (Writer writer = Files.newBufferedWriter(tempPath)) {
				GSON.toJson(obj, writer);
			}
			Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (Throwable t) {
			ShhhPrinter.LOGGER.error("Failed to save shhh-printer.json safely.", t);
		}
	}

	public static int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}

	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}

	// Getters and Setters
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public boolean toggleEnabled() { this.enabled = !this.enabled; return this.enabled; }

	public int getBlocksPerTick() { return blocksPerTick; }
	public void setBlocksPerTick(int blocksPerTick) { this.blocksPerTick = clamp(blocksPerTick, 1, 5); }

	public int getPlacementDelayTicks() { return placementDelayTicks; }
	public void setPlacementDelayTicks(int delay) { this.placementDelayTicks = clamp(delay, 0, 10); }

	public double getReachDistance() { return reachDistance; }
	public void setReachDistance(double reachDistance) { this.reachDistance = clamp(reachDistance, 2.0, 6.0); }

	public boolean isStrictAdjacent() { return strictAdjacent; }
	public void setStrictAdjacent(boolean strictAdjacent) { this.strictAdjacent = strictAdjacent; }

	public boolean isAirPlacement() { return airPlacement; }
	public void setAirPlacement(boolean airPlacement) { this.airPlacement = airPlacement; }

	public boolean isAutoSwitchHotbar() { return autoSwitchHotbar; }
	public void setAutoSwitchHotbar(boolean autoSwitchHotbar) { this.autoSwitchHotbar = autoSwitchHotbar; }

	public boolean isInventorySwap() { return inventorySwap; }
	public void setInventorySwap(boolean inventorySwap) { this.inventorySwap = inventorySwap; }

	public boolean isAutoOrientFacing() { return autoOrientFacing; }
	public void setAutoOrientFacing(boolean autoOrientFacing) { this.autoOrientFacing = autoOrientFacing; }

	public boolean isHudEnabled() { return hudEnabled; }
	public void setHudEnabled(boolean hudEnabled) { this.hudEnabled = hudEnabled; }

	public boolean isSoundEffects() { return soundEffects; }
	public void setSoundEffects(boolean soundEffects) { this.soundEffects = soundEffects; }

	public boolean isBreakWrongBlocks() { return breakWrongBlocks; }
	public void setBreakWrongBlocks(boolean breakWrongBlocks) { this.breakWrongBlocks = breakWrongBlocks; }
}
