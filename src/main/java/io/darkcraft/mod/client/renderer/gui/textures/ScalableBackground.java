package io.darkcraft.mod.client.renderer.gui.textures;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import net.minecraft.util.ResourceLocation;

public class ScalableBackground extends GuiTexture
{
	private final static ResourceLocation cornr = new ResourceLocation(DarkcraftMod.modName, "textures/gui/scalable/corner.png");
	private final static ResourceLocation backg = new ResourceLocation(DarkcraftMod.modName, "textures/gui/scalable/bg.png");
	private final static ResourceLocation hedge = new ResourceLocation(DarkcraftMod.modName, "textures/gui/scalable/hedge.png");
	private final static ResourceLocation vedge = new ResourceLocation(DarkcraftMod.modName, "textures/gui/scalable/vedge.png");
	private final UVStore hUV;
	private final UVStore vUV;
	private final UVStore bUV;

	public ScalableBackground(int w, int h)
	{
		super(null, w, h);
		double horAm = (w / 1024.0);
		double verAm = (h / 1024.0);
		double bgSc = Math.max(horAm, verAm);
		hUV = new UVStore(0,horAm,0,1);
		vUV = new UVStore(0,1,0,verAm);
		bUV = new UVStore(0,horAm/bgSc,0,verAm/bgSc);
	}

	@Override
	public void render(float x, float y, float z, boolean draw)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		//Corners
		RenderHelper.bindTexture(cornr);
		RenderHelper.uiFace(0,		0,		16, 16, z, UVStore.defaultUV, draw);
		RenderHelper.uiFace(w-16,	0,		16, 16, z, UVStore.defaultUV, draw);
		RenderHelper.uiFace(0,		h-16,	16, 16, z, UVStore.defaultUV, draw);
		RenderHelper.uiFace(w-16,	h-16,	16, 16, z, UVStore.defaultUV, draw);

		//HEdges
		RenderHelper.bindTexture(hedge);
		RenderHelper.uiFace(16,		8,		w-32, 8, z, hUV, draw);
		RenderHelper.uiFace(16,		h-16,	w-32, 8, z, hUV, draw);

		//VEdges
		RenderHelper.bindTexture(vedge);
		RenderHelper.uiFace(8,		16,	8, h-32, z, vUV, draw);
		RenderHelper.uiFace(w-16,	16,	8, h-32, z, vUV, draw);

		//BG
		RenderHelper.bindTexture(backg);
		RenderHelper.uiFace(16, 16, w-32, h-32, z, bUV, draw);
		GL11.glPopMatrix();
	}

}
