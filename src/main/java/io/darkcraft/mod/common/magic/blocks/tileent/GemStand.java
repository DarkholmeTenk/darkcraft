package io.darkcraft.mod.common.magic.blocks.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.common.magic.items.SoulGem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GemStand extends AbstractTileEntity implements IActivatable
{
	ItemStack is;

	@Override
	public void tick()
	{

	}

	public SoulGem.Size getGemSize()
	{
		return SoulGem.getGemSize(is);
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		return false;
	}

	public boolean isGemFull()
	{
		return SoulGem.getSoulSize(is) != null;
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		NBTTagCompound isNBT = new NBTTagCompound();
		is.writeToNBT(isNBT);
		nbt.setTag("is", isNBT);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("is"))
			is = null;
		is = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("is"));
	}
}
