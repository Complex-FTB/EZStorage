package com.zerofall.ezstorage;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class EZConfiguration {
	public static int basicCapacity;
	public static int condensedCapacity;
	public static int hyperCapacity;
	public static final String BASIC_STORAGE_NAME = "Basic Storage Capacity";
	public static final int BASIC_STORAGE_CAPACITY_DEFAULT = 200;
	public static final String CONDENSED_STORAGE_NAME = "Condensed Storage Capacity";
	public static final int CONDENSED_STORAGE_CAPACITY_DEFAULT = 4000;
	public static final String HYPER_STORAGE_NAME = "Hyper Storage Capacity";
	public static final int HYPER_STORAGE_CAPACITY_DEFAULT = 800000;

	public static void syncConfig() {
		cpw.mods.fml.common.FMLCommonHandler.instance().bus().register(EZStorage.instance);
		Configuration config = EZStorage.config;
		String BASIC = "general"+"."+"capacities";
		basicCapacity = config.get(BASIC, "Basic Storage Capacity", 200).getInt();
		condensedCapacity = config.get(BASIC, "Condensed Storage Capacity", 4000).getInt();
		hyperCapacity = config.get(BASIC, "Hyper Storage Capacity", 800000).getInt();
		if (EZStorage.config.hasChanged()) {
			EZStorage.config.save();
		}
	}
}
