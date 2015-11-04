package io.darkcraft.mod.common.skills;

import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkillIcon;

public class MagicSkillIcon implements ISkillIcon
{
	private int index;
	private int num;
	private ResourceLocation rl;

	public MagicSkillIcon(String name, int i, int max)
	{
		index = i;
		num = Math.max(1, max);
		//rl = null;
		rl = new ResourceLocation("darkcraft","textures/gui/skills/"+name+".png");
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return rl;
	}

	@Override
	public double u()
	{
		return index / (double)num;
	}

	@Override
	public double U()
	{
		return (index + 1) / (double)num;
	}

	@Override
	public double v()
	{
		return 0;
	}

	@Override
	public double V()
	{
		return 1;
	}

}
