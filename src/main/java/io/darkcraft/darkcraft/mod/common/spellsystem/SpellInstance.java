package io.darkcraft.darkcraft.mod.common.spellsystem;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcraft.mod.DarkcraftMod;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class SpellInstance
{
	private Random							rand				= new Random();
	private final LinkedList<ISpellShape>	remainingShapes;
	private final Set<ISpellEffect>	effects;
	private final Set<ISpellModifier>	mods;
	private final SimpleDoubleCoordStore	pos;
	private boolean							dead				= false;
	private int								age					= -1;
	private int								extAge				= -1;
	private static final int				ageMult				= 10;
	private static final double				tolerance			= 0.5;

	private ISpellShape						lastShape;
	private HashSet<SimpleDoubleCoordStore>	affectedPositions	= new HashSet<SimpleDoubleCoordStore>();
	private int								maxAge				= 0;

	private int getStartOffset()
	{
		return rand.nextInt(ageMult - 1) - (ageMult - 1);
	}

	public SpellInstance(BaseSpell base, SimpleDoubleCoordStore _pos, Set<SimpleDoubleCoordStore> affected)
	{
		this(base.getShapes(), base.getEffects(), base.getMods(), -1, _pos, affected);
	}

	private SpellInstance(LinkedList<ISpellShape> shapes, Set<ISpellEffect> e, Set<ISpellModifier> m, int a,
			SimpleDoubleCoordStore _pos, Set<SimpleDoubleCoordStore> affected)
	{
		remainingShapes = shapes;
		effects = e;
		mods = m;
		
		for (ISpellShape shape : shapes)
			shape.applyModifiers(mods);
		for (ISpellEffect effect : effects)
			effect.applyModifiers(mods);
		
		age = a;
		pos = _pos;
		extAge = getStartOffset();

		lastShape = shapes.getLast();
		if (affected == null)
			affectedPositions.add(_pos);
		else
			affectedPositions.addAll(affected);
		for (ISpellShape shape : shapes)
		{
			HashSet<SimpleDoubleCoordStore> tempStore = (HashSet<SimpleDoubleCoordStore>)affectedPositions.clone();
			for (SimpleDoubleCoordStore newPos : affectedPositions)
				tempStore.addAll(shape.getNewLocations(newPos));
			affectedPositions = MathHelper.removeDuplicateLocations(tempStore, tolerance);
			System.out.println("S:"+shape.getID()+":"+shape.getDuration());
			maxAge = Math.max(maxAge, shape.getDuration());
			int temp = shape.getDuration();
			temp *= MathHelper.ceil(maxAge / (double) temp);
			maxAge += (temp % maxAge);
		}
		System.out.println("MaxAge: " + maxAge);
		DarkcraftMod.spellInstanceRegistry.registerSpellInstance(this);
	}

	public void die()
	{
		// System.out.println("Dead spell instance!");
		dead = true;
	}

	public boolean isDead()
	{
		return dead;
	}
	
	public void tick()
	{
		extAge++;
		if (extAge >= 0 && extAge % ageMult != 0)
			return;
		//System.out.println("Ticking SI");
		age++;
		if (age >= maxAge && age > 0)
		{
			die();
			return;
		}
		boolean apply = false;
		for (ISpellEffect e : effects)
			if (age % e.getInterval() == 0)
				apply = true;
		if (!apply) // None of the effects will actually do anything this tick so skip it
			return;
		for (SimpleDoubleCoordStore affPos : affectedPositions)
		{
			Set<EntityLivingBase> ents = lastShape.getAffectedEnts(affPos);
			Set<SimpleCoordStore> blocks = lastShape.getAffectedBlocks(affPos);
			for (ISpellEffect e : effects)
			{
				//System.out.println("Ticking effect " + e.getID());
				if (age % e.getInterval() != 0)
					continue;
				if (ents != null)
					for (EntityLivingBase ent : ents)
						e.applyEffect(ent);
				if (blocks != null)
					for (SimpleCoordStore scs : blocks)
						e.applyEffect(scs);
			}
		}
	}

	public static SpellInstance readFromNBT(NBTTagCompound nbt)
	{
		LinkedList<ISpellShape> shapes = SpellHelper.readShapes(nbt);
		Set<ISpellEffect> effects = SpellHelper.readEffects(nbt);
		Set<ISpellModifier> mods = SpellHelper.readModifiers(nbt);
		for (ISpellShape shape : shapes)
			shape.applyModifiers(mods);
		for (ISpellEffect effect : effects)
			effect.applyModifiers(mods);
		int age = nbt.getInteger("age");
		SimpleDoubleCoordStore pos = SimpleDoubleCoordStore.readFromNBT(nbt);
		return new SpellInstance(shapes, effects, mods, age, pos, null);
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		SpellHelper.writeToNBT(nbt, remainingShapes, effects, mods);
		nbt.setInteger("age", age);
		pos.writeToNBT(nbt);
	}
}
