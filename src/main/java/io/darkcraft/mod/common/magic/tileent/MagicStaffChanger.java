package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.mod.common.magic.items.staff.StaffHelper;
import io.darkcraft.mod.common.magic.items.staff.StaffHelperFactory;
import io.darkcraft.mod.common.magic.items.staff.parts.IStaffPart;
import io.darkcraft.mod.common.magic.items.staff.parts.StaffPartRegistry;
import io.darkcraft.mod.common.magic.items.staff.parts.StaffPartType;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MagicStaffChanger extends AbstractTileEntity implements IActivatablePrecise
{
	private StaffHelper helper;

	private void changePart(StaffPartType type, int d)
	{
		IStaffPart part = helper.getStaffPart(type);
		if(part == null)
		{
			System.err.println("NULL STAFFPART WUT?!");
			return;
		}
		String name = part.id();
		List<String> names = StaffPartRegistry.getStaffPartNames(type, true);
		int index = names.indexOf(name);
		index = MathHelper.cycle(index+d, 0, names.size()-1);
		IStaffPart newPart = StaffPartRegistry.getStaffPart(type, names.get(index));
		helper.setStaffPart(newPart, type);
		queueUpdate();
	}

	@Override
	public boolean activate(EntityPlayer ent, int side, float x, float y, float z)
	{
		//System.out.format("S:%d %.2f,%.2f,%.2f%n", side,x,y,z);
		if(!ServerHelper.isIntegratedClient())
		{
			boolean used = false;
			if((helper != null) && (side == 3))
			{
				if((y > 1.1) && (y < 1.25))
				{
					used = true;
					if(x < 0.15)
						changePart(StaffPartType.HEAD, -1);
					else if(x < 0.3)
						changePart(StaffPartType.HEAD, 1);
					else if(x > 0.85)
						changePart(StaffPartType.BOTTOM, -1);
					else if(x > 0.7)
						changePart(StaffPartType.BOTTOM, 1);
					else if((x > 0.38) && (x < 0.5))
						changePart(StaffPartType.SHAFT, -1);
					else if((x > 0.5) && (x < 0.62))
						changePart(StaffPartType.SHAFT, 1);
					else
						used = false;
				}
			}
			if(!used)
			{
				if(helper == null)
				{
					StaffHelper ish = StaffHelperFactory.getHelper(ent.getHeldItem());
					if(ish != null)
					{
						helper = ish;
						ent.inventory.setInventorySlotContents(ent.inventory.currentItem, null);
						queueUpdate();
					}
				}
				else
				{
					if(ent.getHeldItem() == null)
					{
						ItemStack is = helper.getIS(true);
						if(is != null)
						{
							helper = null;
							WorldHelper.giveItemStack(ent, is);
							queueUpdate();
						}
					}
				}
			}
		}
		return true;
	}

	public StaffHelper getStaff()
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
			helper = StaffHelperFactory.getHelper(snbt);
		}
		else
			helper = null;
	}
}
