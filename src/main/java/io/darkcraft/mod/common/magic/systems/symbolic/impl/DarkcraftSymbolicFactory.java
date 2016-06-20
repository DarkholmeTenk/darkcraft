package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicFactory;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import io.darkcraft.mod.common.magic.systems.symbolic.SymbolicRegistry;
import net.minecraft.nbt.NBTTagCompound;

public class DarkcraftSymbolicFactory implements ISymbolicFactory
{
	{
		SymbolicRegistry.registerFactory(this);
	}

	@Override
	public String match(String glyphs)
	{
		if(glyphs.startsWith("targetmobs")) return "dc.targetmobs";
		if(glyphs.startsWith("magicka.sila")) return "dc.manaregen";
		return null;
	}

	@Override
	public ISymbolicSpell createSpell(String id, String glyphs, SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		if(id == "dc.targetmobs") return new TargetMobsSymbolicSpell(rootRune, center);
		if(id == "dc.manaregen") return new ManaRegenSymbolicSpell(rootRune, center);
		return null;
	}

	@Override
	public ISymbolicSpell loadSpell(String id, String glyphs, NBTTagCompound nbt)
	{
		return null;
	}

}
