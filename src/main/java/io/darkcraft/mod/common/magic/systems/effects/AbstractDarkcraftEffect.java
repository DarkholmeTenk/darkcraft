package io.darkcraft.mod.common.magic.systems.effects;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractDarkcraftEffect extends AbstractEffect
{
	public int magnitude;
	public final ICaster caster;
	private final ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName,"textures/gui/effects/effects.png");

	public AbstractDarkcraftEffect(String _id, ICaster _caster, EntityLivingBase ent, int _magnitude, int _duration, boolean _visible, boolean _doesTick, int _tickFreq)
	{
		super("darkcraft."+_id, ent, _duration<0 ? -1 : _duration, _visible, _doesTick, _tickFreq);
		caster = _caster;
		magnitude = _magnitude;
	}

	@Override
	public ResourceLocation getIcon()
	{
		return rl;
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

	public static int getMagnitude(EntityEffectStore ees, String id)
	{
		AbstractEffect eff = ees.getEffect(id);
		if(eff instanceof AbstractDarkcraftEffect)
			return ((AbstractDarkcraftEffect) eff).magnitude;
		return -1;
	}

	@Override
	public AbstractEffect combine(AbstractEffect newEffect)
	{
		if((duration == -1) || ((duration - getTT()) > newEffect.duration)) return this;
		return newEffect;
	}

}
