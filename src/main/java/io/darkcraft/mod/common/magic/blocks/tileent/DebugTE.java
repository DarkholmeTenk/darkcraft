package io.darkcraft.mod.common.magic.blocks.tileent;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntitySer;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.client.particles.Lightning;

public class DebugTE extends AbstractTileEntitySer
{

	@Override
	public void tick()
	{
		if(ServerHelper.isServer())
			return;
		if((tt % 50) == 0)
		{
			EntityPlayer p = Minecraft.getMinecraft().thePlayer;
			Minecraft.getMinecraft().effectRenderer.addEffect(new Lightning(getWorldObj(),
					xCoord+0.5,yCoord+1,zCoord+0.5,
					p.posX, p.posY, p.posZ));
		}
	}
}
