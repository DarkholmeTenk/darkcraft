package io.darkcraft.mod.common.magic.items.staff;

import io.darkcraft.api.magic.IStaffable;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.items.MagicComponent;
import io.darkcraft.mod.common.magic.spell.CastType;
import io.darkcraft.mod.common.magic.spell.Spell;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class Staff extends AbstractItem
{

	public Staff()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("staff");
		setMaxStackSize(1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		StaffHelper helper = StaffHelperFactory.getHelper(is);
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
			StaffHelper staffHelper = StaffHelperFactory.getHelper(is);
			PlayerCaster caster = Helper.getPlayerCaster(pl);
			if((staffHelper != null) && (caster.getCurrentSpell() != null))
			{
				Spell spell = caster.getCurrentSpell();
				if(spell.type != CastType.SELF)
					caster.cast(spell, new SimpleCoordStore(w,x,y,z), true);
				else
					caster.cast(spell, pl, true);
				return true;
			}
			TileEntity te = w.getTileEntity(x, y, z);
			if(te instanceof IStaffable)
				if(((IStaffable)te).staffActivate(pl, StaffHelperFactory.getHelper(is)))
			rightClick(is, w, pl);
		}
		return true;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer pl)
	{
		if(ServerHelper.isServer())
		{
			System.out.println("non-block?");
			rightClick(is, w, pl);
		}
		else
			pl.swingItem();
		return is;
	}

	private void rightClick(ItemStack is, World w, EntityPlayer pl)
	{
		StaffHelper helper = StaffHelperFactory.getHelper(is);
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		if((helper != null) && (pl != null))
		{
			Spell spell = pc.getCurrentSpell();
			if(spell != null)
			{
				pc.cast(spell, true);
			}
		}
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new StaffRecipe(new ItemStack(this,1),false,"c","s","s",
				'c', MagicComponent.Type.Crystal.getIS(1),
				's', "stickWood"));
	}

	public static boolean hasStaff(EntityPlayer pl)
	{
		if(pl == null) return false;
		if((pl.getHeldItem() == null) || !(pl.getHeldItem().getItem() instanceof Staff)) return false;
		return true;
	}

}
