package io.darkcraft.mod.abstracts;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntitySer;
import io.darkcraft.mod.common.magic.field.MagicField;
import io.darkcraft.mod.common.magic.field.MagicFieldFactory;

public abstract class AbstractMFTileEntity extends AbstractTileEntitySer
{
	public MagicField getMF()
	{
		return MagicFieldFactory.getMagicField(this);
	}

	public MagicField getMagicField()
	{
		return getMF();
	}

	public double getMFValue()
	{
		return getMF().getFieldStrength(coords());
	}

	public double getMagicFieldValue()
	{
		return getMFValue();
	}
}
