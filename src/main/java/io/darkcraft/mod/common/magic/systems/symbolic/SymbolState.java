package io.darkcraft.mod.common.magic.systems.symbolic;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTConstructor;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTMethod;
import io.darkcraft.darkcore.mod.nbt.NBTMethod.Type;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import io.darkcraft.mod.common.magic.systems.symbolic.type.SymbolicEffect;

@NBTSerialisable
public class SymbolState
{
	public static final Mapper<SymbolState> mapper = NBTHelper.getMapper(SymbolState.class, SerialisableType.WORLD);

	@NBTProperty
	public final String[] rings;

	public final SymbolicEffect effect;

	@NBTConstructor("rings")
	public SymbolState(String[] rings)
	{
		this.rings = rings;
		effect = SymbolsRegistry.getFinalSymbol(rings);
	}

	public int getLevel()
	{
		return rings.length - 1;
	}

	@NBTMethod(Type.WRITE)
	public void writeToNBT(NBTTagCompound nbt)
	{
		effect.writeToNBT(nbt);
	}

	@NBTMethod(Type.READ)
	public void readFromNBT(NBTTagCompound nbt)
	{
		effect.readFromNBT(nbt);
	}
}