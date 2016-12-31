package io.darkcraft.mod.common.magic.systems.effects;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EffectResistFire extends AbstractDamageResistEffect
{
	private final static UVStore rfUV = new UVStore(0.3,0.4,0.1,0.2);
	private final static String[] protectedAgainst = new String[]{"fireball", "inFire", "onFire", "lava"};

	public EffectResistFire(ICaster _caster, Entity ent, int _magnitude, int _duration)
	{
		super("resistfire", _caster, ent, _magnitude, _duration, true, false, 0);
	}

	@Override
	public UVStore getIconLocation()
	{
		return rfUV;
	}

	@Override
	public void apply(){}

	@Override
	public float getNewDamage(DamageSource ds, float oldDamage)
	{
		boolean work = ds.isFireDamage();
		if(!work)
			for(String s : protectedAgainst)
			{
				if(s.equals(ds.damageType))
				{
					work = true;
					break;
				}
			}
		if(!work) return oldDamage;

		return damageReduce(oldDamage);
	}

}
