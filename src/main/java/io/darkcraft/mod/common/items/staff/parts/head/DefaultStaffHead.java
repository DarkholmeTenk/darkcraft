package io.darkcraft.mod.common.items.staff.parts.head;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.items.staff.parts.DefaultStaffPart;
import net.minecraft.util.ResourceLocation;

public class DefaultStaffHead extends DefaultStaffPart implements IStaffHead
{
	private static final ResourceLocation defaultStaffModel = new ResourceLocation(DarkcraftMod.modName, "models/staff/defaultHead.obj");
	private static final ResourceLocation defaultStaffTex = new ResourceLocation(DarkcraftMod.modName, "textures/staff/defaultHead.png");

	public DefaultStaffHead(ResourceLocation modelLocation, String name, ResourceLocation textureLocation)
	{
		super(modelLocation, name, textureLocation);
	}

	public DefaultStaffHead(String name, ResourceLocation textureLocation)
	{
		this(defaultStaffModel, name, textureLocation);
	}

	public DefaultStaffHead(String name)
	{
		this(defaultStaffModel, name, defaultStaffTex);
	}

	public DefaultStaffHead(ResourceLocation model, String name)
	{
		this(model, name, defaultStaffTex);
	}

}
