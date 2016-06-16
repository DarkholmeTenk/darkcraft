package io.darkcraft.mod.common.magic.symbolic;

import net.minecraft.nbt.NBTTagCompound;

public interface ISymbolicSpell
{
	/**
	 * Called from the root node every tick
	 */
	public void tick();

	/**
	 * Called when the glyph is broken and the spell cancelled as a result
	 */
	public void cancel();

	/**
	 * Reads nbt data that is saved to disk
	 * @param nbt
	 */
	public void read(NBTTagCompound nbt);

	/**
	 * Writes nbt data that is saved to disk
	 * @param nbt
	 */
	public void write(NBTTagCompound nbt);

	/**
	 * Read data that has been transmitted to the client (or saved to disk)
	 * @param nbt
	 */
	public void readTrans(NBTTagCompound nbt);

	/**
	 * Writes nbt that will be both saved to disk and transmitted to the client.
	 * @param nbt
	 */
	public void writeTrans(NBTTagCompound nbt);
}
