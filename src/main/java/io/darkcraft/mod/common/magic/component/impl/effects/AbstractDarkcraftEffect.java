package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.mod.common.magic.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractDarkcraftEffect extends AbstractEffect
{
	protected int magnitude;
	public final ICaster caster;

	public AbstractDarkcraftEffect(String _id, ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration, boolean _visible, boolean _doesTick, int _tickFreq)
	{
		super("darkcraft."+_id, ent, _duration, _visible, _doesTick, _tickFreq);
		caster = _caster;
		magnitude = _magnitude;
	}

	@Override
	protected void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("mag", magnitude);
	}

	@Override
	protected void readFromNBT(NBTTagCompound nbt)
	{
		magnitude = nbt.getInteger("mag");
	}

}
