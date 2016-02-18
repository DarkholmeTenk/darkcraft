package io.darkcraft.mod.common.magic.field;

import io.darkcraft.darkcore.mod.abstracts.AbstractWorldDataStore;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.nbt.NBTTagCompound;

public class MagicFieldGlobal extends AbstractWorldDataStore
{
	public MagicFieldGlobal(String s){super(s);}

	public MagicFieldGlobal()
	{
		super("MagicFieldGlobal",0);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		deityName = nbt.getString("deity");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	private String deityName;

	public boolean doesMagicExist()
	{
		return (!MagicConfig.deity) || (deityName != null);
	}

	public String getDeityName()
	{
		return deityName;
	}

}
