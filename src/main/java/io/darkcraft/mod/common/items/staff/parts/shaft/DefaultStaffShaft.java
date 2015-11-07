package io.darkcraft.mod.common.items.staff.parts.shaft;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.items.staff.parts.DefaultStaffPart;
import net.minecraft.util.ResourceLocation;

public class DefaultStaffShaft extends DefaultStaffPart implements IStaffShaft
{
	private static final ResourceLocation defaultStaffModel = new ResourceLocation(DarkcraftMod.modName, "models/staff/defaultShaft.obj");
	private static final ResourceLocation defaultStaffTex = new ResourceLocation(DarkcraftMod.modName, "textures/staff/defaultShaft.png");

	public DefaultStaffShaft(ResourceLocation modelLocation, String name, ResourceLocation textureLocation)
	{
		super(modelLocation, name, textureLocation);
	}

	public DefaultStaffShaft(String name, ResourceLocation textureLocation)
	{
		this(defaultStaffModel, name, textureLocation);
	}

	public DefaultStaffShaft(String name)
	{
		this(defaultStaffModel, name, defaultStaffTex);
	}

	public DefaultStaffShaft(ResourceLocation model, String name)
	{
		this(model, name, defaultStaffTex);
	}

}
