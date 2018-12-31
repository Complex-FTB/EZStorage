package com.zerofall.ezstorage;

import com.zerofall.ezstorage.events.XEventHandler;
import com.zerofall.ezstorage.gui.GuiHandler;
import com.zerofall.ezstorage.init.EZBlocks;
import com.zerofall.ezstorage.network.MyMessage;
import com.zerofall.ezstorage.network.PacketHandler;
import com.zerofall.ezstorage.network.RecipeMessage;
import com.zerofall.ezstorage.network.RecipePacketHandler;
import com.zerofall.ezstorage.proxy.CommonProxy;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = "ezstorage", name = "EZ Storage", version = "1.1.0", acceptedMinecraftVersions = "[1.7.10]", guiFactory = "com.zerofall.ezstorage.gui.EZGuiFactory")
public class EZStorage {
	@Mod.Instance("ezstorage")
	public static EZStorage instance;
	@cpw.mods.fml.common.SidedProxy(clientSide = "com.zerofall.ezstorage.proxy.ClientProxy", serverSide = "com.zerofall.ezstorage.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper networkWrapper;
	public static Configuration config;
	public EZTab creativeTab;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		EZConfiguration.syncConfig();
		this.creativeTab = new EZTab();
		EZBlocks.init();
		EZBlocks.register();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("ezChannel");
		networkWrapper.registerMessage(PacketHandler.class, MyMessage.class, 0, Side.SERVER);
		networkWrapper.registerMessage(RecipePacketHandler.class, RecipeMessage.class, 1, Side.SERVER);
		MinecraftForge.EVENT_BUS.register(new XEventHandler());
	}

	@SubscribeEvent
	public void onConfigChanged(final OnConfigChangedEvent event) {
		if (event.modID.equals("ezstorage"))
			EZConfiguration.syncConfig();
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		proxy.registerRenders();
		FMLCommonHandler.instance().bus().register(instance);
	}
}
