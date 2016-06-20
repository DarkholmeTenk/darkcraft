package io.darkcraft.mod.common.magic.systems.soulspell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public class SoulSpellRegistry
{
	private static List<ISoulSpellFactory> facts = new ArrayList();

	public static void registerFactory(ISoulSpellFactory factory)
	{
		facts.add(factory);
	}

	public static ISoulSpell getSoulSpell(String id, NBTTagCompound nbt)
	{
		for(ISoulSpellFactory f : facts)
		{
			ISoulSpell ss = f.createSoulSpell(id, nbt);
			if(ss != null)
				return ss;
		}
		return null;
	}
}
