package io.darkcraft.mod.common.magic.systems.symbolic;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTConstructor;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;

@NBTSerialisable
public class SymbolState
{
	public static final Mapper<SymbolState> mapper = NBTHelper.getMapper(SymbolState.class, SerialisableType.WORLD);

	@NBTProperty
	public final String[] rings;

	@NBTConstructor("rings")
	public SymbolState(String[] rings)
	{
		this.rings = rings;
	}

	public int getLevel()
	{
		return rings.length - 1;
	}
}