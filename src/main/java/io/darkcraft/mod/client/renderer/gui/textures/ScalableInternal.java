package io.darkcraft.mod.client.renderer.gui.textures;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import net.minecraft.util.ResourceLocation;

public class ScalableInternal extends GuiTexture
{
	private final static ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/gui/scalable/internal.png");
	private final UVStore cUV;
	private final UVStore hUV;
	private final UVStore vUV;
	private final UVStore bUV;

	public ScalableInternal(int w, int h)
	{
		super(null, w, h);
		float pixel = 1/1024.0f;
		double horAm = (w / 1024.0);
		double verAm = (h / 1024.0);
		double bgSc = Math.max(horAm, verAm);
		cUV = new UVStore(0,pixel,0,pixel);
		hUV = new UVStore(0,horAm,0,pixel);
		vUV = new UVStore(0,pixel,0,verAm);
		bUV = new UVStore(0,horAm/bgSc,0,verAm/bgSc);
	}

	@Override
	public void render(float x, float y, float z, boolean draw)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		//Corners
		RenderHelper.bindTexture(tex);
		RenderHelper.uiFace(0,		0,		1, 1, z, cUV, draw);
		RenderHelper.uiFace(w-1,	0,		1, 1, z, cUV, draw);
		RenderHelper.uiFace(0,		h-1,	1, 1, z, cUV, draw);
		RenderHelper.uiFace(w-1,	h-1,	1, 1, z, cUV, draw);

		//HEdges
		RenderHelper.uiFace(1,		0,		w-2, 1, z, hUV, draw);
		RenderHelper.uiFace(1,		h-1,	w-2, 1, z, hUV, draw);

		//VEdges
		RenderHelper.uiFace(0,		1,	1, h-2, z, vUV, draw);
		RenderHelper.uiFace(w-1,	1,	1, h-2, z, vUV, draw);

		//BG
		RenderHelper.uiFace(1, 1, w-2, h-2, z, bUV, draw);
		GL11.glPopMatrix();
	}

}
