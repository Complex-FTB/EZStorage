package com.zerofall.ezstorage.init;

import com.zerofall.ezstorage.block.*;
import com.zerofall.ezstorage.tileentity.TileEntityInputPort;
import com.zerofall.ezstorage.tileentity.TileEntityStorageCore;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class EZBlocks {
	public static Block storage_core;
	public static Block storage_box;
	public static Block condensed_storage_box;
	public static Block hyper_storage_box;
	public static Block input_port;
	public static Block crafting_box;
	public static Block search_box;

	public static void init() {
		storage_core = new BlockStorageCore();
		storage_box = new BlockStorage();
		condensed_storage_box = new BlockCondensedStorage();
		hyper_storage_box = new BlockHyperStorage();
		input_port = new BlockInputPort();
		crafting_box = new BlockCraftingBox();
		search_box = new BlockSearchBox();
	}

	public static void register() {
		GameRegistry.registerBlock(storage_core, storage_core.getUnlocalizedName().substring(5));
		GameRegistry.registerTileEntity(TileEntityStorageCore.class, "TileEntityStorageCore");
		GameRegistry.registerBlock(storage_box, storage_box.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(condensed_storage_box, condensed_storage_box.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(hyper_storage_box, hyper_storage_box.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(input_port, input_port.getUnlocalizedName().substring(5));
		GameRegistry.registerTileEntity(TileEntityInputPort.class, "TileEntityInputPort");
		GameRegistry.registerBlock(crafting_box, crafting_box.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(search_box, search_box.getUnlocalizedName().substring(5));
		registerRecipes();
	}

	public static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(storage_core), new Object[] { "ABA", "BCB", "ABA",

				Character.valueOf('A'), Blocks.log, Character.valueOf('B'), Items.stick, Character.valueOf('C'), Blocks.chest });
		GameRegistry.addRecipe(new ItemStack(storage_box), new Object[] { "ABA", "B B", "ABA",

				Character.valueOf('A'), Blocks.log, Character.valueOf('B'), Blocks.chest });
		GameRegistry.addRecipe(new ItemStack(condensed_storage_box), new Object[] { "ACA", "CBC", "ACA",

				Character.valueOf('A'), Blocks.iron_block, Character.valueOf('B'), storage_box, Character.valueOf('C'), Blocks.iron_bars });
		GameRegistry.addRecipe(new ItemStack(hyper_storage_box), new Object[] { "ABA", "ACA", "AAA",

				Character.valueOf('A'), Blocks.obsidian, Character.valueOf('B'), Items.nether_star, Character.valueOf('C'), condensed_storage_box });
		GameRegistry.addRecipe(new ItemStack(input_port), new Object[] { "ABA", "BCB", "ABA",

				Character.valueOf('A'), Blocks.hopper, Character.valueOf('B'), Blocks.piston, Character.valueOf('C'), Blocks.quartz_block });
		GameRegistry.addRecipe(new ItemStack(crafting_box), new Object[] { "ABA", "BCB", "ABA",

				Character.valueOf('A'), Items.ender_eye, Character.valueOf('B'), Blocks.crafting_table, Character.valueOf('C'), Items.diamond });
		GameRegistry.addRecipe(new ItemStack(search_box), new Object[] { "ABA", "BCB", "ABA",

				Character.valueOf('A'), Blocks.iron_block, Character.valueOf('B'), Items.enchanted_book, Character.valueOf('C'), Items.compass });
	}
}
