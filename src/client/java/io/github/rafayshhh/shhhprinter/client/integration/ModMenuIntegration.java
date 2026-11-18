package io.github.rafayshhh.shhhprinter.client.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.rafayshhh.shhhprinter.client.gui.ShhhPrinterConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ShhhPrinterConfigScreen::new;
	}
}
