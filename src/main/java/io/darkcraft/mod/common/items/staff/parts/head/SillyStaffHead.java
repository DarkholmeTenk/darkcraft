package io.darkcraft.mod.common.items.staff.parts.head;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.items.staff.parts.DefaultStaffPart;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;

import org.lwjgl.opengl.GL11;

public class SillyStaffHead extends DefaultStaffPart implements IStaffHead
{
	private static final ResourceLocation defaultStaffModel = new ResourceLocation(DarkcraftMod.modName, "models/staff/defaultHead.obj");
	private static final ResourceLocation defaultStaffTex = new ResourceLocation(DarkcraftMod.modName, "textures/staff/defaultHead.png");

	public SillyStaffHead(ResourceLocation modelLocation, String name, ResourceLocation textureLocation)
	{
		super(modelLocation, name, textureLocation);
	}

	public SillyStaffHead(String name, ResourceLocation textureLocation)
	{
		this(defaultStaffModel, name, textureLocation);
	}

	public SillyStaffHead(String name)
	{
		this(defaultStaffModel, name, defaultStaffTex);
	}

	public SillyStaffHead(ResourceLocation model, String name)
	{
		this(model, name, defaultStaffTex);
	}

	@Override
	public void render()
	{
		if (model == null) model = AdvancedModelLoader.loadModel(objLoc);
		RenderHelper.bindTexture(tex);
		GL11.glPushMatrix();
		model.renderAll();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0, 0.75, 0);
		GL11.glRotated((System.currentTimeMillis() / 20.0) % 360, 0, 1, 0);
		GL11.glTranslated(0.25, 0, 0);
		GL11.glScaled(0.25, 0.25, 0.25);
		model.renderAll();
		GL11.glPopMatrix();
	}
}
