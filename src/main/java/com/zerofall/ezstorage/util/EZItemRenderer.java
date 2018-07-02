package com.zerofall.ezstorage.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;

public class EZItemRenderer extends net.minecraft.client.renderer.entity.RenderItem {
	public void renderItemOverlayIntoGUI(final FontRenderer fr, final ItemStack stack, final int xPosition, final int yPosition, final String text) {
		if (stack!=null) {
			final float ScaleFactor = 0.5F;
			final float RScaleFactor = 1.0F/ScaleFactor;
			final int offset = 0;

			final boolean unicodeFlag = fr.getUnicodeFlag();
			fr.setUnicodeFlag(false);

			long amount = Long.parseLong(text);

			if (amount>999999999999L)
				amount = 999999999999L;
			if (amount!=0L) {
				if (stack.getItem().showDurabilityBar(stack)) {
					final double health = stack.getItem().getDurabilityForDisplay(stack);
					final int j1 = (int) Math.round(13.0D-health*13.0D);
					final int k = (int) Math.round(255.0D-health*255.0D);
					GL11.glDisable(2896);
					GL11.glDisable(2929);
					GL11.glDisable(3553);
					GL11.glDisable(3008);
					GL11.glDisable(3042);
					final Tessellator tessellator = Tessellator.instance;
					final int l = 255-k<<16|k<<8;
					final int i1 = (255-k)/4<<16|0x3F00;

					renderQuad(tessellator, xPosition+2, yPosition+13, 13, 2, 0);
					renderQuad(tessellator, xPosition+2, yPosition+13, 12, 1, i1);
					renderQuad(tessellator, xPosition+2, yPosition+13, j1, 1, l);

					GL11.glEnable(3008);
					GL11.glEnable(3553);
					GL11.glEnable(2896);
					GL11.glEnable(2929);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}

				String var6 = String.valueOf(Math.abs(amount));

				if (amount>999999999L)
					var6 = String.valueOf((int) Math.floor(amount/1.0E9D))+'B';
				else if (amount>99999999L)
					var6 = "."+(int) Math.floor(amount/1.0E8D)+'B';
				else if (amount>999999L)
					var6 = String.valueOf((int) Math.floor(amount/1000000.0D))+'M';
				else if (amount>99999L)
					var6 = "."+(int) Math.floor(amount/100000.0D)+'M';
				else if (amount>9999L)
					var6 = String.valueOf((int) Math.floor(amount/1000.0D))+'K';

				GL11.glDisable(2896);
				GL11.glDisable(2929);
				GL11.glPushMatrix();
				GL11.glScaled(ScaleFactor, ScaleFactor, ScaleFactor);
				final int X = (int) ((xPosition+offset+16.0F-fr.getStringWidth(var6)*ScaleFactor)*RScaleFactor);
				final int Y = (int) ((yPosition+offset+16.0F-7.0F*ScaleFactor)*RScaleFactor);
				fr.drawStringWithShadow(var6, X, Y, 16777215);
				GL11.glPopMatrix();
				GL11.glEnable(2896);
				GL11.glEnable(2929);
			}

			fr.setUnicodeFlag(unicodeFlag);
		}
	}

	private void renderQuad(final Tessellator p_77017_1_, final int p_77017_2_, final int p_77017_3_, final int p_77017_4_, final int p_77017_5_, final int p_77017_6_) {
		p_77017_1_.startDrawingQuads();
		p_77017_1_.setColorOpaque_I(p_77017_6_);
		p_77017_1_.addVertex(p_77017_2_+0, p_77017_3_+0, 0.0D);
		p_77017_1_.addVertex(p_77017_2_+0, p_77017_3_+p_77017_5_, 0.0D);
		p_77017_1_.addVertex(p_77017_2_+p_77017_4_, p_77017_3_+p_77017_5_, 0.0D);
		p_77017_1_.addVertex(p_77017_2_+p_77017_4_, p_77017_3_+0, 0.0D);
		p_77017_1_.draw();
	}
}
