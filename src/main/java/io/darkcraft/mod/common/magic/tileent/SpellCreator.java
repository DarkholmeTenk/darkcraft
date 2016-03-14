package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.darkcore.mod.multiblock.IBlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;
import io.darkcraft.mod.common.magic.gui.client.SpellCreationGui;
import io.darkcraft.mod.common.magic.items.staff.IStaffable;
import io.darkcraft.mod.common.magic.items.staff.StaffHelper;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.entity.player.EntityPlayer;

public class SpellCreator extends AbstractMFTileEntity implements IActivatable, IStaffable
{
	private EntityPlayer user = null;
	private EntityPlayer guiUser = null;

	public void closeGui()
	{
		System.out.println("Gui closed " + ServerHelper.isServer());
		guiUser = null;
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if(ServerHelper.isServer())
		{
			if(ent.getHeldItem() != null) return false;
			if(guiUser == null)
			{
				user = ent;
				guiUser = ent;
				ent.openGui(DarkcraftMod.i, SpellCreationGui.GUI_ID, worldObj, xCoord, yCoord, zCoord);
			}
		}
		return true;
	}

	@Override
	public boolean staffActivate(EntityPlayer player, StaffHelper helper)
	{
		if((user != null) && user.equals(player) && player.isSneaking())
			setSpell(null);
		return false;
	}

	public boolean isValidStructure()
	{
		return true;
	}

	public boolean isUser(EntityPlayer pl)
	{
		if(user == null) return false;
		return user.equals(pl);
	}

	public void setSpell(Spell spell)
	{
		if(spell == null)
		{
			if(user != null)
			{
				MessageHelper.sendMessage(user, "Cleared spell");
				user = null;
				spell = null;
			}
		}
	}

	private static class SpellCreatorStructure implements IMultiBlockStructure
	{

		@Override
		public IBlockState[][][] getStructureDefinition()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCoreX()
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getCoreY()
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getCoreZ()
		{
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
