package com.zerofall.ezstorage.gui;

import com.zerofall.ezstorage.EZStorage;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class ConfigGui extends GuiConfig {

	public ConfigGui(final GuiScreen screen) {
		super(screen, new ConfigElement(EZStorage.config
				.getCategory("general")).getChildElements(), "EZ Storage", false, false,
				GuiConfig.getAbridgedConfigPath(EZStorage.config.toString()));
	}
}
