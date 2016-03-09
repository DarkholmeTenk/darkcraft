package io.darkcraft.mod.common.magic.component.impl.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import net.minecraft.entity.EntityLivingBase;

public class EffectDamage extends AbstractDarkcraftEffect
{
	private static final UVStore lqUV = new UVStore(0,0.1,0,0.1);
	private static final UVStore hqUV = new UVStore(0,0.1,0.1,0.2);

	public EffectDamage(EntityLivingBase ent, ICaster caster, int magnitude, int duration)
	{
		super("damage", caster, ent, magnitude, duration, true, true, 20);
	}

	@Override
	public UVStore getIconLocation()
	{
		if(magnitude > 20)
			return hqUV;
		return lqUV;
	}

	@Override
	public void apply()
	{
		getEntity().attackEntityFrom(MagicalRegistry.magicDamage, magnitude);
		System.out.println("Damage"+getTT()+"/"+duration);
		if(caster instanceof EntityCaster)
		{
			EntityLivingBase ent = ((EntityCaster) caster).getCaster();
			getEntity().setRevengeTarget(ent);
		}
	}

}
