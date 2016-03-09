package io.darkcraft.mod.common.magic.spell;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.event.spell.SpellApplyBlockEvent;
import io.darkcraft.mod.common.magic.event.spell.SpellApplyEntityEvent;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import skillapi.api.implement.ISkill;
import skillapi.api.internal.ISkillHandler;

public class Spell
{
	public final String name;
	public final CastType type;
	public final ComponentInstance[] components;

	public Spell(String _name, ComponentInstance[] componentInstances)
	{
		this(_name, componentInstances, CastType.PROJECTILE);
	}

	public Spell(String _name, ComponentInstance[] componentInstances, CastType castType)
	{
		name = _name;
		components = componentInstances.clone();
		type = castType;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("name", name);
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
		return new Spell(nbt.getString("name"),ciArray);
	}

	public double getCost(ICaster caster)
	{
		double cost = 0;
		ISkillHandler sh = null;
		if(caster instanceof EntityCaster)
			sh = SkillRegistry.api.getSkillHandler(((EntityCaster) caster).getCaster());
		for(ComponentInstance ci : components)
			cost += ci.getCost(caster, sh);
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
		return components[0].component.getProjectileTexture();
	}

	private double xpFunction(double cost)
	{
		return cost * MagicConfig.xpCostMult;
	}

	/**
	 * Apply the spell effect to a block
	 * @param caster the person who cast this spell
	 * @param simpleCoordStore the coords of the block
	 */
	public void apply(ICaster caster, SimpleCoordStore simpleCoordStore)
	{
		HashMap<ISkill,Double> xpMap = new HashMap<ISkill,Double>();
		SpellApplyBlockEvent sabe = new SpellApplyBlockEvent(caster, this, simpleCoordStore);
		MinecraftForge.EVENT_BUS.post(sabe);
		for(int i = 0; i < components.length; i++)
		{
			ComponentInstance ci = components[i];
			if(!ci.component.applyToBlock()) continue;
			double magMult = sabe.spellMagnitudeMults[i];
			double durMult = sabe.spellDurationMults[i];
			if((magMult <= 0) || (durMult <= 0)) continue;
			ci.component.apply(caster, simpleCoordStore, (int)(ci.magnitude * magMult), (int)(ci.duration * durMult));
			ISkill skill = ci.component.getMainSkill();
			xpMap.put(skill, (xpMap.containsKey(skill) ? xpMap.get(skill) : 0) + xpFunction(ci.cost));
		}
		if(caster instanceof EntityCaster)
			((EntityCaster)caster).applyXP(xpMap);
	}

	/**
	 * Apply the spell effect to an entity
	 * @param caster the person who cast this spell
	 * @param ent the entity to apply the spell to
	 */
	public void apply(ICaster caster, Entity ent)
	{
		HashMap<ISkill,Double> xpMap = new HashMap<ISkill,Double>();
		SpellApplyEntityEvent saee = new SpellApplyEntityEvent(caster, this, ent);
		MinecraftForge.EVENT_BUS.post(saee);
		for(int i = 0; i < components.length; i++)
		{
			ComponentInstance ci = components[i];
			double magMult = saee.spellMagnitudeMults[i];
			double durMult = saee.spellDurationMults[i];
			if((magMult <= 0) || (durMult <= 0)) continue;
			ci.component.apply(caster, ent, (int)(ci.magnitude * magMult), (int)(ci.duration * durMult));
			ISkill skill = ci.component.getMainSkill();
			xpMap.put(skill, (xpMap.containsKey(skill) ? xpMap.get(skill) : 0) + xpFunction(ci.cost));
		}
		if(caster instanceof EntityCaster)
			((EntityCaster)caster).applyXP(xpMap);
	}
}
