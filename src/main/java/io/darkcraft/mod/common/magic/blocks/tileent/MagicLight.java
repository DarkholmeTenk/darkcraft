package io.darkcraft.mod.common.magic.blocks.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import net.minecraft.nbt.NBTTagCompound;

public class MagicLight extends AbstractTileEntity
{
	public int ct;

	{
		ct = (int) (System.currentTimeMillis() % 32000);
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		nbt.setInteger("ct", ct);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		ct = nbt.getInteger("ct");
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}
}
