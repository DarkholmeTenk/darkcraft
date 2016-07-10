package io.darkcraft.mod.common.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.magic.field.MagicFieldGlobal;
import io.darkcraft.mod.common.magic.items.staff.Staff;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.IExtendedEntityProperties;
import skillapi.api.implement.ISkill;

public class Helper
{
	private static WeakHashMap<EntityLivingBase, EntityCaster>	entCastMap	= new WeakHashMap();

	private static MagicFieldGlobal mfg;

	public static PlayerCaster getPlayerCaster(EntityPlayer pl)
	{
		if(pl == null) return null;
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

	public static boolean isCaster(ICaster c, EntityLivingBase ent)
	{
		if(c instanceof EntityCaster)
			return ent == ((EntityCaster)c).getCaster();
		return false;
	}

	public static void playFizzleNoise(SimpleDoubleCoordStore pos)
	{

	}

	public static MagicFieldGlobal getMFG()
	{
		if(mfg != null) return mfg;
		mfg = new MagicFieldGlobal();
		mfg.load();
		mfg.save();
		return mfg;
	}

	public static List<IComponent> sortComponents(Collection<IComponent> comps)
	{
		List<IComponent> list = new ArrayList();
		list.addAll(comps);
		if(list.isEmpty()) return list;
		Collections.sort(list, ComponentComparator.i);
		return list;
	}

	public static boolean canCast(EntityPlayer pl)
	{
		if(pl == null) return false;
		if(MagicConfig.castWithHand && (pl.getHeldItem() == null)) return true;
		return Staff.hasStaff(pl);
	}

	private static class ComponentComparator implements Comparator<IComponent>
	{
		private static ComponentComparator i = new ComponentComparator();

		public int compare(ISkill a, ISkill b)
		{
			if(a == b) return 0;
			if(a == null) return 1;
			if(b == null) return -1;
			String sa = a.getName();
			String sb = b.getName();
			return sa.compareTo(sb);
		}

		@Override
		public int compare(IComponent a, IComponent b)
		{
			if(a == b) return 0;
			if(a == null) return 1;
			if(b == null) return -1;
			int c = compare(a.getMainSkill(), b.getMainSkill());
			if(c != 0) return c;
			String na = StatCollector.translateToLocal(a.getUnlocalisedName());
			String nb = StatCollector.translateToLocal(b.getUnlocalisedName());
			return na.compareTo(nb);
		}

	}
}
