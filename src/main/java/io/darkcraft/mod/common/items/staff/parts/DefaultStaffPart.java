package io.darkcraft.mod.common.items.staff.parts;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.common.items.staff.ItemStaffHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public abstract class DefaultStaffPart implements IStaffPart
{
	public final ResourceLocation	objLoc;
	public final ResourceLocation	tex;
	protected IModelCustom			model;
	public final String				id;

	public DefaultStaffPart(ResourceLocation modelLocation, String name, ResourceLocation textureLocation)
	{
		objLoc = modelLocation;
		id = name;
		tex = textureLocation;
	}

	@Override
	public String id()
	{
		return id;
	}

	@Override
	public void render()
	{
		if (model == null) model = AdvancedModelLoader.loadModel(objLoc);
		GL11.glPushMatrix();
		RenderHelper.bindTexture(tex);
		model.renderAll();
		GL11.glPopMatrix();
	}

	@Override
	public double getPowerMult(ItemStaffHelper helper)
	{
		return 1;
	}

	@Override
	public boolean isDefault()
	{
		return true;
	}
}
