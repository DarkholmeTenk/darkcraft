package io.darkcraft.mod.client.renderer.gui.textures;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import net.minecraft.util.ResourceLocation;

public class VerticalScrollbarTexture extends GuiTexture
{
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/gui/scalable/scrollbar.png");
	private static final UVStore bar = new UVStore(0,1,0,0.1f);
	private static final UVStore u = new UVStore(0,1,0.2f,0.5f);
	private static final UVStore m = new UVStore(0,1,0.5f,0.7f);
	private static final UVStore b = new UVStore(0,1,0.7f,1);
	int bt;
	int bb;

	public VerticalScrollbarTexture(int h)
	{
		super(tex,16,h);
		bt = 16;
		bb = h-24;
	}

	public void render(float x, float y, float z, float barPos, boolean drawBar)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		//Bar
		RenderHelper.bindTexture(tex);
		//Top
		RenderHelper.uiFace(0,	0,	16, 24, 0, u, true);
		//Bottom
		RenderHelper.uiFace(0,	h-24,	16, 24, 0, b, true);
		//BG
		if(h > 48)
			RenderHelper.uiFace(0, 24, 16, h-48, z, m, true);
		if(drawBar)
		{
			float pos = MathHelper.interpolate(bb, bt, barPos);
			RenderHelper.uiFace(0,	pos,	16, 8, 0, bar, true);
		}
		GL11.glPopMatrix();
	}

}
