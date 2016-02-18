package io.darkcraft.mod.common.helpers;

import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.field.MagicFieldGlobal;

import java.util.WeakHashMap;

import net.minecraft.entity.EntityLivingBase;

public class Helper
{
	private static WeakHashMap<EntityLivingBase, EntityCaster>	entCastMap	= new WeakHashMap();

	private static MagicFieldGlobal mfg;

	public static EntityCaster getCaster(EntityLivingBase ent)
	{
		if (entCastMap.containsKey(ent)) return entCastMap.get(ent);
		EntityCaster caster = new EntityCaster(ent);
		entCastMap.put(ent, caster);
		return caster;
	}

	public static MagicFieldGlobal getMFG()
	{
		if(mfg != null) return mfg;
		mfg = new MagicFieldGlobal();
		mfg.load();
		mfg.save();
		return mfg;
	}


}
