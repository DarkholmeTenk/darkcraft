package io.darkcraft.darkcraft.mod.common.spellsystem.components.effects;

import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.components.modifiers.DamageMod;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;

public class Damage implements ISpellEffect
{
	private int strength = 1;
	private static final int baseDam = 2;
	
	@Override
	public double getBaseCost()
	{
		return 1;
	}

	@Override
	public void applyEffect(EntityLivingBase ent)
	{
		ent.attackEntityFrom(DamageSource.generic, baseDam*strength);
	}

	@Override
	public void applyEffect(SimpleCoordStore scs)
	{
		if(strength > 1)
		{
			World w = scs.getWorldObj();
			if(w.getBlock(scs.x, scs.y, scs.z) == Blocks.cobblestone)
				w.setBlock(scs.x, scs.y, scs.z, Blocks.sand,0,3);
		}
	}

	@Override
	public int getInterval()
	{
		return 4;
	}

	@Override
	public void applyModifiers(Set<ISpellModifier> modifiers)
	{
		for(ISpellModifier mod : modifiers)
		{
			if(mod instanceof DamageMod)
				strength = 1 + mod.getStrength();
		}
	}

	@Override
	public String getID()
	{
		return "dam";
	}

	@Override
	public ISpellEffect create()
	{
		return new Damage();
	}

}
