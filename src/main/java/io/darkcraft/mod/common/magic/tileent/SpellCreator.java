package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.gui.client.SpellCreationGui;
import net.minecraft.entity.player.EntityPlayer;

public class SpellCreator extends AbstractTileEntity implements IActivatable
{
	private EntityPlayer user = null;
	public void closeGui()
	{
		System.out.println("Gui closed " + ServerHelper.isServer());
		user = null;
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if(ServerHelper.isServer())
		{
			if(user == null)
			{
				user = ent;
				ent.openGui(DarkcraftMod.i, SpellCreationGui.GUI_ID, worldObj, xCoord, yCoord, zCoord);
			}
		}
		return true;
	}

}
