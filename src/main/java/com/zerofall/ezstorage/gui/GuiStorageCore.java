package com.zerofall.ezstorage.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.zerofall.ezstorage.container.ContainerStorageCore;
import com.zerofall.ezstorage.network.MyMessage;
import com.zerofall.ezstorage.tileentity.TileEntityStorageCore;
import com.zerofall.ezstorage.util.EZItemRenderer;
import com.zerofall.ezstorage.util.ItemGroup;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiStorageCore extends GuiContainer {
	TileEntityStorageCore tileEntity;
	EZItemRenderer ezRenderer;
	int scrollRow = 0;
	private boolean isScrolling = false;
	private boolean wasClicking = false;
	private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private static final ResourceLocation searchBar = new ResourceLocation("textures/gui/container/creative_inventory/tab_item_search.png");
	private float currentScroll;
	private GuiTextField searchField;
	private List<ItemGroup> filteredList;

	@Override
	public void initGui() {
		super.initGui();
		this.searchField = new GuiTextField(this.fontRendererObj, this.guiLeft+10, this.guiTop+6, 80, this.fontRendererObj.FONT_HEIGHT);
		this.searchField.setMaxStringLength(20);
		this.searchField.setEnableBackgroundDrawing(false);
		this.searchField.setTextColor(16777215);
		this.searchField.setCanLoseFocus(true);
		this.searchField.setFocused(true);
		this.searchField.setText("");
		this.filteredList = new ArrayList<ItemGroup>(this.tileEntity.inventory.inventory);
	}

	public GuiStorageCore(final EntityPlayer player, final World world, final int x, final int y, final int z) {
		super(new ContainerStorageCore(player, world, x, y, z));
		this.tileEntity = (TileEntityStorageCore) world.getTileEntity(x, y, z);
		this.xSize = 195;
		this.ySize = 222;
	}

	public GuiStorageCore(final ContainerStorageCore containerStorageCore, final World world, final int x, final int y, final int z) {
		super(containerStorageCore);
		this.tileEntity = (TileEntityStorageCore) world.getTileEntity(x, y, z);
		this.xSize = 195;
		this.ySize = 222;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		this.mc.renderEngine.bindTexture(getBackground());
		final int x = (this.width-this.xSize)/2;
		final int y = (this.height-this.ySize)/2;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		this.searchField.setVisible(this.tileEntity.hasSearchBox);
		if (this.tileEntity.hasSearchBox) {
			this.mc.renderEngine.bindTexture(searchBar);
			drawTexturedModalRect(this.guiLeft+8, this.guiTop+4, 80, 4, 90, 12);
			this.searchField.drawTextBox();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		handleScrolling(mouseX, mouseY);
		//		updateFilteredItems();
		this.fontRendererObj.drawString(this.tileEntity.inventory.getTotalCount()+"/"+this.tileEntity.inventory.maxItems, 125, 6, 4210752);
		int x = 8;
		int y = 18;
		RenderHelper.enableGUIStandardItemLighting();

		this.zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		if (this.ezRenderer==null)
			this.ezRenderer = new EZItemRenderer();

		boolean finished = false;
		for (int i = 0; i<rowsVisible(); i++) {
			x = 8;
			for (int j = 0; j<9; j++) {
				int index = i*9+j;
				index = this.scrollRow*9+index;
				if (index>=this.filteredList.size()) {
					finished = true;
					break;
				}

				final ItemGroup group = this.filteredList.get(index);
				final ItemStack stack = group.itemStack;
				FontRenderer font = null;
				if (stack!=null)
					font = stack.getItem().getFontRenderer(stack);
				if (font==null)
					font = this.fontRendererObj;

				itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), stack, x, y);
				this.ezRenderer.renderItemOverlayIntoGUI(font, stack, x, y, ""+group.count);
				x += 18;
			}
			if (finished)
				break;
			y += 18;
		}

		final int i1 = 175;
		final int k = 18;
		final int l = k+108;
		this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
		drawTexturedModalRect(i1, k+(int) ((l-k-17)*this.currentScroll), 232, 0, 12, 15);
		this.zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		final Integer slot = getSlotAt(mouseX, mouseY);
		if (slot!=null) {
			//			int mode = 0;
			//			if (GuiScreen.isShiftKeyDown())
			//				mode = 1;
			int index = this.tileEntity.inventory.slotCount();
			if (slot.intValue()<this.filteredList.size()) {
				final ItemGroup group = this.filteredList.get(slot.intValue());
				if (group!=null) {
					index = this.tileEntity.inventory.inventory.indexOf(group);
					if (index<0)
						return;
					renderToolTip(group.itemStack, mouseX, mouseY);
				}
			}
		}
	}

	@Override
	protected void keyTyped(final char typedChar, final int keyCode) {
		if (!checkHotbarKeys(keyCode))
			if (this.tileEntity.hasSearchBox&&this.searchField.isFocused()&&this.searchField.textboxKeyTyped(typedChar, keyCode))
				updateFilteredItems();
			else
				super.keyTyped(typedChar, keyCode);
	}

	private void updateFilteredItems() {
		this.filteredList = new ArrayList<ItemGroup>(this.tileEntity.inventory.inventory);
		final Iterator<ItemGroup> iterator = this.filteredList.iterator();
		final String s1 = this.searchField.getText().toLowerCase();

		while (iterator.hasNext()) {
			final ItemGroup group = iterator.next();
			final ItemStack itemstack = group.itemStack;
			boolean flag = false;
			final Iterator<?> iterator1 = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();

			while (iterator1.hasNext()) {
				final String s = (String) iterator1.next();

				if (EnumChatFormatting.getTextWithoutFormattingCodes(s).toLowerCase().contains(s1))
					flag = true;
			}
			if (!flag)
				iterator.remove();
		}
	}

	private void handleScrolling(final int mouseX, final int mouseY) {
		final boolean flag = Mouse.isButtonDown(0);

		final int k = this.guiLeft;
		final int l = this.guiTop;
		final int i1 = k+175;
		final int j1 = l+18;
		final int k1 = i1+14;
		final int l1 = j1+108;

		if (!this.wasClicking&&flag&&mouseX>=i1&&mouseY>=j1&&mouseX<k1&&mouseY<l1)
			this.isScrolling = true;

		if (!flag)
			this.isScrolling = false;

		this.wasClicking = flag;

		if (this.isScrolling) {
			this.currentScroll = (mouseY-j1-7.5F)/(l1-j1-15.0F);
			this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
			scrollTo(this.currentScroll);
		}
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		final Integer slot = getSlotAt(mouseX, mouseY);
		if (slot!=null) {
			int mode = 0;
			if (GuiScreen.isShiftKeyDown())
				mode = 1;
			int index = this.tileEntity.inventory.slotCount();
			if (slot.intValue()<this.filteredList.size()) {
				final ItemGroup group = this.filteredList.get(slot.intValue());
				if (group!=null) {
					index = this.tileEntity.inventory.inventory.indexOf(group);
					if (index<0)
						return;
				}
			}
			com.zerofall.ezstorage.EZStorage.networkWrapper.sendToServer(new MyMessage(index, mouseButton, mode));
			final ContainerStorageCore container = (ContainerStorageCore) this.inventorySlots;
			container.customSlotClick(index, mouseButton, mode, this.mc.thePlayer);
		} else {
			final int elementX = this.searchField.xPosition;
			final int elementY = this.searchField.yPosition;
			if (mouseX>=elementX&&mouseX<=elementX+this.searchField.width&&mouseY>=elementY&&mouseY<=elementY+this.searchField.height)
				this.searchField.setFocused(true);
			else
				this.searchField.setFocused(false);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void handleMouseClick(final Slot p_146984_1_, final int p_146984_2_, final int p_146984_3_, final int p_146984_4_) {
		super.handleMouseClick(p_146984_1_, p_146984_2_, p_146984_3_, p_146984_4_);
		updateFilteredItems();
	}

	private Integer getSlotAt(final int x, final int y) {
		final int startX = this.guiLeft+8;
		final int startY = this.guiTop+18;

		final int clickedX = x-startX;
		final int clickedY = y-startY;

		if (clickedX>0&&clickedY>0) {
			final int column = clickedX/18;
			if (column<9) {
				final int row = clickedY/18;
				if (row<rowsVisible()) {
					final int slot = row*9+column+this.scrollRow*9;
					return Integer.valueOf(slot);
				}
			}
		}
		return null;
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		final int i = Mouse.getEventDWheel();

		if (i!=0) {
			final int rows = (int) Math.ceil(this.tileEntity.inventory.slotCount()/9F);
			final int max = rows-rowsVisible();
			final float row = 1F/max;
			if (i>0&&this.scrollRow>0) {
				this.scrollRow--;
				this.currentScroll = MathHelper.clamp_float(this.currentScroll-row, 0F, 1F);
			} else if (i<0&&this.scrollRow<max) {
				this.scrollRow++;
				this.currentScroll = MathHelper.clamp_float(this.currentScroll+row, 0F, 1F);
			}
		}
	}

	private void scrollTo(final float scroll) {
		final int i = (this.tileEntity.inventory.slotCount()+8)/9-rowsVisible();
		int j = (int) (scroll*i+0.5D);
		if (j<0)
			j = 0;
		this.scrollRow = j;
		System.out.println(this.scrollRow);
	}

	protected ResourceLocation getBackground() {
		return new ResourceLocation("ezstorage:textures/gui/storageScrollGui.png");
	}

	public int rowsVisible() {
		return 6;
	}
}
