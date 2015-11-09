package io.darkcraft.mod.common.items.staff;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.mod.DarkcraftMod;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemStaff extends AbstractItem
{

	public ItemStaff()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("staff");
		setMaxStackSize(1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		ItemStaffHelper helper = ItemStaffHelperFactory.getHelper(is);
		if(helper != null)
		{
			helper.addInfo(infoList, player);
		}
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
