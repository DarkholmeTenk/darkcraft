package io.darkcraft.mod.common.magic.blocks;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicStaffChanger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MagicTouchPassBlock extends AbstractBlock
{
	public MagicTouchPassBlock()
	{
		super(false, DarkcraftMod.modName);
	}

	@Override
	public void initData()
	{
		setBlockName("MagicTouchPass");
		setSubNames("StaffChanger");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer pl, int s, float i, float j, float k)
	{
		int meta = w.getBlockMetadata(x,y,z);
		if(meta == 0)
		{
			for(int c = 1; c <= 2; c++)
			{
				TileEntity te = w.getTileEntity(x, y-c, z);
				if(te instanceof MagicStaffChanger)
				{
					((MagicStaffChanger)te).activate(pl, s, i,j+c,k);
					return true;
				}
			}
		}
		return false;
	}

}
