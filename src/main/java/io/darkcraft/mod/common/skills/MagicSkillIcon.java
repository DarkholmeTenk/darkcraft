package io.darkcraft.mod.common.skills;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkillIcon;

public class MagicSkillIcon implements ISkillIcon
{
	private UVStore uv;
	private int index;
	private int num;
	private ResourceLocation rl;

	public MagicSkillIcon(String name, int i, int max)
	{
		index = i;
		num = Math.max(1, max);
		//rl = null;
		rl = new ResourceLocation("darkcraft","textures/gui/skills/"+name+".png");
		uv = new UVStore(index / (double)num, (index+1)/(double)num,0,1);
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return rl;
	}

	@Override
	public UVStore getUV()
	{
		return uv;
	}

}
