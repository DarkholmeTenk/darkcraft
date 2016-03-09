package io.darkcraft.mod.common.helpers;

import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.field.MagicFieldGlobal;

import java.util.WeakHashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.IExtendedEntityProperties;

public class Helper
{
	private static WeakHashMap<EntityLivingBase, EntityCaster>	entCastMap	= new WeakHashMap();

	private static MagicFieldGlobal mfg;

	public static PlayerCaster getPlayerCaster(EntityPlayer pl)
	{
		IExtendedEntityProperties ieep = pl.getExtendedProperties("dcPC");
		PlayerCaster caster;
		if(ieep instanceof PlayerCaster)
			caster = (PlayerCaster) ieep;
		else
		{
			caster = new PlayerCaster(pl);
			pl.registerExtendedProperties("dcPC", caster);
		}
		return caster;
	}

	public static EntityCaster getCaster(EntityLivingBase ent)
	{
		if(ent instanceof EntityPlayer)
			return getPlayerCaster((EntityPlayer) ent);
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
