package io.darkcraft.mod.common.magic.systems.spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import io.darkcraft.api.magic.ISpellable;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.impl.PrimMapper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.event.spell.SpellApplyBlockEvent;
import io.darkcraft.mod.common.magic.event.spell.SpellApplyEntityEvent;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

import skillapi.api.implement.ISkill;
import skillapi.api.internal.ISkillHandler;

public class Spell
{
	public final String name;
	public final CastType type;
	public final ComponentInstance[] components;
	public final ComponentInstance mostExpensiveComponent;
	private final int maxArea;
	public final boolean affectBlocks;
	public final boolean affectEntities;

	public Spell(String _name, ComponentInstance[] componentInstances)
	{
		this(_name, componentInstances, CastType.PROJECTILE);
	}

	public Spell(String _name, ComponentInstance[] componentInstances, CastType castType)
	{
		name = _name;
		components = componentInstances.clone();
		type = castType;
		double c = 0;
		ComponentInstance mec = null;
		int ma = 0;
		boolean ae = false;
		boolean ab = false;
		for(ComponentInstance ci : components)
		{
			ae = ae || ci.component.applyToEnt();
			ab = ab || ci.component.applyToBlock();
			if(ci.area > ma)
				ma = ci.area;
			if(ci.cost > c)
			{
				c = ci.cost;
				mec = ci;
			}
		}
		affectEntities = ae;
		affectBlocks = ab;
		maxArea = ma;
		mostExpensiveComponent = mec;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("name", name);
		nbt.setInteger("ct", type.ordinal());
		for(int i = 0; i < components.length; i++)
			components[i].writeToNBT(nbt, "ci"+i);
	}

	public static Spell readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("pfs"))
			return MagicalRegistry.getPrefabSpell(nbt.getString("pfs"));
		ArrayList<ComponentInstance> ciList = new ArrayList();
		int i = 0;
		while(nbt.hasKey("ci" + i))
		{
			ComponentInstance ci = ComponentInstance.readFromNBT(nbt, "ci"+i);
			if(ci != null)
				ciList.add(ci);
			i++;
		}
		if(ciList.size() == 0) return null;
		ComponentInstance[] ciArray = new ComponentInstance[ciList.size()];
		ciArray = ciList.toArray(ciArray);
		CastType ct = CastType.values()[nbt.getInteger("ct")];
		return new Spell(nbt.getString("name"),ciArray,ct);
	}

	public double getCost(ICaster caster)
	{
		double cost = 0;
		ISkillHandler sh = null;
		if(caster instanceof EntityCaster)
			sh = SkillRegistry.api.getSkillHandler(((EntityCaster) caster).getCaster());
		for(ComponentInstance ci : components)
			cost += ci.getCost(caster, sh);
		cost *= type.costMult;
		return cost;
	}

	public void addInfo(List<String> list, EntityPlayer pl)
	{
		EntityCaster caster = Helper.getPlayerCaster(pl);
		double cost = getCost(caster);
		list.add("Spell name: " + name);
		list.add(String.format("Spell Cost: %.1f", cost));
	}

	public ResourceLocation getTexture()
	{
		return mostExpensiveComponent.component.getProjectileTexture();
	}

	public UVStore getTextureLocation(int frame)
	{
		return mostExpensiveComponent.component.getProjectileLocation(frame);
	}

	private double xpFunction(ComponentInstance ci, boolean ent)
	{
		if(ent)
			return (ci.cost * MagicConfig.xpCostMult) / (ci.area + 1);
		return (ci.cost * MagicConfig.xpCostMult) / ci.areaCubed;
	}

	public ISkill getMainSkill()
	{
		if(components.length == 0) return null;
		HashMap<ISkill,Double> xpMap = new HashMap();
		for(ComponentInstance c : components)
			xpMap.put(c.getMainSkill(), xpMap.containsKey(c.getMainSkill()) ? c.cost + xpMap.get(c.getMainSkill()) : c.cost);
		Entry<ISkill,Double> best = null;
		for(Entry<ISkill, Double> ent : xpMap.entrySet())
			if((best == null) || (ent.getValue() > best.getValue()))
				best = ent;
		return best.getKey();
	}

	private void applyArea(ICaster caster, SimpleDoubleCoordStore center)
	{
		HashMap<ISkill,Double> xpMap = new HashMap<>();
		if(affectBlocks)
		{
			SimpleCoordStore iCenter = center.floor();
			for(int x = center.iX - maxArea; x <= (center.iX + maxArea); x++)
				for(int y = center.iY - maxArea; y <= (center.iY + maxArea); y++)
					for(int z  =center.iZ - maxArea; z <= (center.iZ + maxArea); z++)
					{
						long d = iCenter.diagonalParadoxDistance(x, y, z);
						{
							boolean inRange = false;
							for(ComponentInstance ci : components)
							{
								if(d > ci.area) continue;
								if(!ci.component.applyToBlock()) continue;
								inRange = true;
								break;
							}
							if(!inRange) continue;
						}
						//Must be in range to be here
						SimpleCoordStore scs = new SimpleCoordStore(center.world, x,y,z);
						if(!MagicConfig.hitSelf && (type != CastType.SELF) && (caster != null) && caster.isCaster(scs)) continue;
						SpellApplyBlockEvent sabe = new SpellApplyBlockEvent(caster, this, scs);
						MinecraftForge.EVENT_BUS.post(sabe);
						for(int i = 0; i < components.length; i++)
						{
							ComponentInstance ci = components[i];
							if(!ci.component.applyToBlock()) continue;
							if(d > ci.area) continue;
							double magMult = sabe.spellMagnitudeMults[i];
							double durMult = sabe.spellDurationMults[i];
							ci.component.apply(caster, scs, -1, (int)(ci.magnitude * magMult), (int)(ci.duration * durMult), ci.config);
							ISkill skill = ci.component.getMainSkill();
							xpMap.put(skill, (xpMap.containsKey(skill) ? xpMap.get(skill) : 0) + xpFunction(ci,false));
						}
					}
		}
		if(affectEntities)
		{
			World w = center.getWorldObj();
			AxisAlignedBB aabb = center.getAABB(maxArea);
			List<Entity> entList = w.getEntitiesWithinAABB(Entity.class, aabb);
			for(Entity e : entList)
			{
				if(!MagicConfig.hitSelf && (type != CastType.SELF) && (caster != null) && caster.isCaster(e)) continue;
				double d = center.distance(new SimpleDoubleCoordStore(e));
				if(d > maxArea) continue;

				{
					boolean inRange = false;
					for(ComponentInstance ci : components)
					{
						if(d > ci.area) continue;
						if(!ci.component.applyToEnt()) continue;
						inRange = true;
						break;
					}
					if(!inRange) continue;
				}

				SpellApplyEntityEvent saee = new SpellApplyEntityEvent(caster, this, e);
				MinecraftForge.EVENT_BUS.post(saee);
				for(int i = 0; i < components.length; i++)
				{
					ComponentInstance ci = components[i];
					if((d > ci.area) || !(ci.component.applyToEnt())) continue;
					double magMult = saee.spellMagnitudeMults[i];
					double durMult = saee.spellDurationMults[i];
					if((magMult <= 0) || (durMult <= 0)) continue;
					ci.component.apply(caster, e, (int)(ci.magnitude * magMult), (int)(ci.duration * durMult), ci.config);
					ISkill skill = ci.component.getMainSkill();
					xpMap.put(skill, (xpMap.containsKey(skill) ? xpMap.get(skill) : 0) + xpFunction(ci,true));
				}
			}
		}

		if(caster instanceof EntityCaster)
			((EntityCaster)caster).applyXP(xpMap);
	}

	/**
	 * Apply the spell effect to a block
	 * @param caster the person who cast this spell
	 * @param scs the coords of the block
	 * @param side TODO
	 */
	public void apply(ICaster caster, SimpleCoordStore scs, int side)
	{
		if((scs.getBlock() instanceof ISpellable) && ((ISpellable)scs.getBlock()).spellHit(scs, this, caster)) return;
		if((scs.getTileEntity() instanceof ISpellable) && ((ISpellable)scs.getTileEntity()).spellHit(scs, this, caster)) return;
		if(maxArea == 0)
		{
			if(!affectBlocks) return;
			HashMap<ISkill,Double> xpMap = new HashMap<>();
			SpellApplyBlockEvent sabe = new SpellApplyBlockEvent(caster, this, scs);
			MinecraftForge.EVENT_BUS.post(sabe);
			for(int i = 0; i < components.length; i++)
			{
				ComponentInstance ci = components[i];
				if(!ci.component.applyToBlock()) continue;
				double magMult = sabe.spellMagnitudeMults[i];
				double durMult = sabe.spellDurationMults[i];
				if((magMult <= 0) || (durMult <= 0)) continue;
				ci.component.apply(caster, scs, side, (int)(ci.magnitude * magMult), (int)(ci.duration * durMult), ci.config);
				ISkill skill = ci.component.getMainSkill();
				xpMap.put(skill, (xpMap.containsKey(skill) ? xpMap.get(skill) : 0) + xpFunction(ci,false));
			}
			if(caster instanceof EntityCaster)
				((EntityCaster)caster).applyXP(xpMap);
		}
		else
			applyArea(caster,scs.getCenter());
	}

	/**
	 * Apply the spell effect to an entity
	 * @param caster the person who cast this spell
	 * @param ent the entity to apply the spell to
	 */
	public void apply(ICaster caster, Entity ent)
	{
		if(maxArea == 0)
		{
			if(!affectEntities) return;
			HashMap<ISkill,Double> xpMap = new HashMap<>();
			SpellApplyEntityEvent saee = new SpellApplyEntityEvent(caster, this, ent);
			MinecraftForge.EVENT_BUS.post(saee);
			for(int i = 0; i < components.length; i++)
			{
				ComponentInstance ci = components[i];
				if(!ci.component.applyToEnt()) continue;
				double magMult = saee.spellMagnitudeMults[i];
				double durMult = saee.spellDurationMults[i];
				if((magMult <= 0) || (durMult <= 0)) continue;
				ci.component.apply(caster, ent, (int)(ci.magnitude * magMult), (int)(ci.duration * durMult), ci.config);
				ISkill skill = ci.component.getMainSkill();
				xpMap.put(skill, (xpMap.containsKey(skill) ? xpMap.get(skill) : 0) + xpFunction(ci,true));
			}
			if(caster instanceof EntityCaster)
				((EntityCaster)caster).applyXP(xpMap);
		}
		else
			applyArea(caster, new SimpleDoubleCoordStore(ent));
	}

	public boolean isValid()
	{
		return (name != null) && (name.length() >= 3) && (components.length > 0);
	}

	public static class SpellNameComparator implements Comparator<Spell>
	{
		public static SpellNameComparator withSkill = new SpellNameComparator(true);
		public static SpellNameComparator noSkill = new SpellNameComparator(false);

		private final boolean useSkill;
		public SpellNameComparator(boolean b)
		{
			useSkill = b;
		}

		@Override
		public int compare(Spell a, Spell b)
		{
			if(useSkill)
			{
				ISkill as = a.getMainSkill();
				ISkill bs = b.getMainSkill();
				if((as != null) && (bs == null)) return -1;
				if((as == null) && (bs != null)) return 1;
				int c = as.getID().compareTo(bs.getID());
				if(c != 0) return c;
			}
			return a.name.compareTo(b.name);
		}

	}

	@Override
	public int hashCode()
	{
		return Objects.hash(Arrays.hashCode(components), name, type);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Spell)) return false;
		Spell other = (Spell) obj;
		if (!Arrays.equals(components, other.components)) return false;
		if(!Objects.equals(name, other.name)) return false;
		if (type != other.type) return false;
		return true;
	}

	public static final PrimMapper<Spell> spellMapper = new PrimMapper<Spell>()
	{
		{
			NBTHelper.register(Spell.class, this);
		}

		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Spell t)
		{
			NBTTagCompound snbt = new NBTTagCompound();
			t.writeToNBT(snbt);
			nbt.setTag(id, snbt);
		}

		@Override
		public Spell readFromNBT(NBTTagCompound nbt, String id)
		{
			return Spell.readFromNBT(nbt.getCompoundTag(id));
		}

	};
}
