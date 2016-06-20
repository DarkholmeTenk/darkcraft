package io.darkcraft.mod.common.magic.systems.soulspell;

import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import net.minecraft.nbt.NBTTagCompound;

public interface ISoulSpell
{
	public String id();

	public void tick(BlockCaster caster);

	public void setGemStand(GemStand stand);

	public void writeToNBT(NBTTagCompound nbt);

	public void readFromNBT(NBTTagCompound nbt);
}
