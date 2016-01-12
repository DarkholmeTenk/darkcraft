package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;
import io.darkcraft.mod.common.magic.field.MagicField;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MagicFieldMeasurer extends AbstractMFTileEntity implements IActivatable
{
	private double[][] vals = new double[5][5];

	@Override
	public void init()
	{
		for(double[] da : vals)
			for(int i = 0; i < da.length; i++)
				da[i] = 0;
		if(ServerHelper.isServer())
			getMF();
	}

	@Override
	public void tick()
	{
		if((tt % 100) == 0) sendUpdate();
	}

	public double[][] getMagicFieldValues()
	{
		if(ServerHelper.isServer())
		{
			MagicField mf = getMF();
			int shift = MagicConfig.fieldSize;
			int midI = vals.length / 2;
			for(int i = 0; i < vals.length; i++)
			{
				int midJ = vals[i].length / 2;
				int x = xCoord + ((i - midI) << shift);
				for(int j = 0; j < vals[i].length; j++)
				{
					int z = zCoord + ((j - midJ) << shift);
					vals[i][j] = mf.getFieldStrength(x, yCoord, z);
				}
			}
		}
		return vals;
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if(ServerHelper.isClient())
			return true;
		MagicField mf = getMF();
		mf.addToFieldStrength(coords(), ent.isSneaking() ? -10 : 10);
		mf.sendUpdate();
		sendUpdate();
		return true;
	}

	@Override
	public void writeTransmittableOnly(NBTTagCompound nbt)
	{
		nbt.setInteger("valsSize", vals.length);
		vals = getMagicFieldValues();
		for(int i = 0; i < vals.length; i++)
			for(int j = 0; j < vals[i].length; j++)
				nbt.setDouble(i+","+j, vals[i][j]);
	}

	@Override
	public void readTransmittableOnly(NBTTagCompound nbt)
	{
		int size = nbt.getInteger("valsSize");
		if(size != vals.length)
			vals = new double[size][size];
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				vals[i][j] = nbt.getDouble(i+","+j);
	}

}
