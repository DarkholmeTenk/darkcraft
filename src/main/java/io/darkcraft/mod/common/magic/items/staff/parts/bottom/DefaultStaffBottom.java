package io.darkcraft.mod.common.magic.items.staff.parts.bottom;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.items.staff.parts.DefaultStaffPart;
import net.minecraft.util.ResourceLocation;

public class DefaultStaffBottom extends DefaultStaffPart implements IStaffBottom
{
	private static final ResourceLocation defaultStaffModel = new ResourceLocation(DarkcraftMod.modName, "models/staff/defaultBottom.obj");
	private static final ResourceLocation defaultStaffTex = new ResourceLocation(DarkcraftMod.modName, "textures/staff/defaultBottom.png");

	public DefaultStaffBottom(ResourceLocation modelLocation, String name, ResourceLocation textureLocation)
	{
		super(modelLocation, name, textureLocation);
	}

	public DefaultStaffBottom(String name, ResourceLocation textureLocation)
	{
		this(defaultStaffModel, name, textureLocation);
	}

	public DefaultStaffBottom(String name)
	{
		this(defaultStaffModel, name, defaultStaffTex);
	}

	public DefaultStaffBottom(ResourceLocation model, String name)
	{
		this(model, name, defaultStaffTex);
	}

}
