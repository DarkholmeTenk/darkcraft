package io.darkcraft.darkcraft.mod.common.spellsystem;

import io.darkcraft.darkcraft.mod.common.registries.BaseSpellRegistry;
import io.darkcraft.darkcraft.mod.common.registries.SpellComponentRegistry;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

public class SpellHelper
{
	public static void writeToNBT(NBTTagCompound nbt, LinkedList<ISpellShape> shps, Set<ISpellEffect> effs,
			Set<ISpellModifier> mdrs)
	{
		StringBuilder shapes =  new StringBuilder();
		for(ISpellShape shape : shps)
			if(shape != null)
				shapes.append(shape.getID()).append(':');
		nbt.setString("shapes", shapes.toString());
		
		StringBuilder effects = new StringBuilder();
		for(ISpellEffect effect : effs)
			if(effect != null)
				effects.append(effect.getID()).append(':');
		nbt.setString("effects", effects.toString());
		
		StringBuilder modifiers = new StringBuilder();
		for(ISpellModifier modifier : mdrs)
			if(modifier != null)
				for(int i = 0; i < modifier.getStrength();i++)
					modifiers.append(modifier.getID()).append(':');
		nbt.setString("modifiers", modifiers.toString());
	}
	
	public static LinkedList<ISpellShape> readShapes(String baseData)
	{
		LinkedList<ISpellShape> shapes = new LinkedList<ISpellShape>();
		if(baseData == null)
			return shapes;
		if(BaseSpellRegistry.shapeRegistry.containsKey(baseData))
			return BaseSpellRegistry.shapeRegistry.get(baseData);
		String[] individualEffects = baseData.split(":");
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisShape = individualEffects[i];
			if(thisShape.length() < 1)
				continue;
			ISpellShape shape = SpellComponentRegistry.getShape(thisShape);
			if(shape != null)
				shapes.addLast(shape);
		}
		return shapes;
	}
	
	public static LinkedList<ISpellShape> readShapes(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("shapes");
		return readShapes(baseData);
	}
	
	public static Set<ISpellEffect> readEffects(String baseData)
	{
		HashSet<ISpellEffect> effects = new HashSet<ISpellEffect>();
		if(baseData == null)
			return effects;
		String[] individualEffects = baseData.split(":");
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisEffect = individualEffects[i];
			if(thisEffect.length() < 1)
				continue;
			ISpellEffect effect = SpellComponentRegistry.getEffect(thisEffect);
			if(effect != null)
				effects.add(effect);
		}
		return effects;
	}
	
	public static Set<ISpellEffect> readEffects(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("effects");
		return readEffects(baseData);
	}
	
	public static Set<ISpellModifier> readModifiers(String baseData)
	{
		HashMap<ISpellModifier,ISpellModifier> modifiers = new HashMap<ISpellModifier,ISpellModifier>();
		HashSet<ISpellModifier> set = new HashSet<ISpellModifier>();
		if(baseData == null)
			return set;
		String[] individualEffects = baseData.split(":");
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisEffect = individualEffects[i];
			if(thisEffect.length() < 1)
				continue;
			ISpellModifier modifier = SpellComponentRegistry.getModifier(thisEffect);
			if(modifier != null)
			{
				if(!modifiers.containsKey(modifier))
					modifiers.put(modifier,modifier);
				else
				{
					ISpellModifier m = modifiers.get(modifier);
					m.setStrength(m.getStrength()+1);
				}
			}
		}
		set.addAll(modifiers.values());
		return set;
	}
	
	public static Set<ISpellModifier> readModifiers(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("modifiers");
		return readModifiers(baseData);
	}
}
