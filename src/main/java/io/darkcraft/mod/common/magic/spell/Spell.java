package io.darkcraft.mod.common.magic.spell;

import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.SkillRegistry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.api.implement.ISkill;
import skillapi.api.internal.ISkillHandler;

public class Spell
{
	private final String name;
	private final ComponentInstance[] components;

	public Spell(String _name, ComponentInstance[] componentInstances)
	{
		name = _name;
		components = componentInstances;
	}

	private ISkillHandler getHandler(ICaster caster)
	{
		ISkillHandler sh = null;
		if(caster instanceof EntityCaster)
			sh = SkillRegistry.api.getSkillHandler(((EntityCaster)caster).getCaster());
		return sh;
	}

	private double getCost(ComponentInstance ci, ICaster caster, ISkillHandler handler)
	{
		double cost = ci.cost;
		ISkill skill = ci.component.getMainSkill();
		if((skill != null) && (handler != null))
		{
			int min = skill.getMinimumSkillLevel(handler);
			int max = skill.getMaximumSkillLevel(handler);
			if(max == min) return cost;
			int lvl = handler.getLevel(skill);
			double percent = 1 - ((lvl - min) / (double)(max-min));
			cost *= ((percent * (MagicConfig.maxCostMult - MagicConfig.minCostMult)) + MagicConfig.minCostMult);
		}
		return cost;
	}

	private double getCost(ICaster caster)
	{
		double cost = 0;
		ISkillHandler handler = getHandler(caster);
		for(ComponentInstance ci : components)
			cost += getCost(ci, caster, handler);
		return cost;
	}

	private void giveXP(ISkillHandler handler)
	{
		if(handler == null) return;
		for(ComponentInstance ci : components)
		{
			ISkill skill = ci.component.getMainSkill();
			if(skill == null) continue;
			handler.addXP(skill, ci.cost * MagicConfig.xpCostMult);
		}
	}

	private void createSpellInstance(ICaster caster)
	{

	}

	public void cast(ICaster caster)
	{
		double cost = 0;
		ISkillHandler sh = getHandler(caster);
		for(ComponentInstance ci : components)
			cost += getCost(ci, caster, sh);
		if(caster.useMana(cost, false))
		{
			giveXP(sh);
			createSpellInstance(caster);
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("name", name);
		for(int i = 0; i < components.length; i++)
			components[i].writeToNBT(nbt, "ci"+i);
	}

	public static Spell readFromNBT(NBTTagCompound nbt)
	{
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

	public void addInfo(List<String> list, EntityPlayer pl)
	{
		EntityCaster caster = Helper.getCaster(pl);
		double cost = getCost(caster);
		list.add("Spell name: " + name);
		list.add(String.format("Spell Cost: %.1f", cost));
	}
}
