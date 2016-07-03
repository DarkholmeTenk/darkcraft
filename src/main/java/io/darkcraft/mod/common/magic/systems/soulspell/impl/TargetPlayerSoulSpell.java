package io.darkcraft.mod.common.magic.systems.soulspell.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TargetPlayerSoulSpell extends TargetMobsSoulSpell
{
	public TargetPlayerSoulSpell(NBTTagCompound nbt)
	{
		super(nbt);
	}

	@Override
	public String id()
	{
		return "dc.targetplayers";
	}

	@Override
	public boolean shouldTarget(Entity e)
	{
		return e instanceof EntityPlayer;
	}
}
