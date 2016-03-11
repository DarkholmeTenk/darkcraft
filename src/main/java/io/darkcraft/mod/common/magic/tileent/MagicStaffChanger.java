package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.mod.common.magic.items.staff.ItemStaffHelper;
import io.darkcraft.mod.common.magic.items.staff.ItemStaffHelperFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MagicStaffChanger extends AbstractTileEntity implements IActivatablePrecise
{
	private ItemStaffHelper helper;

	@Override
	public boolean activate(EntityPlayer ent, int side, float x, float y, float z)
	{
		System.out.format("S:%d %.2f,%.2f,%.2f%n", side,x,y,z);
		if(helper != null)
		{

		}
		ItemStaffHelper ish = ItemStaffHelperFactory.getHelper(ent.getHeldItem());
		if(ish != null)
			helper = ish;
		return true;
	}

	public ItemStaffHelper getStaff()
	{
		return helper;
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		if(helper != null)
		{
			NBTTagCompound snbt = new NBTTagCompound();
			helper.writeToNBT(snbt);
			nbt.setTag("sh", snbt);
		}
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		if(nbt.hasKey("sh"))
		{
			NBTTagCompound snbt = nbt.getCompoundTag("sh");
			helper = ItemStaffHelperFactory.getHelper(snbt);
		}
		else
			helper = null;
	}
}
