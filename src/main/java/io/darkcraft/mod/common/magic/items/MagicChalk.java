package io.darkcraft.mod.common.magic.items;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.item.MagicChalkRenderer;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicSymbol;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

public class MagicChalk extends AbstractItem
{
	public MagicChalk()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("MagicChalk");
		setSubNames(ChalkType.getNames());
		setMaxStackSize(1);
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer pl, World w, int x, int y, int z, int s, float i, float j, float k)
    {
		if(ServerHelper.isClient()) return true;
		if(ForgeDirection.VALID_DIRECTIONS[s] != ForgeDirection.UP) return true;
		if(!w.isAirBlock(x, y+1, z)) return true;
		Character c = getChar(is);
		if(c != null)
		{
			w.setBlock(x, y+1, z, ItemBlockRegistry.magicSymbol);
			TileEntity te = w.getTileEntity(x, y+1, z);
			if(te instanceof MagicSymbol)
			{
				((MagicSymbol)te).setCharacter(c);
				damage(is);
				if(damage(is))
					pl.inventory.decrStackSize(pl.inventory.currentItem, 1);
			}
		}
		return true;
    }

	public static boolean damage(ItemStack is)
	{
		if(is.stackTagCompound == null) return false;
		double nd = is.stackTagCompound.getDouble("dam")+0.002;
		is.stackTagCompound.setDouble("dam", nd);
		return nd >= 1;
	}

	public static String getString(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk) || (is.stackTagCompound == null)) return null;
		return is.stackTagCompound.getString("chalkText");
	}

	public static Character getChar(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk) || (is.stackTagCompound == null)) return null;
		String d = getString(is);
		int slot = is.stackTagCompound.getInteger("slot");
		if(d == null) return null;
		char c = d.charAt(slot % d.length());
		is.stackTagCompound.setInteger("slot", ++slot);
		return c;
	}

	public static void setString(ItemStack is, String string)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk)) return;
		if(is.stackTagCompound == null)	is.stackTagCompound = new NBTTagCompound();
		is.stackTagCompound.setString("chalkText", string);
		is.stackTagCompound.setInteger("slot", 0);
	}

	public static double getISDamage(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalk)) return 0;
		if(is.stackTagCompound == null) return 0;
		if(!is.stackTagCompound.hasKey("dam")) is.stackTagCompound.setDouble("dam", 0);
		return is.stackTagCompound.getDouble("dam");
	}

	@Override
	public IItemRenderer getRenderer()
	{
		return new MagicChalkRenderer();
	}

}
