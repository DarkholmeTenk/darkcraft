package io.darkcraft.mod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import io.darkcraft.mod.common.magic.blocks.tileent.SpellCreator;
import io.darkcraft.mod.common.magic.gui.ChalkContainer;
import io.darkcraft.mod.common.magic.gui.SpellCreationContainer;

import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	ParticleHandler particleHandler;

	public void init(){}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		switch(id)
		{
			case 1000: return new ChalkContainer(player);
			case 1397: return new SpellCreationContainer((SpellCreator)te);
			case 1399: return new ChalkContainer(player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
