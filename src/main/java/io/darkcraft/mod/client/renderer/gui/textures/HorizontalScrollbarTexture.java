package io.darkcraft.mod.client.renderer.gui.textures;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import net.minecraft.util.ResourceLocation;

public class HorizontalScrollbarTexture extends GuiTexture
{
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/gui/scalable/scrollbarH.png");
	private static final UVStore bar = new UVStore(0,.1f,0,1);
	private static final UVStore l = new UVStore(0.2f,0.5f,0,1);
	private static final UVStore m = new UVStore(0.5f,0.7f,0,1);
	private static final UVStore r = new UVStore(0.7f,1,0,1);
	int bl;
	int br;

	public HorizontalScrollbarTexture(int w)
	{
		super(tex,w,16);
		bl = 16;
		br = w-24;
	}

	public void render(float x, float y, float z, float barPos, boolean drawBar)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		RenderHelper.bindTexture(tex);
		//Top
		RenderHelper.uiFace(0,	0,	24, 16, 0, l, true);
		//Bottom
		RenderHelper.uiFace(w-24, 0,	24, 16, 0, r, true);
		//BG
		if(w > 48)
			RenderHelper.uiFace(24, 0, w-48, h, z, m, true);
		if(drawBar)
		{
			float pos = MathHelper.interpolate(br, bl, barPos);
			RenderHelper.uiFace(pos, 0,	8, 16, 0, bar, true);
		}
		GL11.glPopMatrix();
	}

}
