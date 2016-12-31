package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.handlers.containers.EntityContainerHandler;
import io.darkcraft.darkcore.mod.handlers.containers.IEntityContainer;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public abstract class AbstractDarkcraftEffect extends AbstractEffect
{
	public int magnitude;
	public final ICaster caster;
	private final ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName,"textures/gui/effects/effects.png");

	protected final IEntityContainer<EntityLivingBase> container;

	public AbstractDarkcraftEffect(String _id, ICaster _caster, Entity ent, int _magnitude, int _duration, boolean _visible, boolean _doesTick, int _tickFreq)
	{
		super("darkcraft."+_id, ent, _duration, _visible, _doesTick, _tickFreq);
		caster = _caster;
		magnitude = _magnitude;
		container = EntityContainerHandler.getContainer(ent);
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

}
