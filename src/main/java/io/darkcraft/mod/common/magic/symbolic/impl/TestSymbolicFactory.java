package io.darkcraft.mod.common.magic.symbolic.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.symbolic.ISymbolicFactory;
import io.darkcraft.mod.common.magic.symbolic.ISymbolicSpell;
import io.darkcraft.mod.common.magic.symbolic.SymbolicRegistry;
import net.minecraft.nbt.NBTTagCompound;

public class TestSymbolicFactory implements ISymbolicFactory
{
	{
		SymbolicRegistry.registerFactory(this);
	}

	@Override
	public String match(String glyphs)
	{
		if(glyphs.equals("testtest")) return "dc.test";
		return null;
	}

	@Override
	public ISymbolicSpell createSpell(String id, String glyphs, SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		if(id == "dc.test") return new TestSymbolicSpell();
		return null;
	}

	@Override
	public ISymbolicSpell loadSpell(String id, String glyphs, NBTTagCompound nbt)
	{
		return null;
	}

}
