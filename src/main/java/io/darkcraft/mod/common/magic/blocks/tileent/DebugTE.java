package io.darkcraft.mod.common.magic.blocks.tileent;

import net.minecraft.client.Minecraft;

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
			Minecraft.getMinecraft().effectRenderer.addEffect(new Lightning(getWorldObj(),
					xCoord+0.5,yCoord+1,zCoord+0.5,
					xCoord+0.5,yCoord+3,zCoord+0.5));
	}
}
