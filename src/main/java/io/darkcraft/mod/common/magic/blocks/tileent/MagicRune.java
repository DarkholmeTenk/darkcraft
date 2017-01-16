package io.darkcraft.mod.common.magic.blocks.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntitySer;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import io.darkcraft.mod.common.magic.systems.symbolic.SymbolState;
import net.minecraft.util.AxisAlignedBB;

@NBTSerialisable
public class MagicRune extends AbstractTileEntitySer
{
	@NBTProperty
	private SymbolState state;

	@Override
	public void init()
	{
		if(state != null)
			state.effect.setTileEntity(this);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return coords().getAABB(1);
	}

	@Override
	public void tick()
	{
		if(state != null)
			state.effect.tick();
		else
			coords().setToAir();
	}
	
	public boolean useCharge(int chargeAmount, boolean sim)
	{
		return true;
	}
}
