package io.darkcraft.mod.common.magic.systems.soulspell;

import net.minecraft.nbt.NBTTagCompound;

public interface ISoulSpell
{
	public String id();

	public void tick();

	public void writeToNBT(NBTTagCompound nbt);

	public void readFromNBT(NBTTagCompound nbt);
}
