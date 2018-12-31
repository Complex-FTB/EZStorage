package com.zerofall.ezstorage.gui;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

public class EZGuiFactory
		implements IModGuiFactory {
	public void initialize(Minecraft minecraftInstance) {
	}

	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ConfigGui.class;
	}

	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element) {
		return null;
	}
}
