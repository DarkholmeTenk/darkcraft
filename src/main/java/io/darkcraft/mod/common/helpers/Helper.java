package io.darkcraft.mod.common.helpers;

import io.darkcraft.mod.common.magic.caster.EntityCaster;

import java.util.WeakHashMap;

import net.minecraft.entity.EntityLivingBase;

public class Helper
{
	private static WeakHashMap<EntityLivingBase, EntityCaster>	entCastMap	= new WeakHashMap();

	public static EntityCaster getCaster(EntityLivingBase ent)
	{
		if (entCastMap.containsKey(ent)) return entCastMap.get(ent);
		EntityCaster caster = new EntityCaster(ent);
		entCastMap.put(ent, caster);
		return caster;
	}
}
