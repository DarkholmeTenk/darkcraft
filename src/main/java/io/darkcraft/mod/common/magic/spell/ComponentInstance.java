package io.darkcraft.mod.common.magic.spell;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IDurationComponent;
import io.darkcraft.mod.common.magic.component.IMagnitudeComponent;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import skillapi.api.implement.ISkill;
import skillapi.api.internal.ISkillHandler;

public class ComponentInstance
{
	public final IComponent component;
	public final int magnitude;
	public final int duration;
	public final double cost;
	public final int area;

	public ComponentInstance(IComponent comp, int mag, int dur){this(comp,mag,dur,0);}

	public ComponentInstance(IComponent comp, int mag, int dur, int _area)
	{
		component = comp;
		double tempCost = comp.getCost();
		if(comp instanceof IMagnitudeComponent)
		{
			IMagnitudeComponent magComp = (IMagnitudeComponent) comp;
			magnitude = MathHelper.clamp(mag, magComp.getMinMagnitude(), magComp.getMaxMagnitude());
			tempCost = magComp.getCostMag(magnitude, tempCost);
		}
		else
			magnitude = 1;

		if(comp instanceof IDurationComponent)
		{
			IDurationComponent durComp = (IDurationComponent) comp;
			duration = MathHelper.clamp(dur, durComp.getMinDuration(), durComp.getMaxDuration());
			tempCost = durComp.getCostDur(duration, tempCost);
		}
		else
			duration = 1;
		area = _area;
		if(area > 0)
			tempCost *= Math.pow(1.5, area);
		cost = tempCost;
	}

	public ISkill getMainSkill()
	{
		return component.getMainSkill();
	}

	public double getCost(ICaster caster, ISkillHandler handler)
	{
		double cost = this.cost;
		ISkill skill = component.getMainSkill();
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

	public String getDurationString()
	{
		if(!(component instanceof IDurationComponent)) return "";
		return MathHelper.getTimeString(duration);
	}

	public String getMagnitudeString()
	{
		if(!(component instanceof IMagnitudeComponent)) return "";
		return magnitude+"";
	}

	public String getAreaString()
	{
		return area + " br";
	}

	@Override
	public String toString()
	{
		String s = StatCollector.translateToLocal(component.getUnlocalisedName());
		if(component instanceof IMagnitudeComponent)
			s += " " + getMagnitudeString();
		if(component instanceof IDurationComponent)
			s += " for " + getDurationString();
		if(area > 0)
			s += " over " + getAreaString();
		return s;
	}

	public void writeToNBT(NBTTagCompound nbt, String id)
	{
		NBTTagCompound sub = new NBTTagCompound();
		sub.setString("id", component.id());
		sub.setInteger("mag", magnitude);
		sub.setInteger("dur", duration);
		sub.setInteger("area", area);
		nbt.setTag(id, sub);
	}

	public static ComponentInstance readFromNBT(NBTTagCompound nbt, String id)
	{
		if(!nbt.hasKey(id)) return null;
		NBTTagCompound sub = nbt.getCompoundTag(id);
		String cid = sub.getString("id");
		int mag = sub.getInteger("mag");
		int dur = sub.getInteger("dur");
		int area = sub.getInteger("area");
		IComponent component = SpellPartRegistry.getComponent(cid);
		if(component != null)
			return new ComponentInstance(component, mag, dur, area);
		return null;
	}
}
