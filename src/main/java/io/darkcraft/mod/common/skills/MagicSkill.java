package io.darkcraft.mod.common.skills;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import skillapi.api.implement.ISkill;
import skillapi.api.implement.ISkillIcon;
import skillapi.api.internal.ISkillHandler;
import skillapi.api.internal.SkillVisibility;

public class MagicSkill implements ISkill
{
	private final String skillName;
	private ISkillIcon[] icons;

	public MagicSkill(String name)
	{
		skillName = name;
		icons = new ISkillIcon[5];
		for(int i = 0; i < icons.length; i++)
			icons[i] = new MagicSkillIcon(name, i, icons.length);
	}

	private String id;
	@Override
	public String getID()
	{
		if(id == null)
			return id = "darkcraft.skills.magic." + skillName;
		return id;
	}

	private String name;
	@Override
	public String getName()
	{
		if(name == null)
			return name = getID() + ".name";
		return name;
	}

	private String desc;
	@Override
	public String getDescription()
	{
		if(desc == null)
			return desc = getID() + ".desc";
		return desc;
	}

	@Override
	public ISkillIcon getIcon(ISkillHandler handler)
	{
		int level = MathHelper.clamp((handler.getLevel(this) * (icons.length-1)) / getMaximumSkillLevel(handler),0,icons.length);
		return icons[level];
	}

	@Override
	public SkillVisibility getVisibility()
	{
		return SkillVisibility.ALWAYS;
	}

	@Override
	public int getMinimumSkillLevel(ISkillHandler handler)
	{
		return 0;
	}

	@Override
	public int getMaximumSkillLevel(ISkillHandler handler)
	{
		return 100;
	}

	@Override
	public double getXPForNextLevel(int currentLevel, ISkillHandler handler)
	{
		return (currentLevel + 1) * 100;
	}

}
