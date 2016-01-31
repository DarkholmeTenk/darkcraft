package io.darkcraft.mod.common.magic.items.staff;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.spell.Spell;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
	public boolean onItemUse(ItemStack is, EntityPlayer pl, World w, int x, int y, int z, int s, float i, float j, float k)
    {
		if(ServerHelper.isServer())
		{
			TileEntity te = w.getTileEntity(x, y, z);
			if(te instanceof IStaffable)
				if(((IStaffable)te).staffActivate(pl, ItemStaffHelperFactory.getHelper(is)))
			rightClick(is, w, pl);
		}
		return true;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		if(ServerHelper.isServer())
		{
			System.out.println("non-block?");
			rightClick(is, world, player);
		}
		else
			player.swingItem();
		return is;
	}

	private void rightClick(ItemStack is, World w, EntityPlayer pl)
	{
		ItemStaffHelper helper = ItemStaffHelperFactory.getHelper(is);
		if((helper != null) && (pl != null))
		{
			Spell spell = helper.getSpell();
			if(spell != null)
				spell.cast(Helper.getCaster(pl));
		}
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
