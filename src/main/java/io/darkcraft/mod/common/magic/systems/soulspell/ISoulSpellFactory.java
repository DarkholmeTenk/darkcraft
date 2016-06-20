package io.darkcraft.mod.common.magic.systems.soulspell;

import net.minecraft.nbt.NBTTagCompound;

public interface ISoulSpellFactory
{
	/**
	 * This method will be called when a soul gem with a soul spell is placed on to a gem stand
	 * @param id the id of the soul spell
	 * @param nbt any saved nbt data
	 * @return null if the id does not match your factory, or a soul spell if it does
	 */
	public ISoulSpell createSoulSpell(String id, NBTTagCompound nbt);
}
